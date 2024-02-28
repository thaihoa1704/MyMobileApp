package com.example.myapplication.Models;

import java.util.List;

public class Category {
    private String categoryName;
    private List<Brand> brandList;

    public Category() {
    }

    public Category(String name, List<Brand> brandList) {
        this.categoryName = name;
        this.brandList = brandList;
    }

    public String getName() {
        return categoryName;
    }

    public void setName(String name) {
        this.categoryName = name;
    }

    public List<Brand> getBrandList() {
        return brandList;
    }

    public void setBrandList(List<Brand> brandList) {
        this.brandList = brandList;
    }
}
