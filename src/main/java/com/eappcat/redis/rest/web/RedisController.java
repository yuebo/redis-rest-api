package com.eappcat.redis.rest.web;

import com.eappcat.redis.rest.web.event.SsePublishEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redis")
@ConditionalOnProperty(prefix = "spring.redis",value = "api",havingValue = "true",matchIfMissing = true)
public class RedisController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ApplicationContext applicationContext;


    @GetMapping("/keys")
    public Set<String> keys(@RequestParam(required = false,name = "key",defaultValue = "*")String key){
        return redisTemplate.keys(key);
    }
    @GetMapping("/types")
    public List<Map<String,Object>> types(@RequestParam(required = false,name = "key",defaultValue = "*")String key){
        List<Map<String,Object>> list=new ArrayList<>();
        redisTemplate.keys(key).stream().forEach(val ->{
            HashMap<String,Object> map=new HashMap<>();
            map.put("key",val);
            map.put("type",redisTemplate.type(val));
            map.put("expire",redisTemplate.getExpire(val));
            list.add(map);
        });
        return list;
    }

    @GetMapping("/type/{key}")
    public DataType type(@PathVariable(name = "key")String key){
        return redisTemplate.type(key);
    }

    @PostMapping("/expire/{key}")
    public int expire(@PathVariable(name = "key")String key,@RequestBody Long time){
         boolean result=redisTemplate.expire(key,time,TimeUnit.MICROSECONDS);
         return result?1:0;
    }
    @GetMapping("/expireGet/{key}")
    public long expireGet(@PathVariable(name = "key")String key){
        return redisTemplate.getExpire(key,TimeUnit.MICROSECONDS);
    }

    @PostMapping("/pub/{channel}")
    public int publish(@PathVariable(name = "channel")String channel,@RequestBody String value){
        redisTemplate.convertAndSend(channel,value);
        return 1;
    }

    @GetMapping(value="/subscribe/{key}")
    public SseEmitter subscribe(@PathVariable(name = "key")String key) {
        final SseEmitter emitter = new SseEmitter();
        applicationContext.publishEvent(new SsePublishEvent(emitter,key));
        return emitter;
    }

}
