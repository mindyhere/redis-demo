package com.example.redis;

import com.example.redis.domain.ItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping("{id}/purchase")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void purchase(
            @PathVariable("id")
            Long id
    ) {
        itemService.purchase(id);
    }

    @GetMapping("/ranks")
    public List<ItemDTO> getRanks() {
        return itemService.getMostSold();
    }

    @PostMapping
    public ItemDTO create(
        @RequestBody
        ItemDTO itemDto
    ) {
        return itemService.create(itemDto);
    }

    @GetMapping
    public List<ItemDTO> readAll() {
        return itemService.readAll();
    }

    @GetMapping("{id}")
    public ItemDTO readOne(
        @PathVariable("id")
        Long id
    ) {
        return itemService.readOne(id);
    }

    @PutMapping("{id}")
    public ItemDTO update(
        @PathVariable("id")
        Long id,
        @RequestBody
        ItemDTO dto
    ) {
        return itemService.update(id, dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
        @PathVariable
        Long id
    ) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public Page<ItemDTO> search(
        @RequestParam(name = "q") String query, Pageable pageable
    ) {
        return itemService.searchByName(query, pageable);
    }
}
