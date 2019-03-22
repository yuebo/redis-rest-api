package com.eappcat.redis.rest.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis/value")
@ConditionalOnProperty(prefix = "spring.redis",value = "api",havingValue = "true",matchIfMissing = true)
public class RedisStringController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/get/{key}")
    public String value(@PathVariable(name = "key")String key){
        return redisTemplate.opsForValue().get(key);
    }
    @PostMapping("/set/{key}")
    public int value(@PathVariable(name = "key")String key, @RequestBody String value){
        redisTemplate.opsForValue().set(key,value);
        return 1;
    }
}
