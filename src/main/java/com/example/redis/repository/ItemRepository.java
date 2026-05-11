package com.example.redis.repository;


import com.example.redis.Item;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, String> {
}
