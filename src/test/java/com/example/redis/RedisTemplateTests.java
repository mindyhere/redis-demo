package com.example.redis;

import com.example.redis.domain.ItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTemplateTests {
    @Autowired
    private StringRedisTemplate redisTemplate; // <- Redis 의 자료형(String)을 의미하는 것 X. Java에서의 자료형

    @Test
    public void stringOpsTest() {
        // ValueOperations: 문자열 조작을 위한 클래스
        // RedisTemplate 에 설정된 타입을 바탕으로 Redis 문자열 조작
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        ops.set("simpleKey", "simpleValue1");
        System.out.println(ops.get("simpleKey"));

        // 집합을 조작하기 위한 클래스
        SetOperations<String, String> setOps = redisTemplate.opsForSet();
        setOps.add("hobbies", "game");
        setOps.add("hobbies", "coding", "coffee", "swimming");

        System.out.println("SCARD hobbies ##### " + setOps.size("hobbies"));

        redisTemplate.expire("hobbies", 10, TimeUnit.SECONDS);
        redisTemplate.delete("simpleKey");
    }

    @Autowired
    private RedisTemplate<String, ItemDTO> itemRedisTemplate;

    @Test
    public void itemRedisTemplateTest() {
        ValueOperations<String, ItemDTO> ops = itemRedisTemplate.opsForValue();

        ops.set("my:keyboard", ItemDTO.builder()
                .name("Mechanical Keyboard")
                .price(250000)
                .description("TOO Expensive")
                .build());

        System.out.println("MGET keyboard ##### "+ ops.get("my:keyboard"));

        ops.set("my:mouse", ItemDTO.builder()
                .name("Logitech Mouse")
                .price(100000)
                .description("TOO Expensive")
                .build());
        System.out.println("MGET mouse ##### " + ops.get("my:mouse"));
    }
}
