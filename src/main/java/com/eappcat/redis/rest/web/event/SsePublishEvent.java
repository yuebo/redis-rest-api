package com.eappcat.redis.rest.web.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SsePublishEvent {
    private SseEmitter sseEmitter;
    private String channel;
}
