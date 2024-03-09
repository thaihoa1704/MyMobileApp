package com.example.myapplication.Models;

import java.io.Serializable;

public class Brand implements Serializable {
    private String brandName;
    private String image;
    private boolean selected;
    public Brand() {
    }

    public Brand(String brandName, String image, boolean selected) {
        this.brandName = brandName;
        this.image = image;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
