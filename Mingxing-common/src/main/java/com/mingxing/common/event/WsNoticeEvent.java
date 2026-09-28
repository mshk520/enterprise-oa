package com.mingxing.common.event;

import java.util.HashMap;
import java.util.Map;

public class WsNoticeEvent {

    private String type;
    private Map<String, Object> payload;

    public WsNoticeEvent(String type, Map<String, Object> payload) {
        this.type = type;
        this.payload = payload != null ? payload : new HashMap<>();
        this.payload.put("type", type);
        this.payload.put("time", System.currentTimeMillis());
    }

    public String getType() {
        return type;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public String toJson() {
        return com.alibaba.fastjson2.JSON.toJSONString(payload);
    }
}
