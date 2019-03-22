package com.eappcat.redis.rest.web.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SseQueueEvent {
    public static final int TYPE_RIGHT=1;
    public static final int TYPE_LEFT=0;
    private SseEmitter sseEmitter;
    private String key;
    private boolean running;
    private int type;
}
