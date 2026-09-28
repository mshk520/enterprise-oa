package com.mingxing.framework.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mingxing.common.core.domain.model.LoginUser;
import com.mingxing.common.utils.spring.SpringUtils;
import com.mingxing.framework.web.service.TokenService;

/**
 * WebSocket 服务端广播端点
 *
 * 兼容两种建连方式（缓存了老版本代码的浏览器仍会走 /ws/{token}）：
 *   1) GET ws(s)://host/ws             ?token=xxx   ← 推荐，JWT 字符安全，新前端使用
 *   2) GET ws(s)://host/ws/{token}                  ← 向前兼容，老前端缓存路径
 *
 * 每个连接保存真实登录用户名 + 权限集合，支持按权限/按 loginName 精准推送，避免把审批通知推给普通用户。
 */
@Component
@ServerEndpoint("/ws")
public class NoticeWebSocket {
    private static final Logger log = LoggerFactory.getLogger(NoticeWebSocket.class);
    private static final Map<String, NoticeWebSocket> onlineUsers = new ConcurrentHashMap<>();
    private Session session;
    /** 连接唯一 key：u_tokenHash_clientType，同账号多端可同时在线 */
    private String connKey;
    /** 真实登录用户名（取自 LoginUser.username），用于按用户推送 */
    private volatile String loginName;
    /** 当前登录用户权限集合（取自 LoginUser.permissions），null 表示解析失败/未登录状态 */
    private volatile Set<String> permissions;
    /** 是否是超级管理员（admin 账号 / user.isAdmin()），admin 天然拥有所有权限判断 */
    private volatile boolean admin;

    @OnOpen
    public void onOpen(Session session) throws IOException {
        onOpen(session, null);
    }

    /**
     * 供向前兼容的 /ws/{token} 端点在 onOpen 时桥接调用（保持一个类里的连接逻辑、清理逻辑、广播逻辑）。
     */
    public void onOpen(Session session, @PathParam("token") String pathToken) throws IOException {
        ResolvedToken resolved = resolveToken(session, pathToken);
        if (resolved == null || resolved.token.isEmpty()) {
            log.warn("WebSocket rejected: missing token, uri={}, qs={}",
                    session.getRequestURI(), session.getQueryString());
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Missing token"));
            } catch (IOException ignored) {
            }
            return;
        }
        // 拒绝未指定 clientType 的旧客户端连接，静默关闭避免死循环刷日志
        if ("unknown".equals(resolved.clientType)) {
            try {
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Upgrade required: clientType missing"));
            } catch (IOException ignored) {
            }
            return;
        }
        this.session = session;
        this.connKey = "u_" + Math.abs(resolved.token.hashCode()) + "_" + resolved.clientType;
        // 同 token 重复连接：新连接接管，关闭旧连接（旧连接多为页面刷新/热更新残留的僵尸连接，
        // 若拒绝新连接，前端会无限重连刷日志；后连接的永远是更可靠的会话）
        NoticeWebSocket existing = onlineUsers.put(this.connKey, this);
        if (existing != null) {
            log.info("WebSocket duplicate connKey={}, new session takes over, closing old session", this.connKey);
            try {
                if (existing.session != null && existing.session.isOpen()) {
                    existing.session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Replaced by new connection"));
                }
            } catch (IOException ignored) {
            }
        }
        // 解析真实用户 + 权限
        // 先尝试从 Redis 缓存获取完整 LoginUser（含 permissions、admin 标志）
        boolean tokenResolved = false;
        try {
            TokenService tokenService = SpringUtils.getBean(TokenService.class);
            if (tokenService != null) {
                LoginUser loginUser = tokenService.getLoginUser(resolved.token);
                if (loginUser != null && loginUser.getUser() != null) {
                    this.loginName = loginUser.getUsername();
                    this.permissions = loginUser.getPermissions();
                    this.admin = loginUser.getUser().isAdmin();
                    tokenResolved = true;
                }
            }
        } catch (Exception e) {
            log.warn("WebSocket 解析登录用户(第1次)失败, connKey={}, cause={}", this.connKey, e.getMessage());
        }

