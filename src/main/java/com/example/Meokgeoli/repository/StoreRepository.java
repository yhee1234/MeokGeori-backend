package com.example.Meokgeoli.repository;

import com.example.Meokgeoli.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // 카테고리로 조회
    List<Store> findByCategory(String category);

    // 이름 부분 검색(대소문자 무시)
    List<Store> findByNameContainingIgnoreCase(String keyword);

    // 페이징 예시 (필요할 때 사용)
    Page<Store> findByCategory(String category, Pageable pageable);

    // 중복 체크 예시
    boolean existsByNameAndLatitudeAndLongitude(String name, double latitude, double longitude);
}

