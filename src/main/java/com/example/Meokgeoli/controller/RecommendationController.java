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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommend")
@CrossOrigin(origins = "*")
public class RecommendationController {

    private final StoreService storeService;
    private final ShoppingService shoppingService;
    private final OpenAiService openAiService;
    private final ObjectMapper objectMapper;

    public RecommendationController(StoreService storeService, ShoppingService shoppingService, OpenAiService openAiService, ObjectMapper objectMapper) {
        this.storeService = storeService;
        this.shoppingService = shoppingService;
        this.openAiService = openAiService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/course")
    public RecommendationResDto recommendCourse(@RequestBody RecommendationReqDto requestDto) throws IOException {
        String userInput = requestDto.getPrompt();
        double userLat = requestDto.getUserLocation().getLatitude();
        double userLon = requestDto.getUserLocation().getLongitude();

        // 1. (AI) 사용자 입력 파싱 -> 예산, 구매 희망 목록 추출
        String parsedJson = openAiService.parseUserInput(userInput);
        Map<String, Object> parsedInput = objectMapper.readValue(parsedJson, new TypeReference<>() {});
        int budget = (Integer) parsedInput.get("budget");
        List<String> desiredItems = (List<String>) parsedInput.get("items");

        // 2. (DB) 위치 기반으로 주변 상점 검색
        List<Store> nearbyStores = storeService.findStoresWithinRadius(userLat, userLon, 2.0);

        // 3. (Logic) 예산과 희망 목록으로 장바구니 최적화
        List<CartItem> optimizedCart = shoppingService.optimizeCart(nearbyStores, budget, desiredItems);

        // 4. (AI) 방문할 상점 목록으로 최적 경로 추천
        List<Store> storesToVisit = optimizedCart.stream()
                .map(cartItem -> cartItem.getProduct().getStore())
                .distinct().collect(Collectors.toList());

        List<String> sortedStoreNames = List.of();
        if (!storesToVisit.isEmpty()) {
            String routeResult = openAiService.getOptimalRoute(storesToVisit); // "상점1 -> 상점2 -> ..."
            sortedStoreNames = List.of(routeResult.split(" -> "));
        }

        // 5. 프론트엔드에 보낼 응답 데이터 가공
        Map<String, List<CartItem>> itemsByStore = optimizedCart.stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getStore().getName()));

        List<ShoppingCourseDto> shoppingCourses = sortedStoreNames.stream()
                .map(storeName -> {
                    List<CartItem> storeCartItems = itemsByStore.get(storeName);
                    Store store = storeCartItems.get(0).getProduct().getStore();
                    int storeTotalCost = storeCartItems.stream().mapToInt(CartItem::getTotalPrice).sum();
                    List<ItemDto> itemDtos = storeCartItems.stream()
                            .map(item -> new ItemDto(item.getProduct().getName(), item.getUnitPrice(), item.getProduct().getImageUrl()))
                            .collect(Collectors.toList());
                    StoreLocationDto storeLocation = new StoreLocationDto(store.getLatitude(), store.getLongitude());
                    return new ShoppingCourseDto(storeName, storeLocation, storeTotalCost, itemDtos);
                }).collect(Collectors.toList());

        int totalCost = shoppingCourses.stream().mapToInt(ShoppingCourseDto::getStoreTotalCost).sum();

        return new RecommendationResDto(totalCost, shoppingCourses);
    }
}