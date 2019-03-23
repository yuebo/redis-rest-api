package com.eappcat.redis.rest.web.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class RedisMessageListener {
    private final StringRedisTemplate redisTemplate;
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    public RedisMessageListener(StringRedisTemplate redisTemplate, RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisTemplate = redisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    private class DefaultMessageListener implements MessageListener{
        private SsePublishEvent event;

        DefaultMessageListener(SsePublishEvent event){
            this.event=event;
        }

        @Override
        public void onMessage(Message message, byte[] pattern) {
            try {
                String channel = (String)redisTemplate.getValueSerializer().deserialize(message.getChannel());
                String value = (String)redisTemplate.getValueSerializer().deserialize(message.getBody());
                Map<String,String> messageMap=new HashMap<>();
                messageMap.put("channel",channel);
                messageMap.put("value",value);
                log.info("receive message on topic {}, {}",channel,value);
                event.getSseEmitter().send(SseEmitter.event().id(UUID.randomUUID().toString()).data(value,MediaType.APPLICATION_JSON));
            } catch (Exception e) {
                event.getSseEmitter().complete();
            }
        }
    }

    @EventListener
    public void listen(SsePublishEvent emitterEvent){
        DefaultMessageListener messageListener=new DefaultMessageListener(emitterEvent);
        emitterEvent.getSseEmitter().onCompletion(()->{
            redisMessageListenerContainer.removeMessageListener(messageListener);
            log.info("topic remove {}", emitterEvent.getChannel());
        });
        redisMessageListenerContainer.addMessageListener(messageListener,new ChannelTopic(emitterEvent.getChannel()));
    }

}
