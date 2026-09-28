package com.mingxing.common.utils.spring;

import com.mingxing.common.event.WsNoticeEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WsEventPublisher {

    private static ApplicationEventPublisher publisher;

    @Autowired
    public void setPublisher(ApplicationEventPublisher publisher) {
        WsEventPublisher.publisher = publisher;
    }

    public static void publish(String type, Map<String, Object> payload) {
        if (publisher != null) {
            publisher.publishEvent(new WsNoticeEvent(type, payload));
        }
    }
}
