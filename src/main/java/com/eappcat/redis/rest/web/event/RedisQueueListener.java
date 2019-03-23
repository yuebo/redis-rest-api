package com.eappcat.redis.rest.web.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis Block Queue Listener
 */
@Component
@Slf4j
public class RedisQueueListener {
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisQueueListener(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @EventListener
    @Async
    public void listen(SseQueueEvent event){
        event.getSseEmitter().onCompletion(()-> event.setRunning(false));
        while (event.isRunning()){
            String value;
            if(event.getType()==SseQueueEvent.TYPE_LEFT){
                value=redisTemplate.opsForList().leftPop(event.getKey(),0, TimeUnit.SECONDS);
            }else {
                value=redisTemplate.opsForList().rightPop(event.getKey(),0, TimeUnit.SECONDS);
            }
            try {
                SseEmitter.SseEventBuilder builder=SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .data(value,MediaType.APPLICATION_JSON);
                event.getSseEmitter().send(builder);
            } catch (Exception e) {
                log.info("stop sse {}",e.getMessage());
                event.getSseEmitter().complete();
                event.setRunning(false);
            }

        }
    }

}
