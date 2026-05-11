package com.example.redis;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// Entity 대신 RedisHash
@RedisHash("item")
public class Item implements Serializable {
    @Id
    // Id String -> UUID 가 자동으로 배정된다.
    private String id;
//    private Long id;
    private String name;
    private String description;
    private Integer price;

}
