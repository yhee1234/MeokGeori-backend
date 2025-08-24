package com.example.Meokgeoli.controller;

import com.example.Meokgeoli.dto.request.RecommendationReqDto;
import com.example.Meokgeoli.dto.response.RecommendationResDto;
import com.example.Meokgeoli.dto.response.ShoppingCourseDto;
import com.example.Meokgeoli.dto.response.ItemDto;
import com.example.Meokgeoli.dto.response.StoreLocationDto;
import com.example.Meokgeoli.entity.CartItem;
import com.example.Meokgeoli.entity.Store;
import com.example.Meokgeoli.service.OpenAiService;
import com.example.Meokgeoli.service.ShoppingService;
import com.example.Meokgeoli.service.StoreService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommend")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final StoreService storeService;
    private final ShoppingService shoppingService;
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    public RecommendationController(StoreService storeService, ShoppingService shoppingService,
                                    OpenAiService openAiService, ObjectMapper objectMapper) {
        this.storeService = storeService;
        this.shoppingService = shoppingService;
        this.openAiService = openAiService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/course")
    public ResponseEntity<RecommendationResDto> recommendCourse(@RequestBody RecommendationReqDto requestDto) {
        try {
            String userInput = requestDto.getPrompt();
            double userLat = requestDto.getUserLocation().getLatitude();
            double userLon = requestDto.getUserLocation().getLongitude();

            // 1. (AI) 사용자 입력 파싱 -> 예산, 구매 희망 목록 추출
            String parsedJson = openAiService.parseUserInput(userInput);

            // JSON 파싱을 안전하게 처리
            int budget = 50000; // 기본값
            List<String> desiredItems = new ArrayList<>();

            try {
                Map<String, Object> parsedInput = objectMapper.readValue(parsedJson, new TypeReference<>() {});

                // budget 안전하게 추출
                Object budgetObj = parsedInput.get("budget");
                if (budgetObj instanceof Integer) {
                    budget = (Integer) budgetObj;
                } else if (budgetObj instanceof String) {
                    try {
                        budget = Integer.parseInt((String) budgetObj);
                    } catch (NumberFormatException e) {
                        budget = 50000; // 파싱 실패시 기본값
                    }
                }

                // items 안전하게 추출
                Object itemsObj = parsedInput.get("items");
                if (itemsObj instanceof List) {
                    desiredItems = (List<String>) itemsObj;
                }
            } catch (Exception e) {
                // JSON 파싱 실패시 기본값 사용
                System.err.println("JSON 파싱 실패: " + parsedJson + ", 오류: " + e.getMessage());
                desiredItems = Arrays.asList("삼겹살", "상추", "마늘"); // 기본 아이템
            }

            // 2. (DB) 위치 기반으로 주변 상점 검색
            List<Store> nearbyStores = storeService.findStoresWithinRadius(userLat, userLon, 2.0);

            if (nearbyStores.isEmpty()) {
                return ResponseEntity.ok(new RecommendationResDto(0, new ArrayList<>()));
            }

            // 3. (Logic) 예산과 희망 목록으로 장바구니 최적화
            List<CartItem> optimizedCart = shoppingService.optimizeCart(nearbyStores, budget, desiredItems);

            if (optimizedCart.isEmpty()) {
                return ResponseEntity.ok(new RecommendationResDto(0, new ArrayList<>()));
            }

            // 4. (AI) 방문할 상점 목록으로 최적 경로 추천
            List<Store> storesToVisit = optimizedCart.stream()
                    .map(cartItem -> cartItem.getProduct().getStore())
                    .distinct()
                    .collect(Collectors.toList());

            List<String> sortedStoreNames = new ArrayList<>();
            if (!storesToVisit.isEmpty()) {
                String routeResult = openAiService.getOptimalRoute(storesToVisit);
                if (routeResult != null && !routeResult.trim().isEmpty() && !routeResult.contains("오류")) {
                    sortedStoreNames = Arrays.stream(routeResult.split(" -> "))
                            .map(String::trim)
                            .collect(Collectors.toList());
                } else {
                    // AI 실패시 기본 순서 사용
                    sortedStoreNames = storesToVisit.stream()
                            .map(Store::getName)
                            .collect(Collectors.toList());
                }
            }

            // 5. 프론트엔드에 보낼 응답 데이터 가공
            Map<String, List<CartItem>> itemsByStore = optimizedCart.stream()
                    .collect(Collectors.groupingBy(item -> item.getProduct().getStore().getName()));

            List<ShoppingCourseDto> shoppingCourses = sortedStoreNames.stream()
                    .filter(itemsByStore::containsKey) // 실제 장바구니 아이템이 있는 상점만
                    .map(storeName -> {
                        List<CartItem> storeCartItems = itemsByStore.get(storeName);
                        Store store = storeCartItems.get(0).getProduct().getStore();
                        int storeTotalCost = storeCartItems.stream().mapToInt(CartItem::getTotalPrice).sum();
                        List<ItemDto> itemDtos = storeCartItems.stream()
                                .map(item -> new ItemDto(
                                        item.getProduct().getName(),
                                        item.getUnitPrice(),
                                        item.getProduct().getImageUrl()
                                ))
                                .collect(Collectors.toList());
                        StoreLocationDto storeLocation = new StoreLocationDto(store.getLatitude(), store.getLongitude());
                        return new ShoppingCourseDto(storeName, storeLocation, storeTotalCost, itemDtos);
                    })
                    .collect(Collectors.toList());

            int totalCost = shoppingCourses.stream().mapToInt(ShoppingCourseDto::getStoreTotalCost).sum();

            return ResponseEntity.ok(new RecommendationResDto(totalCost, shoppingCourses));

        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생시 빈 결과 반환
            return ResponseEntity.ok(new RecommendationResDto(0, new ArrayList<>()));
        }
    }
}