package com.example.Meokgeoli.service;

import com.example.Meokgeoli.dto.StoreRequest;
import com.example.Meokgeoli.entity.Store;
import com.example.Meokgeoli.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreService {

    private final StoreRepository repository;

    public StoreService(StoreRepository repository) {
        this.repository = repository;
    }

    public Store create(StoreRequest req) {
        Store s = new Store(req.getName(), req.getCategory(), req.getLatitude(), req.getLongitude());
        return repository.save(s);
    }

    @Transactional(readOnly = true)
    public Store get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Store not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Store> list() {
        return repository.findAll();
    }

    public Store update(Long id, StoreRequest req) {
        Store s = get(id);
        s.setName(req.getName());
        s.setCategory(req.getCategory());
        s.setLatitude(req.getLatitude());
        s.setLongitude(req.getLongitude());
        return s; // JPA dirty checking으로 자동 업데이트
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Store> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Store> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }
}

