package com.example.Meokgeoli.controller;

import com.example.Meokgeoli.dto.CartDtos;
import com.example.Meokgeoli.service.CartOptimizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    private final CartOptimizationService optimizer;
    public CartController(CartOptimizationService optimizer){ this.optimizer = optimizer; }

    @PostMapping("/optimize")
    public ResponseEntity<?> optimize(@RequestBody CartDtos.CartRequest req){
        var items = req.getItems().stream()
                .map(i -> new CartOptimizationService.CartItem(i.getBaseProductId(), i.getQuantity()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(optimizer.optimizeGreedy(items));
    }
}

