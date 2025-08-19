package com.example.Meokgeoli.controller;

import com.example.Meokgeoli.dto.StoreRequest;
import com.example.Meokgeoli.dto.StoreResponse;
import com.example.Meokgeoli.entity.Store;
import com.example.Meokgeoli.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*")
public class StoreController {

    private final StoreService service;

    public StoreController(StoreService service) {
        this.service = service;
    }

    @GetMapping
    public List<StoreResponse> list() {
        return service.list().stream().map(StoreResponse::from).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StoreResponse get(@PathVariable Long id) {
        return StoreResponse.from(service.get(id));
    }

    @PostMapping
    public ResponseEntity<StoreResponse> create(@RequestBody StoreRequest req) {
        Store saved = service.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(StoreResponse.from(saved));
    }

    @PutMapping("/{id}")
    public StoreResponse update(@PathVariable Long id, @RequestBody StoreRequest req) {
        return StoreResponse.from(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 보너스: 간단 검색 (Phase 2 범위)
    @GetMapping("/search")
    public List<StoreResponse> search(@RequestParam(required = false) String name,
                                      @RequestParam(required = false) String category) {
        List<Store> result;
        if (name != null && !name.isBlank()) {
            result = service.searchByName(name);
        } else if (category != null && !category.isBlank()) {
            result = service.findByCategory(category);
        } else {
            result = service.list();
        }
        return result.stream().map(StoreResponse::from).collect(java.util.stream.Collectors.toList());
    }
}

