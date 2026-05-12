package com.example.redis.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private Integer price;

    public static ItemDTO fromEntity(Item entity) {
        return ItemDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .price(entity.getPrice())
            .build();
    }
}

