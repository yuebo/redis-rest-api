package com.eappcat.redis.rest;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(JUnit4.class)
//@SpringBootTest
@Slf4j
public class RedisRestApplicationTests {

    @Test
    public void sseClient() throws InterruptedException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://localhost:8080/redis/list/bLeftPop/test")
                .build();
        EventSource.Factory factory = EventSources.createFactory(client);
        EventSource eventSource = factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info("{}:{}",id,data);
            }
        });

        Thread.sleep(10000);

    }

}
