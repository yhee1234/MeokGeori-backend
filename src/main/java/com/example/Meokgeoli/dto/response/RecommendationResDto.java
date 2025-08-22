package com.example.Meokgeoli.dto.response;

import java.util.List;

// 최종 응답의 전체 구조를 나타내는 클래스
public class RecommendationResDto {
    private int totalCost;
    private List<ShoppingCourseDto> shoppingCourse;

    // 생성자, Getter
    public RecommendationResDto(int totalCost, List<ShoppingCourseDto> shoppingCourse) {
        this.totalCost = totalCost;
        this.shoppingCourse = shoppingCourse;
    }
    public int getTotalCost() { return totalCost; }
    public List<ShoppingCourseDto> getShoppingCourse() { return shoppingCourse; }
}