        // Redis 缓存可能尚未就绪，异步重试最多 3 次（每次间隔 200ms）
        if (!tokenResolved) {
            final String tokenForRetry = resolved.token;
            final String connKey = this.connKey;
            for (int i = 1; i <= 3 && !tokenResolved; i++) {
                try {
                    Thread.sleep(200L);
                    TokenService tokenService = SpringUtils.getBean(TokenService.class);
                    if (tokenService != null) {
                        LoginUser loginUser = tokenService.getLoginUser(tokenForRetry);
                        if (loginUser != null && loginUser.getUser() != null) {
                            this.loginName = loginUser.getUsername();
                            this.permissions = loginUser.getPermissions();
                            this.admin = loginUser.getUser().isAdmin();
                            tokenResolved = true;
                            log.info("WebSocket 重试第{}次解析成功, connKey={}, loginName={}", i, connKey, this.loginName);
                        }
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    log.warn("WebSocket 第{}次重试失败, connKey={}, cause={}", i, connKey, e.getMessage());
                }
            }
        }

        // 最后兜底：如果 Redis 始终查不到，直接从 JWT 中提取用户名
        // 仅用于排除申请人本人，权限判断在无法获取时降级为登录名匹配
        if (this.loginName == null || this.loginName.isEmpty()) {
            try {
                TokenService tokenService = SpringUtils.getBean(TokenService.class);
                if (tokenService != null) {
                    String username = tokenService.getUsernameFromToken(resolved.token);
                    if (username != null && !username.isEmpty()) {
                        this.loginName = username;
                        log.info("WebSocket 从JWT直接提取用户名(兜底), connKey={}, loginName={}", this.connKey, this.loginName);
                    }
                }
            } catch (Exception e) {
                log.warn("WebSocket JWT解析兜底也失败, connKey={}, cause={}", this.connKey, e.getMessage());
            }
        }
        log.info("WebSocket connected: connKey={}, loginName={}, admin={}, perms={}, total={}",
                this.connKey, this.loginName, this.admin,
                this.permissions != null ? this.permissions.size() : 0,
                onlineUsers.size());
    }

    private static class ResolvedToken {
        final String token;
        final String clientType;
        ResolvedToken(String token, String clientType) {
            this.token = token;
            this.clientType = clientType == null || clientType.isEmpty() ? "unknown" : clientType;
        }
    }

    private ResolvedToken resolveToken(Session session, String pathToken) {
        // 方式一：?token=xxx&clientType=xxx（新路径 /ws）
        String qs = session.getQueryString();
        if (qs != null && !qs.isEmpty()) {
            Map<String, String> params = parseQuery(qs);
            String token = params.get("token");
            String clientType = params.getOrDefault("clientType", "unknown");
            if (token != null && !token.isEmpty()) {
                try {
                    return new ResolvedToken(URLDecoder.decode(token, StandardCharsets.UTF_8.name()),
                            URLDecoder.decode(clientType, StandardCharsets.UTF_8.name()));
                } catch (UnsupportedEncodingException e) {
                    return new ResolvedToken(token, clientType);
                }
            }
        }
        // 方式二：/ws/{token} 路径参数（旧代码路径，向前兼容）
        if (pathToken != null && !pathToken.isEmpty()) {
            try {
                return new ResolvedToken(URLDecoder.decode(pathToken, StandardCharsets.UTF_8.name()), "legacy");
            } catch (UnsupportedEncodingException e) {
                return new ResolvedToken(pathToken, "legacy");
            }
        }
        return null;
    }

