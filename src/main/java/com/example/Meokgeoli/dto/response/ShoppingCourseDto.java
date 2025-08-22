package com.example.Meokgeoli.dto.response;
import java.util.List;

public class ShoppingCourseDto {
    private String storeName;
    private StoreLocationDto storeLocation;
    private int storeTotalCost;
    private List<ItemDto> items;

    public ShoppingCourseDto(String storeName, StoreLocationDto storeLocation, int storeTotalCost, List<ItemDto> items) {
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.storeTotalCost = storeTotalCost;
        this.items = items;
    }
    public String getStoreName() { return storeName; }
    public StoreLocationDto getStoreLocation() { return storeLocation; }
    public int getStoreTotalCost() { return storeTotalCost; }
    public List<ItemDto> getItems() { return items; }
}