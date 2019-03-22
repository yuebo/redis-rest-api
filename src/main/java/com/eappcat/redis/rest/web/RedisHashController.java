package com.eappcat.redis.rest.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/redis/hash")
@ConditionalOnProperty(prefix = "spring.redis",value = "api",havingValue = "true",matchIfMissing = true)
public class RedisHashController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @GetMapping("/keys/{key}")
    public Set<String> hashKeys(@PathVariable(name = "key")String key){
        return redisTemplate.<String,String>opsForHash().keys(key);
    }

    @GetMapping("/get/{key}")
    public String hashGet(@PathVariable(name = "key")String key,@RequestParam(name = "key")String hashKey){
        return redisTemplate.<String,String>opsForHash().get(key,hashKey);
    }
    @GetMapping("/multiGet/{key}")
    public List<String> hashMultiGet(@PathVariable(name = "key")String key, @RequestParam(name = "keys")List<String> hashKey){
        return redisTemplate.<String,String>opsForHash().multiGet(key,hashKey);
    }

    @PostMapping("/put/{key}")
    public int hashPut(@PathVariable(name = "key")String key,@RequestParam(name = "key")String hashKey,@RequestBody String value){
        redisTemplate.<String,String>opsForHash().put(key,hashKey,value);
        return 1;
    }

    @GetMapping("/entries/{key}")
    public Map<String,String> hashEntries(@PathVariable(name = "key")String key){
        return redisTemplate.<String,String>opsForHash().entries(key);
    }
    @GetMapping("/values/{key}")
    public List<String> hashValues(@PathVariable(name = "key")String key){
        return redisTemplate.<String,String>opsForHash().values(key);
    }

    @DeleteMapping("/delete/{key}")
    public int hashDelete(@PathVariable(name = "key")String key,@RequestParam(name = "key")String hashKey){
        redisTemplate.opsForHash().delete(key,hashKey);
        return 1;
    }
}
