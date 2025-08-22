// src/main/java/com/example/Meokgeoli/entity/Product.java
package com.example.Meokgeoli.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_products_base", columnList = "baseProductId"),
                @Index(name = "idx_products_store", columnList = "store_id"),
                @Index(name = "idx_products_name", columnList = "name")
        }
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 상점 간 같은 규격 상품을 묶는 공통 식별자(예: '우유 1L'이면 모든 상점이 같은 값) */
    @Column(nullable = false)
    private Long baseProductId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer price;            // 원 단위(정수)

    @Column(nullable = false)
    private Integer stock = 0;        // 재고

    private String unit = "개";       // 단위 (kg, 개, 묶음 등)

    // 할인 정보
    private Integer discountRate = 0; // 할인율 (%)
    private Integer discountPrice;    // 할인된 가격(있으면 우선)

    private String imageUrl;

    /** 상품이 속한 상점 (다대일) — FK는 필수 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    @JsonIgnore // 엔티티를 직접 반환할 때 순환직렬화 방지(응답 DTO 쓰면 제거 가능)
    private Store store;

    // --- 생성자 ---
    protected Product() { }

    public Product(Long baseProductId, String name, Integer price, Store store) {
        this.baseProductId = baseProductId;
        this.name = name;
        this.price = price;
        this.store = store;
    }

    // 기존 생성자도 유지하고 싶다면 오버로드로 둡니다.
    public Product(String name, Integer price, Store store) {
        this(null, name, price, store);
    }

    // --- 비즈니스 로직 ---
    /** 최종가(할인가 우선 → 할인율 → 정가). price가 null이면 null 반환 */
    public Integer getFinalPrice() {
        if (price == null) return null;
        if (discountPrice != null && discountPrice > 0) return discountPrice;
        if (discountRate != null && discountRate > 0) {
            return (int) Math.round(price * (100 - discountRate) / 100.0);
        }
        return price;
    }

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBaseProductId() { return baseProductId; }
    public void setBaseProductId(Long baseProductId) { this.baseProductId = baseProductId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Integer getDiscountRate() { return discountRate; }
    public void setDiscountRate(Integer discountRate) { this.discountRate = discountRate; }

    public Integer getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(Integer discountPrice) { this.discountPrice = discountPrice; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Store getStore() { return store; }
    public void setStore(Store store) { this.store = store; }
}
