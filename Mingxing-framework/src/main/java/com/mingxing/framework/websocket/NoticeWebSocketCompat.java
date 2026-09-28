package com.mingxing.framework.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 向前兼容端点：老版本前端代码用 /ws/{token} 路径（JWT 直接在 path 里）。
 * 把所有回调直接委派给 NoticeWebSocket 的实例方法，保证两套路径的连接池、广播逻辑一致。
 */
@Component
@ServerEndpoint("/ws/{token}")
public class NoticeWebSocketCompat {

    private final NoticeWebSocket delegate = new NoticeWebSocket();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        delegate.onOpen(session, token);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        delegate.onMessage(message, session);
    }

    @OnClose
    public void onClose() {
        delegate.onClose();
    }

    @OnError
    public void onError(Session session, Throwable error) {
        delegate.onError(session, error);
    }
}
