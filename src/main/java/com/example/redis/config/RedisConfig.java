package com.example.redis.config;

import com.example.redis.domain.ItemDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, ItemDTO> itemRedisTemplate(
        RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, ItemDTO> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(RedisSerializer.string());
//        template.setValueSerializer(RedisSerializer.json());
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));

        return  template;
    }

    @Bean
    public RedisTemplate<String, Integer> articleTemplate(
        RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.string());
        return template;
    }

    /**
     * 기본 Java 기반 직렬화 ->  JSON을 비롯한 방식으로 직렬화
     */
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return RedisSerializer.json();
    }

    /**
     * 리더보드 실습
     */
    @Bean
    public RedisTemplate<String, ItemDTO> rankTemplate(
        RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, ItemDTO> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());

        return  template;
    }
}
