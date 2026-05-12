package com.example.redis.domain;


import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO implements Serializable {
    private Long id;
    private String name;
    private String category;

    public static StoreDTO fromEntity(Store entity) {
        return StoreDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .category(entity.getCategory())
                .build();
    }
}
