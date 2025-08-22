// 2. Product.java - 상품 정보
package com.example.Meokgeoli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer price;

    private Integer stock = 0; // 재고

    private String unit = "개"; // 단위 (kg, 개, 묶음 등)

    // 할인 정보
    private Integer discountRate = 0; // 할인율 (%)
    private Integer discountPrice; // 할인된 가격

    private String imageUrl;

    // 상품이 속한 상점 (다대일 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // 생성자
    public Product() {}

    public Product(String name, Integer price, Store store) {
        this.name = name;
        this.price = price;
        this.store = store;
    }

    // 할인된 최종 가격 계산
    public Integer getFinalPrice() {
        if (discountPrice != null && discountPrice > 0) {
            return discountPrice;
        }
        if (discountRate > 0) {
            return (int) (price * (100 - discountRate) / 100.0);
        }
        return price;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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