package com.example.myapplication.Models;

import java.io.Serializable;

public class Product implements Serializable {
    private  String productId;
    private String productName;
    private String image;
    private int price;
    private String category;
    private String brand;
    private String description;
    private int quantity;
    public Product() {
    }
    public Product(String productId, String productName, String image, int price, String category, String brand, String description, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
