package com.example.Meokgeoli.repository;

import com.example.Meokgeoli.entity.Product;
import com.example.Meokgeoli.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // 상점별 상품 조회
    List<Product> findByStore(Store store);

    // 상점 ID로 상품 조회
    List<Product> findByStoreId(Long storeId);

    // 상품명으로 검색 (부분 일치, 대소문자 무시)
    List<Product> findByNameContainingIgnoreCase(String name);

    // 가격 범위로 검색
    List<Product> findByPriceBetween(Integer minPrice, Integer maxPrice);

    // 재고가 있는 상품만 조회
    List<Product> findByStockGreaterThan(Integer stock);

    // 특정 상점들의 상품 중에서 이름이 포함된 상품 검색
    @Query("SELECT p FROM Product p WHERE p.store IN :stores AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.stock > 0")
    List<Product> findByStoresAndNameContainingAndStockGreaterThanZero(@Param("stores") List<Store> stores, @Param("name") String name);

    // 특정 상점들의 모든 재고 있는 상품 조회
    @Query("SELECT p FROM Product p WHERE p.store IN :stores AND p.stock > 0")
    List<Product> findByStoresAndStockGreaterThanZero(@Param("stores") List<Store> stores);

    // 할인 상품 조회
    @Query("SELECT p FROM Product p WHERE p.discountRate > 0 OR p.discountPrice IS NOT NULL")
    List<Product> findDiscountedProducts();
}