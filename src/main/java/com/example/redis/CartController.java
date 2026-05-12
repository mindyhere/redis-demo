package com.example.redis;

import com.example.redis.domain.CartDTO;
import com.example.redis.domain.CartItemDTO;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("cart")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public CartDTO modifyCart(
            @RequestBody
            CartItemDTO itemDto,
            HttpSession session
    ) {
        cartService.modifyCart(session.getId(), itemDto);
        return cartService.getCart(session.getId());
    }

    @GetMapping
    public CartDTO getCart(
            HttpSession session
    ) {
        log.info(session.getId());
        return cartService.getCart(session.getId());
    }
}
