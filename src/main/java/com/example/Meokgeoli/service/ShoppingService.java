package com.example.Meokgeoli.service;

import com.example.Meokgeoli.entity.CartItem;
import com.example.Meokgeoli.entity.Product;
import com.example.Meokgeoli.entity.Store;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingService {

    /**
     * 그리디 알고리즘을 사용한 장바구니 최적화
     * @param stores 검색된 상점 리스트
     * @param budget 예산
     * @param desiredProducts 원하는 상품 이름 리스트
     * @return 최적화된 장바구니 아이템 리스트
     */
    public List<CartItem> optimizeCart(List<Store> stores, int budget, List<String> desiredProducts) {
        List<CartItem> cart = new ArrayList<>();
        int currentCost = 0;

        // 1. 모든 상점의 상품들 중, AI가 추천한 상품 목록에 포함된 것만 후보로 추린다.
        //    (실제 앱에서는 DB에 상품 정보가 있어야 이 로직이 의미가 있습니다.)
        List<Product> availableProducts = stores.stream()
                .flatMap(store -> {
                    // 임시로 각 상점이 모든 원하는 상품을 판다고 가정합니다.
                    // 실제로는 store.getProducts()를 사용해야 합니다.
                    return desiredProducts.stream().map(productName -> {
                        // 가격은 임시로 5000원으로 설정합니다.
                        // 실제로는 DB에 저장된 상품 가격을 사용해야 합니다.
                        Product p = new Product(productName, 5000, store);
                        p.setImageUrl("http://example.com/" + productName + ".jpg"); // 임시 이미지 URL
                        return p;
                    });
                })
                .collect(Collectors.toList());

        // 2. 가격이 저렴한 순서(가성비 순)로 정렬한다.
        availableProducts.sort(Comparator.comparingInt(Product::getFinalPrice));

        // 3. 예산 내에서, 아직 장바구니에 담지 않은 종류의 상품을 하나씩 담는다.
        for (Product product : availableProducts) {
            if (currentCost + product.getFinalPrice() <= budget) {
                // 이미 담은 상품인지 확인 (다른 가게의 같은 상품)
                boolean alreadyInCart = cart.stream()
                        .anyMatch(item -> item.getProduct().getName().equals(product.getName()));

                if (!alreadyInCart) {
                    cart.add(new CartItem(product, 1)); // 수량은 1개로 가정
                    currentCost += product.getFinalPrice();
                }
            }
        }
        return cart;
    }
}