    private Map<String, String> parseQuery(String qs) {
        Map<String, String> map = new LinkedHashMap<>();
        String[] pairs = qs.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                map.put(pair.substring(0, idx), pair.substring(idx + 1));
            }
        }
        return map;
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        if ("ping".equals(message)) {
            session.getBasicRemote().sendText("pong");
        }
    }

    @OnClose
    public void onClose() {
        if (this.connKey != null) {
            onlineUsers.remove(this.connKey, this);
            log.info("WebSocket disconnected: connKey={}, loginName={}, total={}",
                    this.connKey, this.loginName, onlineUsers.size());
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket error, connKey={}, loginName={}, cause={}",
                this.connKey, this.loginName, error == null ? "null" : error.getMessage(), error);
    }

    private void sendIfOpen(String message) {
        if (this.session != null && this.session.isOpen()) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("Send message error, connKey={}, loginName={}", this.connKey, this.loginName, e);
            }
        }
    }

    /**
     * 推给指定连接 key（注意：key 是 u_hash_clientType，不是真实用户名）。
     * 大多数业务请使用 {@link #sendToLoginName(String, String)} 或 {@link #broadcastByPermission(String, String, Collection)}。
     */
    public static void sendMessage(String connKey, String message) {
        NoticeWebSocket endpoint = onlineUsers.get(connKey);
        if (endpoint != null) {
            endpoint.sendIfOpen(message);
        }
    }

    /** 推给某个真实登录用户的所有在线端（Web / App 同时在线时两边都收） */
    public static int sendToLoginName(String loginName, String message) {
        if (loginName == null || loginName.isEmpty()) return 0;
        int sent = 0;
        for (NoticeWebSocket endpoint : onlineUsers.values()) {
            if (loginName.equals(endpoint.loginName)) {
                endpoint.sendIfOpen(message);
                sent++;
            }
        }
        return sent;
    }

    /**
     * 按权限精准推送：只有满足 hasPermission(perm) 的连接能收到。
     * admin 天然有所有权限。
     *
     * @param perm            权限标识，如 "car:apply:list"
     * @param message         要推送的 JSON 消息
     * @param excludeLoginNames 可选：排除这些 loginName（比如申请人自己，即使他有审批权限也别弹窗）
     * @return 实际推送数量
     */
    public static int broadcastByPermission(String perm, String message, Collection<String> excludeLoginNames) {
        int sent = 0;
        int skippedExcluded = 0;
        int noLoginName = 0;
        int noPermission = 0;
        for (NoticeWebSocket endpoint : onlineUsers.values()) {
            if (excludeLoginNames != null && endpoint.loginName != null
                    && excludeLoginNames.contains(endpoint.loginName)) {
                skippedExcluded++;
                continue;
            }
            // 必须已登录（有 loginName）才能接收；无 loginName 说明未认证或解析失败
            if (endpoint.loginName == null || endpoint.loginName.isEmpty()) {
                noLoginName++;
                continue;
            }
            boolean allow = endpoint.admin
                    || (endpoint.permissions != null && (endpoint.permissions.contains(perm)
                            || endpoint.permissions.contains("*:*:*")));
            if (allow) {
                endpoint.sendIfOpen(message);
                sent++;
            } else {
                noPermission++;
            }
        }
        log.info("[实时推送] broadcastByPermission perm={}, online={}, sent={}, skippedExcluded={}, noLoginName={}, noPermission={}, exclude={}",
                perm, onlineUsers.size(), sent, skippedExcluded, noLoginName, noPermission, excludeLoginNames);
        return sent;
    }

    /**
     * 按自定义条件推送。
     */
    public static int broadcastWithFilter(Predicate<NoticeWebSocket> predicate, String message) {
        int sent = 0;
        for (NoticeWebSocket endpoint : onlineUsers.values()) {
            if (predicate.test(endpoint)) {
                endpoint.sendIfOpen(message);
                sent++;
            }
        }
        return sent;
    }

    /** 全局广播（慎用，仅用于系统公告等面向所有用户的场景；业务通知请用 broadcastByPermission） */
    public static void broadcast(String message) {
        int sent = 0;
        for (NoticeWebSocket endpoint : onlineUsers.values()) {
            endpoint.sendIfOpen(message);
            sent++;
        }
        log.debug("[实时推送] broadcast, online={}, sent={}", onlineUsers.size(), sent);
    }

    public static int getOnlineCount() {
        return onlineUsers.size();
    }

    /* ================ 给推送逻辑暴露的只读 getter（不需要 getter 的字段不暴露） ================ */
    public String getLoginName() { return loginName; }
    public Set<String> getPermissions() { return permissions; }
    public boolean isAdmin() { return admin; }
}
