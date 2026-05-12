package com.example.redis.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrderDTO implements Serializable {
    private Long id;
    private Long itemId;
    private Integer count;


    public static ItemOrderDTO fromEntity(ItemOrder entity) {
        return ItemOrderDTO.builder()
                .id(entity.getId())
                .itemId(entity.getItemId())
                .count(entity.getCount())
                .build();
    }
}
