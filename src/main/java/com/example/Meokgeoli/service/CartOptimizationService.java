package com.example.Meokgeoli.service;

import com.example.Meokgeoli.entity.Product;
import com.example.Meokgeoli.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CartOptimizationService {

    public record CartItem(Long baseProductId, int qty) {}
    public record Assignment(Long baseProductId, Long storeId, String storeName,
                             String productName, int unitPrice, int qty, int lineTotal) {}
    public record StoreCost(Long storeId, String storeName,
                            int itemsTotal, int deliveryFee, double distanceKm, int storeTotal) {}
    public record OptimizationResult(List<Assignment> assignments,
                                     List<StoreCost> stores, int grandTotal) {}

    private final ProductRepository productRepo;
    public CartOptimizationService(ProductRepository productRepo){ this.productRepo = productRepo; }

    public OptimizationResult optimizeGreedy(List<CartItem> items) {
        // 1) 후보 한 번에 조회
        var baseIds = items.stream().map(CartItem::baseProductId).distinct().toList();
        var candidates = productRepo.findAllCandidates(baseIds);

        // 2) 품목별 최저가 선택
        Map<Long, Assignment> chosen = new LinkedHashMap<>();
        for (CartItem ci : items) {
            Product best = candidates.stream()
                    .filter(p -> p.getBaseProductId().equals(ci.baseProductId()))
                    .min(Comparator.comparingInt(Product::getPrice))
                    .orElseThrow(() -> new IllegalArgumentException("후보 없음: " + ci.baseProductId()));

            int line = best.getPrice() * ci.qty();
            chosen.put(ci.baseProductId(), new Assignment(
                    ci.baseProductId(),
                    best.getStore().getId(),
                    best.getStore().getName(),
                    best.getName(),
                    best.getPrice(),
                    ci.qty(),
                    line
            ));
        }

        // 3) 상점별 합계
        Map<Long, StoreCost> perStore = new LinkedHashMap<>();
        for (Assignment a : chosen.values()) {
            var prev = perStore.get(a.storeId());
            int newItemsTotal = (prev == null ? 0 : prev.itemsTotal()) + a.lineTotal();
            perStore.put(a.storeId(), new StoreCost(a.storeId(), a.storeName(), newItemsTotal, 0, 0.0, newItemsTotal));
        }

        int grand = perStore.values().stream().mapToInt(StoreCost::storeTotal).sum();
        return new OptimizationResult(new ArrayList<>(chosen.values()), new ArrayList<>(perStore.values()), grand);
    }

    // (개선형) 상점 배송비·무료배송·거리비용 반영 알고리즘은 이후 단계에서 추가
}

