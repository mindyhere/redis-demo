package com.example.redis.domain;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
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

//public class ItemDTO implements Serializable {
//    private String name;
//    private String description;
//    private Integer price;
//
//}
