package com.mingxing.framework.config;

import com.mingxing.framework.websocket.NoticeWebSocket;
import com.mingxing.framework.websocket.NoticeWebSocketCompat;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.server.ServerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import jakarta.servlet.ServletContext;

/**
 * WebSocket 配置
 *
 * 注册两个端点：
 *   /ws           ← 新前端用，token 通过 query 参数 ?token=xxx 传入（JWT 含特殊字符也安全）
 *   /ws/{token}   ← 向前兼容老前端缓存代码，JWT 直接在路径里
 *
 * 如果只加 @ServerEndpoint 注解，在内嵌 Tomcat 中偶尔不会登记到 ServerContainer，
 * 因此同时通过 ServletContextInitializer 手动调用 container.addEndpoint 双保险。
 */
@Configuration
public class WebSocketConfig
{
    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    @Bean
    public ServerEndpointExporter serverEndpointExporter()
    {
        return new ServerEndpointExporter();
    }

    @Bean
    public ServletContextInitializer webSocketEndpointRegistrar()
    {
        return this::registerEndpoints;
    }

    private void registerEndpoints(ServletContext servletContext)
    {
        Object attr = servletContext.getAttribute(ServerContainer.class.getName());
        ServerContainer container = attr instanceof ServerContainer ? (ServerContainer) attr : null;
        if (container == null) {
            Object multi = servletContext.getAttribute("jakarta.websocket.server.ServerContainer");
            if (multi instanceof Iterable) {
                for (Object item : (Iterable<?>) multi) {
                    if (item instanceof ServerContainer) {
                        deployAll((ServerContainer) item);
                    }
                }
                return;
            }
            log.warn("[WebSocket] ServerContainer 未找到，依赖 ServerEndpointExporter 自动注册 /ws 和 /ws/{token}");
            return;
        }
        deployAll(container);
    }

    private void deployAll(ServerContainer container)
    {
        deploy(container, NoticeWebSocket.class, "/ws");
        deploy(container, NoticeWebSocketCompat.class, "/ws/{token}");
    }

    private void deploy(ServerContainer container, Class<?> endpointClass, String path)
    {
        try {
            container.addEndpoint(endpointClass);
            log.info("[WebSocket] 已注册端点: {} ({})", path, endpointClass.getSimpleName());
        } catch (DeploymentException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (msg.contains("already") || msg.contains("duplicate") || msg.contains("registered")) {
                log.info("[WebSocket] 端点 {} 已存在，跳过", path);
            } else {
                log.error("[WebSocket] 注册端点 {} 失败", path, e);
            }
        }
    }
}
