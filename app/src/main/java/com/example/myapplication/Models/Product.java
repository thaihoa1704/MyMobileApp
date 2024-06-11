package com.example.myapplication.Models;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    private  String id;
    private String name;
    private List<String> images;
    private int price;
    private String category;
    private String brand;
    private String description;
    private List<ProductColor> colors;
    private boolean special;
    public Product() {
    }

    public Product(String id, String name, List<String> images, int price, String category, String brand, String description, List<ProductColor> colors, boolean special) {
        this.id = id;
        this.name = name;
        this.images = images;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.colors = colors;
        this.special = special;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImageList(List<String> images) {
        this.images = images;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<ProductColor> getColors() {
        return colors;
    }

    public void setColors(List<ProductColor> colors) {
        this.colors = colors;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }
}
