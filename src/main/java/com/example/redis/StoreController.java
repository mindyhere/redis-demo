package com.example.redis;

import com.example.redis.domain.StoreDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stores")
public class StoreController {
    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }

    @PostMapping
    public StoreDTO store(
            @RequestBody StoreDTO dto
    ) {
        return service.create(dto);
    }

    @GetMapping
    public List<StoreDTO> readAll() {
        return service.readAll();
    }

    @GetMapping("{id}")
    public StoreDTO read(
            @PathVariable("id")
            Long id
    ) {
        return service.readOne(id);
    }

    @PutMapping("{id}")
    public StoreDTO update(
            @PathVariable("id")
            Long id,
            @RequestBody
            StoreDTO dto
    ) {
        return service.update(id, dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable("id")
            Long id
    ) {
        service.delete(id);
    }
}
