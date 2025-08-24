package com.example.Meokgeoli.service;

import com.example.Meokgeoli.entity.CartItem;
import com.example.Meokgeoli.entity.Product;
import com.example.Meokgeoli.entity.Store;
import com.example.Meokgeoli.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingService {

    private final ProductRepository productRepository;

    public ShoppingService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * 향상된 장바구니 최적화 알고리즘
     * @param stores 검색된 상점 리스트
     * @param budget 예산
     * @param desiredProducts 원하는 상품 이름 리스트
     * @return 최적화된 장바구니 아이템 리스트
     */
    public List<CartItem> optimizeCart(List<Store> stores, int budget, List<String> desiredProducts) {
        List<CartItem> cart = new ArrayList<>();
        int currentCost = 0;
        Set<String> addedProducts = new HashSet<>(); // 이미 추가된 상품 이름들

        // 1. 원하는 상품들을 우선순위별로 처리
        for (String desiredProductName : desiredProducts) {
            if (currentCost >= budget) break;

            // 2. 해당 상품을 파는 상점들에서 가장 저렴한 상품 찾기
            Product bestProduct = findCheapestProduct(stores, desiredProductName);

            if (bestProduct != null && !addedProducts.contains(bestProduct.getName())) {
                int productPrice = bestProduct.getFinalPrice();

                // 3. 예산 내에서 구매 가능한지 확인
                if (currentCost + productPrice <= budget) {
                    cart.add(new CartItem(bestProduct, 1));
                    currentCost += productPrice;
                    addedProducts.add(bestProduct.getName());
                }
            }
        }

        // 4. 남은 예산으로 추가 상품 구매 (가성비 좋은 상품들)
        if (currentCost < budget) {
            List<Product> remainingProducts = getAffordableProducts(stores, budget - currentCost, addedProducts);

            for (Product product : remainingProducts) {
                if (currentCost + product.getFinalPrice() <= budget) {
                    cart.add(new CartItem(product, 1));
                    currentCost += product.getFinalPrice();
                    addedProducts.add(product.getName());

                    if (currentCost >= budget * 0.95) break; // 예산의 95%까지만 사용
                }
            }
        }

        return cart;
    }

    /**
     * 특정 상품명에 대해 주변 상점들에서 가장 저렴한 상품을 찾습니다.
     */
    private Product findCheapestProduct(List<Store> stores, String productName) {
        return productRepository.findByStoresAndNameContainingAndStockGreaterThanZero(stores, productName)
                .stream()
                .min(Comparator.comparingInt(Product::getFinalPrice))
                .orElse(null);
    }

    /**
     * 남은 예산으로 구매 가능한 상품들을 가성비 순으로 정렬하여 반환합니다.
     */
    private List<Product> getAffordableProducts(List<Store> stores, int remainingBudget, Set<String> excludeProducts) {
        return productRepository.findByStoresAndStockGreaterThanZero(stores)
                .stream()
                .filter(product -> product.getFinalPrice() <= remainingBudget)
                .filter(product -> !excludeProducts.contains(product.getName()))
                .sorted(this::compareProductsByValue) // 가성비 비교
                .collect(Collectors.toList());
    }

    /**
     * 상품의 가성비를 비교합니다.
     * 할인율이 높거나, 가격이 저렴하거나, 필수품일수록 우선순위가 높습니다.
     */
    private int compareProductsByValue(Product p1, Product p2) {
        // 1. 할인율 비교 (높은 할인율 우선)
        int discount1 = p1.getDiscountRate() != null ? p1.getDiscountRate() : 0;
        int discount2 = p2.getDiscountRate() != null ? p2.getDiscountRate() : 0;

        if (discount1 != discount2) {
            return Integer.compare(discount2, discount1);
        }

        // 2. 필수품 여부 (육류, 채소, 쌀 등을 필수품으로 간주)
        int priority1 = getProductPriority(p1.getName());
        int priority2 = getProductPriority(p2.getName());

        if (priority1 != priority2) {
            return Integer.compare(priority2, priority1);
        }

        // 3. 가격 비교 (저렴한 것 우선)
        return Integer.compare(p1.getFinalPrice(), p2.getFinalPrice());
    }

    /**
     * 상품의 우선순위를 반환합니다.
     * 높을수록 중요한 상품입니다.
     */
    private int getProductPriority(String productName) {
        String name = productName.toLowerCase();

        // 주식류 (쌀, 빵 등)
        if (name.contains("쌀") || name.contains("빵") || name.contains("면")) return 10;

        // 단백질 (고기, 생선, 계란 등)
        if (name.contains("삼겹살") || name.contains("고기") || name.contains("생선") ||
                name.contains("계란") || name.contains("닭") || name.contains("소") ||
                name.contains("돼지") || name.contains("연어") || name.contains("고등어")) return 9;

        // 채소류
        if (name.contains("상추") || name.contains("양파") || name.contains("마늘") ||
                name.contains("당근") || name.contains("시금치") || name.contains("배추") ||
                name.contains("오이") || name.contains("토마토")) return 8;

        // 조미료
        if (name.contains("간장") || name.contains("기름") || name.contains("소금") ||
                name.contains("설탕")) return 7;

        // 기타
        return 5;
    }

    /**
     * 장바구니의 총 가격을 계산합니다.
     */
    public int calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToInt(CartItem::getTotalPrice)
                .sum();
    }

    /**
     * 예산 대비 장바구니 최적화 점수를 계산합니다.
     * @param cartItems 장바구니 아이템들
     * @param budget 예산
     * @param desiredProducts 원하는 상품 목록
     * @return 0~100 사이의 점수
     */
    public double calculateOptimizationScore(List<CartItem> cartItems, int budget, List<String> desiredProducts) {
        if (cartItems.isEmpty()) return 0.0;

        int totalPrice = calculateTotalPrice(cartItems);

        // 예산 활용률 (40%)
        double budgetUtilization = Math.min(totalPrice / (double) budget, 1.0) * 40;

        // 원하는 상품 만족도 (60%)
        long matchedProducts = cartItems.stream()
                .map(item -> item.getProduct().getName())
                .filter(name -> desiredProducts.stream()
                        .anyMatch(desired -> name.toLowerCase().contains(desired.toLowerCase())))
                .count();

        double productSatisfaction = (matchedProducts / (double) Math.max(desiredProducts.size(), 1)) * 60;

        return budgetUtilization + productSatisfaction;
    }
}