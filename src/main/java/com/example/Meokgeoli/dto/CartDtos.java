package com.example.Meokgeoli.dto;

import java.util.List;

public class CartDtos {

    public static class Item {
        private Long baseProductId;
        private int quantity;
        public Long getBaseProductId(){ return baseProductId; }
        public int getQuantity(){ return quantity; }
        public void setBaseProductId(Long v){ this.baseProductId = v; }
        public void setQuantity(int v){ this.quantity = v; }
    }

    public static class CartRequest {
        private List<Item> items;
        private Double userLat; // 선택(거리 비용 쓸 때)
        private Double userLng; // 선택
        private Integer alphaPerKm; // 선택: 원/㎞
        public List<Item> getItems(){ return items; }
        public Double getUserLat(){ return userLat; }
        public Double getUserLng(){ return userLng; }
        public Integer getAlphaPerKm(){ return alphaPerKm; }
        public void setItems(List<Item> items){ this.items = items; }
        public void setUserLat(Double v){ this.userLat = v; }
        public void setUserLng(Double v){ this.userLng = v; }
        public void setAlphaPerKm(Integer v){ this.alphaPerKm = v; }
    }
}
