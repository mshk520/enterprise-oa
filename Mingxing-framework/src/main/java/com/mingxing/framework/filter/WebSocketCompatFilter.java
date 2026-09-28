package com.mingxing.framework.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * WebSocket 老路径兼容过滤器：
 * 老前端缓存代码请求 /ws/{token}（JWT 直接在路径里），把 token 搬到 query，
 * 然后转发到 /ws?token=xxx，这样只需要一个 @ServerEndpoint("/ws") 即可处理全部连接。
 *
 * 处理规则：
 *   1) 上下文路径之后以 "/ws/" 开头，路径 "/ws/" 之后还有字符（不是空） → 这是老代码请求
 *   2) 拆出 "/ws/{token}" 中的 {token} 作为 query 参数拼回 ?token=xxx
 *   3) 修改 getRequestURI() / getQueryString() / getServletPath() 三个方法
 */
public class WebSocketCompatFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String ctx = req.getContextPath() == null ? "" : req.getContextPath();
        String uri = req.getRequestURI();
        String rel = uri.startsWith(ctx) ? uri.substring(ctx.length()) : uri;

        if (rel.startsWith("/ws/") && rel.length() > 4) {
            String rest = rel.substring(4);
            // 如果存在 query 参数，先拆出来
            String qs = req.getQueryString();
            String token;
            String appended = "";
            int slash = rest.indexOf('?');
            if (slash >= 0) {
                token = rest.substring(0, slash);
                appended = rest.substring(slash + 1);
            } else {
                token = rest;
            }
            // 确保不是第二个斜杠后面还有分段（虽然 JWT 里可能有 /）
            StringBuilder newQs = new StringBuilder();
            newQs.append("token=").append(urlEncode(token));
            if (appended != null && !appended.isEmpty()) {
                newQs.append('&').append(appended);
            }
            if (qs != null && !qs.isEmpty()) {
                newQs.append('&').append(qs);
            }
            String newUri = ctx + "/ws";
            chain.doFilter(new RewrittenRequest(req, newUri, "/ws", newQs.toString()), response);
            return;
        }

        chain.doFilter(request, response);
    }

    private static String urlEncode(String s) {
        // JWT 里有 / = + . _ - 等，必须做 URL 编码；不依赖 URLEncoder 避免中文路径处理
        StringBuilder sb = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')
                    || c == '-' || c == '_' || c == '.' || c == '~') {
                sb.append(c);
            } else {
                sb.append('%');
                String hex = Integer.toHexString(c);
                if (hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
        }
        return sb.toString();
    }

    private static class RewrittenRequest extends HttpServletRequestWrapper {
        private final String newRequestUri;
        private final String newServletPath;
        private final String newQueryString;
        private final Map<String, String[]> paramMap;

        RewrittenRequest(HttpServletRequest req, String newRequestUri, String newServletPath, String newQueryString) {
            super(req);
            this.newRequestUri = newRequestUri;
            this.newServletPath = newServletPath;
            this.newQueryString = newQueryString;
            this.paramMap = parseParams(req.getParameterMap(), newQueryString);
        }

        @Override public String getRequestURI() { return newRequestUri; }
        @Override public String getServletPath() { return newServletPath; }
        @Override public String getQueryString() { return newQueryString; }
        @Override public String getParameter(String name) {
            String[] v = paramMap.get(name);
            return (v == null || v.length == 0) ? null : v[0];
        }
        @Override public Map<String, String[]> getParameterMap() { return paramMap; }
        @Override public Enumeration<String> getParameterNames() {
            return Collections.enumeration(paramMap.keySet());
        }
        @Override public String[] getParameterValues(String name) {
            return paramMap.get(name);
        }
        @Override public StringBuffer getRequestURL() {
            StringBuffer sb = new StringBuffer();
            String scheme = super.getScheme();
            sb.append(scheme).append("://").append(super.getServerName());
            int port = super.getServerPort();
            if (!(("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443))) {
                sb.append(':').append(port);
            }
            sb.append(newRequestUri);
            return sb;
        }

        private static Map<String, String[]> parseParams(Map<String, String[]> original, String newQueryString) {
            Map<String, String[]> merged = new LinkedHashMap<>();
            if (original != null) {
                for (Map.Entry<String, String[]> e : original.entrySet()) {
                    merged.put(e.getKey(), e.getValue());
                }
            }
            if (newQueryString != null && !newQueryString.isEmpty()) {
                String[] pairs = newQueryString.split("&");
                Map<String, List<String>> tmp = new HashMap<>();
                for (String pair : pairs) {
                    if (pair.isEmpty()) continue;
                    int idx = pair.indexOf('=');
                    String k;
                    String v;
                    if (idx > 0) {
                        k = urlDecode(pair.substring(0, idx));
                        v = urlDecode(pair.substring(idx + 1));
                    } else {
                        k = urlDecode(pair);
                        v = "";
                    }
                    tmp.computeIfAbsent(k, x -> new java.util.ArrayList<>()).add(v);
                }
                for (Map.Entry<String, List<String>> e : tmp.entrySet()) {
                    merged.put(e.getKey(), e.getValue().toArray(new String[0]));
                }
            }
            return merged;
        }

        private static String urlDecode(String s) {
            if (s.indexOf('%') < 0) return s;
            StringBuilder sb = new StringBuilder(s.length());
            for (int i = 0; i < s.length(); ) {
                char c = s.charAt(i);
                if (c == '%' && i + 2 < s.length()) {
                    try {
                        sb.append((char) Integer.parseInt(s.substring(i + 1, i + 3), 16));
                        i += 3;
                        continue;
                    } catch (NumberFormatException ignored) {
                    }
                }
                sb.append(c);
                i++;
            }
            return sb.toString();
        }
    }
}
