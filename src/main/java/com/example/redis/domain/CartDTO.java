package com.example.redis.domain;

import lombok.*;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
    private Set<CartItemDTO> items;
    private Date expireAt;

    public static CartDTO fromHashPairs(
            Map<String, Integer> entries,
            Date expireAt
    ) {
        return CartDTO.builder()
                .items(entries.entrySet().stream()
                        .map(entry -> CartItemDTO.builder()
                                .item(entry.getKey())
                                .count(entry.getValue())
                                .build())
                        .collect(Collectors.toUnmodifiableSet()))
                .expireAt(expireAt)
                .build();
    }
}
