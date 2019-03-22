Redis Rest
------------
用于Redis操作的Web API。

## 基本操作

### 列出所有的Keys
```
GET /redis/keys?key={key}
```
参数：
* key: Redis的key

返回值：
```
["key1","key2","key3"]
```

### 根据key查询类型
```
GET /redis/type/{key}
```
参数：
* key: Redis的key

返回值：
```
"HASH"
```

### 列出所有的Keys的类型和过期时间
```
GET /redis/types?key={key}
```
参数：
* key: Redis的key

返回值：
```
[
    {
        "key":"key1"
        "type":"HASH",
        "expire": -1
    }
]
```

### 设置过期时间
```
POST /redis/expire/{key}
```
参数：
* key: Redis的key

Body：
```
1000000
```

返回值：
```
1
```

### 获取过期时间
```
GET /redis/expireGet/{key}
```
参数：
* key: Redis的key

返回值：
```
1000000
```

### 发送事件
```
POST /redis/pub/{channel}
```
参数：
* channel: Redis事件的channel

Body：
```
"aaaa"
```

返回值：
```
1
```


### 监听事件
监听事件是通过Server Send Events技术实现的
```
GET /redis/subscribe/{channel}
```
参数：
* channel: Redis事件的channel

```javascript
var source = new EventSource('/redis/subscribe/test');
source.addEventListener("message",function(message){
    console.log(message);
});

```

## Hash

### 列出所有的HashKeys
```
GET /redis/hash/keys/{key}
```
参数：
* key: Redis的key

返回值：
```
["key1","key2","key3"]
```

### 根据hash值取value
```
GET /redis/hash/get/{key}?key={hashKey}
```
参数：
* key: Redis的key
* hashKey: Redis的Hash里面的名称

返回值：
```
"value"
```

### 根据hash值取value
```
GET /redis/hash/multiGet/{key}?key={hashKey1}&key={hashKeyN}
```
参数：
* key: Redis的key
* hashKeyN: Redis的Hash里面的名称

返回值：
```
"value"
```


### 设置Hash的值
```
POST /redis/hash/put/{key}?key={hashKey}
```

参数：
* key: Redis的key
* hashKey: Redis的Hash里面的名称

Body：
```
"value"
```

返回值：
```
1
```

### 列出Hash里所有的项
```
GET /redis/hash/entries/{key}
```
参数：
* key: Redis的key

返回值：
```
{
    "key1":"value1",
    "key2":"value2"
}
```

### 列出Hash里面所有的values
```
GET /redis/hash/values/{key}
```
参数：
* key: Redis的key


返回值：
```
["value1","value2"]
```


### 删除Hash里面的项
```
DELETE /redis/hash/delete/{key}
```
参数：
* key: Redis的key


返回值：
```
1
```

## String


### 设置值
```
POST /redis/value/set/{key}
```
参数：
* key: Redis的key

Body：
```
"value"
```

返回值：
```
1
```


### 获取值
```
GET /redis/value/get/{key}
```
参数：
* key: Redis的key

返回值：
```
"value"
```

## Set

### 添加值
```
POST /redis/set/add/{key}
```
参数：
* key: Redis的key

Body：
```
"value"
```

返回值：
```
1
```

### 删除值
```
DELETE /redis/set/remove/{key}
```
参数：
* key: Redis的key

返回值：
```
1
```

### 获取不同
```
GET /redis/set/diff/{key}?other={other}
```
参数：
* key: Redis的key
* other: 另外一个集合的key

返回值：
```
["aaa","bbb"]
```

### 交集
```
GET /redis/set/intersect/{key}?other={other}
```
参数：
* key: Redis的key
* other: 另外一个集合的key

返回值：
```
["aaa","bbb"]
```


### 并集
```
GET /redis/set/union/{key}?other={other}
```
参数：
* key: Redis的key
* other: 另外一个集合的key

返回值：
```
["aaa","bbb"]
```

### 获取集合里的全部元素
```
GET /redis/set/members/{key}
```
参数：
* key: Redis的key

返回值：
```
["aaa","bbb"]
```

## List
### left pop
```
GET /redis/list/leftPop/{key}
```
参数：
* key: Redis的key


返回值：
```
"value"
```

### left push
```
POST /redis/list/leftPush/{key}
```
参数：
* key: Redis的key

Body：
```
"value"
```

返回值：
```
1
```

### right pop
```
GET /redis/list/rightPop/{key}
```
参数：
* key: Redis的key


返回值：
```
"value"
```

### right push
```
POST /redis/list/rightPush/{key}
```
参数：
* key: Redis的key

Body：
```
"value"
```

返回值：
```
1
```


### range
```
GET /redis/list/range/{key}?start={start}&end={end}
```
参数：
* key: Redis的key
* start: 起始位置
* end: 结束位置



返回值：
```
["value1","value2"]
```

### index
```
GET /redis/list/index/{key}?index={index}
```
参数：
* key: Redis的key
* index: 元素位置

返回值：
```
"value1"
```

### length
```
GET /redis/list/length/{key}
```
参数：
* key: Redis的key

返回值：
```
100
```


### index
```
DELETE /redis/list/remove/{key}?count={count}&value={value}
```
参数：
* key: Redis的key
* count: 移除元素个数
* value: 元素值

返回值：
```
"value1"
```

### 根据index设置值
```
POST /redis/list/set/{key}?index={index}
```
参数：
* key: Redis的key
* index: 元素位置

Body：
```
"value"
```


返回值：
```
"value1"
```