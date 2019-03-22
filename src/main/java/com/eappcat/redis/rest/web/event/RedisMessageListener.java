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

@Component
@Slf4j
public class RedisMessageListener {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    public class DefaultMessageListener implements MessageListener{
        private SseEmitter emitter;

        public DefaultMessageListener(SseEmitter emitter){
            this.emitter=emitter;
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
                emitter.send(messageMap, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                emitter.complete();
            }
        }
    }

    @EventListener
    public void listen(SsePublishEvent emitterEvent){
        DefaultMessageListener messageListener=new DefaultMessageListener(emitterEvent.getSseEmitter());
        emitterEvent.getSseEmitter().onCompletion(()->{
            redisMessageListenerContainer.removeMessageListener(messageListener);
            log.info("topic remove {}", emitterEvent.getChannel());
        });
        redisMessageListenerContainer.addMessageListener(messageListener,new ChannelTopic(emitterEvent.getChannel()));
    }

}
