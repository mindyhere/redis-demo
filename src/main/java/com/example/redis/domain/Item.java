package com.example.redis.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private Integer price;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<ItemOrder> orders = new ArrayList<>();
}


//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//// Entity 대신 RedisHash
////@RedisHash("item")
//public class Item implements Serializable {
//    @Id
//    // Id String -> UUID 가 자동으로 배정된다.
//    private String id;
////    private Long id;
//    private String name;
//    private String description;
//    private Integer price;
//
//}
