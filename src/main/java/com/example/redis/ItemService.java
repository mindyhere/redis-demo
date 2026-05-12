package com.example.redis;

import com.example.redis.domain.Item;
import com.example.redis.domain.ItemDTO;
import com.example.redis.domain.ItemOrder;
import com.example.redis.repository.ItemRepository;
import com.example.redis.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ZSetOperations<String, ItemDTO> rankOps;

    public ItemService(
        ItemRepository itemRepository,
        OrderRepository orderRepository,
        RedisTemplate<String, ItemDTO> rankTemplate
    ) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.rankOps = rankTemplate.opsForZSet();
    }

    public void purchase(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        orderRepository.save(ItemOrder.builder()
                .item(item)
                .count(1)
                .build());

        rankOps.incrementScore("soldRanks", ItemDTO.fromEntity(item), 1);
    }

    public List<ItemDTO> getMostSold() {
        Set<ItemDTO> ranks = rankOps.reverseRange("soldRanks", 0, 9);
        if (ranks == null) return Collections.emptyList();
        return ranks.stream().toList();
    }

    @CachePut(cacheNames = "itemCache", key = "#result.id")
    public ItemDTO create(ItemDTO dto) {
        return ItemDTO.fromEntity(itemRepository.save(Item.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .price(dto.getPrice())
            .build()));
    }

    @Cacheable(cacheNames = "itemAllCache", key = "methodName")
    public List<ItemDTO> readAll() {
        return itemRepository.findAll()
            .stream()
            .map(ItemDTO::fromEntity)
            .toList();
    }

    /**
     * @Cacheable -> 이 메서드의 결과는 캐싱이 가능하다
     * cacheNames: 이 메서드로 인해서 만들어질 캐시를 지징하는 이름
     * key: 캐시 데이터를 구분하기 위해 활용하는 값
     */
    @Cacheable(cacheNames = "itemCache", key = "args[0]")
    public ItemDTO readOne(Long id) {
        log.info("Read One: {}", id);
        return itemRepository.findById(id)
            .map(ItemDTO::fromEntity)
            .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @CachePut(cacheNames = "itemCache", key = "args[0]")
    @CacheEvict(cacheNames = "itemAllCache", allEntries = true)
    public ItemDTO update(Long id, ItemDTO dto) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        return ItemDTO.fromEntity(itemRepository.save(item));
    }

    @Caching(evict = {
        @CacheEvict(cacheNames = "itemCache", key = "args[0]"),
        @CacheEvict(cacheNames = "itemAllCache", allEntries = true)
    })
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Cacheable(
        cacheNames = "itemSearchCache",
        key = "{args[0], args[1].pageNumber, args[1].pageSize}"
    )
    public Page<ItemDTO> searchByName(String query, Pageable pageable) {
        return itemRepository.findAllByNameContains(query, pageable)
                .map(ItemDTO::fromEntity);
    }
}
