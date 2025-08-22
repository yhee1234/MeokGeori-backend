package com.example.Meokgeoli.service;

import com.example.Meokgeoli.dto.StoreRequest;
import com.example.Meokgeoli.entity.Store;
import com.example.Meokgeoli.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StoreService {

    private final StoreRepository repository;
    private final RestTemplate restTemplate;

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    public StoreService(StoreRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    // DB에 저장된 모든 상점 조회 (기존 메소드)
    @Transactional(readOnly = true)
    public List<Store> list() {
        return repository.findAll();
    }

    /**
     * Haversine 공식을 사용하여 두 지점 간의 거리를 계산합니다.
     * @param lat1 지점 1의 위도
     * @param lon1 지점 1의 경도
     * @param lat2 지점 2의 위도
     * @param lon2 지점 2의 경도
     * @return 거리 (킬로미터 단위)
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // 지구의 반지름 (킬로미터)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * 사용자 위치 반경 내 상점 검색
     * @param userLat 사용자 위도
     * @param userLon 사용자 경도
     * @param radius 검색 반경 (km)
     * @return 반경 내 상점 리스트
     */
    @Transactional(readOnly = true)
    public List<Store> findStoresWithinRadius(double userLat, double userLon, double radius) {
        List<Store> allStores = repository.findAll();
        return allStores.stream()
                .filter(store -> calculateDistance(userLat, userLon, store.getLatitude(), store.getLongitude()) <= radius)
                .collect(Collectors.toList());
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