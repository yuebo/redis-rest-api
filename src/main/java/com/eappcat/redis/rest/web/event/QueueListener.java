package com.eappcat.redis.rest.web.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class QueueListener {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @EventListener
    @Async
    public void listen(SseQueueEvent event){
        event.getSseEmitter().onCompletion(()->{
            event.setRunning(false);
        });
        while (event.isRunning()){
            String value;
            if(event.getType()==SseQueueEvent.TYPE_LEFT){
                value=redisTemplate.opsForList().leftPop(event.getKey(),0, TimeUnit.SECONDS);
            }else {
                value=redisTemplate.opsForList().rightPop(event.getKey(),0, TimeUnit.SECONDS);
            }
            try {
                event.getSseEmitter().send(value, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                log.info("stop sse {}",e.getMessage());
                event.getSseEmitter().complete();
                event.setRunning(false);
            }

        }
    }

}
