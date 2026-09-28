package com.mingxing.framework.websocket;

import com.mingxing.common.event.WsNoticeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class WsNoticeEventListener {

    private static final Logger log = LoggerFactory.getLogger(WsNoticeEventListener.class);

    @EventListener
    public void onWsNotice(WsNoticeEvent event) {
        try {
            String msg = event.toJson();
            NoticeWebSocket.broadcast(msg);
        } catch (Exception e) {
            log.error("WebSocket事件广播失败: type={}, error={}", event.getType(), e.getMessage(), e);
        }
    }
}
