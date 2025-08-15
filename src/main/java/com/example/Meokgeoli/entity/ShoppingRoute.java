// 5. ShoppingRoute.java - 장보기 경로
package com.example.Meokgeoli.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_routes")
public class ShoppingRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String routeName; // 예: "삼겹살 파티 장보기"

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "route_id")
    private List<CartItem> cartItems = new ArrayList<>();

    // 방문할 상점들 (순서대로)
    @ManyToMany
    @JoinTable(
            name = "route_stores",
            joinColumns = @JoinColumn(name = "route_id"),
            inverseJoinColumns = @JoinColumn(name = "store_id")
    )
    @OrderColumn(name = "visit_order")
    private List<Store> visitStores = new ArrayList<>();

    private Integer totalBudget;
    private Integer estimatedTotalPrice;
    private Double estimatedDistance; // 총 이동거리 (km)
    private Integer estimatedTimeMinutes; // 예상 소요시간 (분)

    private LocalDateTime createdAt;

    // 생성자
    public ShoppingRoute() {
        this.createdAt = LocalDateTime.now();
    }

    public ShoppingRoute(User user, String routeName, Integer totalBudget) {
        this();
        this.user = user;
        this.routeName = routeName;
        this.totalBudget = totalBudget;
    }

    // 총 가격 계산
    public Integer calculateTotalPrice() {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public List<CartItem> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItem> cartItems) { this.cartItems = cartItems; }

    public List<Store> getVisitStores() { return visitStores; }
    public void setVisitStores(List<Store> visitStores) { this.visitStores = visitStores; }

    public Integer getTotalBudget() { return totalBudget; }
    public void setTotalBudget(Integer totalBudget) { this.totalBudget = totalBudget; }

    public Integer getEstimatedTotalPrice() { return estimatedTotalPrice; }
    public void setEstimatedTotalPrice(Integer estimatedTotalPrice) { this.estimatedTotalPrice = estimatedTotalPrice; }

    public Double getEstimatedDistance() { return estimatedDistance; }
    public void setEstimatedDistance(Double estimatedDistance) { this.estimatedDistance = estimatedDistance; }

    public Integer getEstimatedTimeMinutes() { return estimatedTimeMinutes; }
    public void setEstimatedTimeMinutes(Integer estimatedTimeMinutes) { this.estimatedTimeMinutes = estimatedTimeMinutes; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}