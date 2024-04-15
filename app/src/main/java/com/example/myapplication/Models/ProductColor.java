package com.example.myapplication.Models;

public class ProductColor {
    private String color;
    private String colorCode;

    public ProductColor(String color, String colorCode) {
        this.color = color;
        this.colorCode = colorCode;
    }

    public ProductColor() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
