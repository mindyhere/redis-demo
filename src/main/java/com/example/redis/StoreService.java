package com.example.redis;

import com.example.redis.domain.Store;
import com.example.redis.domain.StoreDTO;
import com.example.redis.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * 새로 만든 상점은 다음 검색에서 등장할 수 있게끔 기존의 전체 조회 캐시 삭제
     * 인지도가 높지 않을 가능성이 있으므로 생성시 추가 캐시 생성은 하지 않는다.
     */
    @CacheEvict(cacheNames = "storeAllCache", allEntries = true)
    public StoreDTO create(StoreDTO dto) {
        return StoreDTO.fromEntity(storeRepository.save(Store.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .build()));
    }

    /**
     * 조회된 상점은 일단 캐시에 둔다.
     * TTL은 줄이면서 TTI를 설정 -> 자주 조회되지 않는 상점들은 캐시에서 빠르게 제거 & 자주 조회되는 상점은 캐시에 유지
     */
    @Cacheable(cacheNames = "storeCache", key = "args[0]")
    public StoreDTO readOne(Long id) {
        return storeRepository.findById(id)
                .map(StoreDTO::fromEntity)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * 전체 조회는 일반적인 서비스에서 가장 많이 활용된다.
     */
    @Cacheable(cacheNames = "storeAllCache", key = "methodName")
    public List<StoreDTO> readAll() {
        return storeRepository.findAll()
                .stream()
                .map(StoreDTO::fromEntity)
                .toList();
    }

    /**
     * 상점이 갱신될 경우 해당 내용을 갱신해 주어야 한다.
     * 단일 캐시는 갱신, 전체 캐시는 제거
     */
    @CachePut(cacheNames = "storeCache", key = "#result.id")
    @CacheEvict(cacheNames = "storeAllCache", allEntries = true)
    public StoreDTO update(Long id, StoreDTO dto) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        store.setName(dto.getName());
        store.setCategory(dto.getCategory());
        return StoreDTO.fromEntity(storeRepository.save(store));
    }

    /**
     * 삭제될 경우 단일 캐시, 전체 캐시 전부 초기화.
     */
    @Caching(evict = {
            @CacheEvict(cacheNames = "storeCache", key = "args[0]"),
            @CacheEvict(cacheNames = "storeAllCache", allEntries = true)
    })
    public void delete(Long id) {
        storeRepository.deleteById(id);
    }
}
