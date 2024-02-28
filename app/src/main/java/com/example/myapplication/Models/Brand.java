package com.example.myapplication.Models;

public class Brand {
    private String brandName;
    private String image;

    public Brand() {
    }

    public Brand(String brandName, String image) {
        this.brandName = brandName;
        this.image = image;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
