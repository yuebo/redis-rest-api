package com.eappcat.redis.rest.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/redis/set")
@ConditionalOnProperty(prefix = "spring.redis",value = "api",havingValue = "true",matchIfMissing = true)
public class RedisSetController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostMapping("/add/{key}")
    public long add(@PathVariable(name ="key")String key,@RequestBody String value){
        return redisTemplate.opsForSet().add(key,value);
    }

    @DeleteMapping("/remove/{key}")
    public long remove(@PathVariable(name ="key")String key,@RequestBody String value){
        return redisTemplate.opsForSet().remove(key,value);
    }
    @GetMapping("/diff/{key}")
    public Set<String> diff(@PathVariable(name ="key")String key, @RequestParam(name = "other") String other){
        return redisTemplate.opsForSet().difference(key,other);
    }
    @GetMapping("/union/{key}")
    public Set<String> union(@PathVariable(name ="key")String key, @RequestParam(name = "other") String other){
        return redisTemplate.opsForSet().union(key,other);
    }
    @GetMapping("/intersect/{key}")
    public Set<String> intersect(@PathVariable(name ="key")String key, @RequestParam(name = "other") String other){
        return redisTemplate.opsForSet().intersect(key,other);
    }
    @GetMapping("/members/{key}")
    public Set<String> members(@PathVariable(name ="key")String key){
        return redisTemplate.opsForSet().members(key);
    }
}
