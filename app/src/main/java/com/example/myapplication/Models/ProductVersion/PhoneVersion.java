package com.example.myapplication.Models.ProductVersion;

import java.util.Objects;

public class PhoneVersion {
    private String id;
    private String color;
    private String ram;
    private String storage;
    private int price;
    private int quantity;

    public PhoneVersion() {
    }

    public PhoneVersion(String id, String color, String ram, String storage, int price, int quantity) {
        this.id = id;
        this.color = color;
        this.ram = ram;
        this.storage = storage;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        PhoneVersion version = (PhoneVersion) object;
        return Objects.equals(ram, version.ram) && Objects.equals(storage, version.storage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ram, storage);
    }
}
