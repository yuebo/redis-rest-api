package com.eappcat.redis.rest.web;

import com.eappcat.redis.rest.web.event.SseQueueEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


@RestController
@RequestMapping("/redis/list")
@ConditionalOnProperty(prefix = "spring.redis",value = "api",havingValue = "true",matchIfMissing = true)
public class RedisListController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/leftPop/{key}")
    public String leftPop(@PathVariable("key")String key){
        return redisTemplate.opsForList().leftPop(key);
    }
    @PostMapping("/leftPush/{key}")
    public long leftPush(@PathVariable("key")String key,@RequestBody String value){
         return redisTemplate.opsForList().leftPush(key,value);
    }
    @GetMapping("/rightPop/{key}")
    public String rightPop(@PathVariable("key")String key){
        return redisTemplate.opsForList().rightPop(key);
    }
    @PostMapping("/rightPush/{key}")
    public long rightPush(@PathVariable("key")String key,@RequestBody String value){
        return redisTemplate.opsForList().rightPush(key,value);
    }

    @GetMapping("/range/{key}")
    public List<String> range(@PathVariable("key")String key, @RequestParam(name = "start") int start, @RequestParam(name = "end") int end){
        return redisTemplate.opsForList().range(key,start,end);
    }
    @GetMapping("/length/{key}")
    public long length(@PathVariable("key")String key){
        return redisTemplate.opsForList().size(key);
    }

    @GetMapping("/index/{key}")
    public String index(@PathVariable("key")String key,@RequestParam(name = "index") int index){
        return redisTemplate.opsForList().index(key,index);
    }

    @DeleteMapping("/remove/{key}")
    public long remove(@PathVariable("key")String key, @RequestParam(name = "count") long count,@RequestBody String value){
        return redisTemplate.opsForList().remove(key,count,value);
    }

    @PostMapping("/set/{key}")
    public int indexSet(@PathVariable("key")String key,@RequestParam(name = "index") long index,@RequestBody String value){
         redisTemplate.opsForList().set(key,index,value);
         return 1;
    }
    @GetMapping("/bLeftPop/{key}")
    public SseEmitter bLeftPop(@PathVariable(name = "key")String key){
        SseEmitter emitter=new SseEmitter();
        applicationContext.publishEvent(new SseQueueEvent(emitter,key,true,SseQueueEvent.TYPE_LEFT));
        return emitter;
    }
    @GetMapping("/bRightPop/{key}")
    public SseEmitter bRightPop(@PathVariable(name = "key")String key){
        SseEmitter emitter=new SseEmitter();
        applicationContext.publishEvent(new SseQueueEvent(emitter,key,true,SseQueueEvent.TYPE_RIGHT));
        return emitter;
    }


}
