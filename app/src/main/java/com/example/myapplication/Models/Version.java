package com.example.myapplication.Models;

import java.util.Objects;

public class Version {
    private String id;
    private String color;
    private String ram;
    private String storage;
    private String hardDrive;
    private String cpu;
    private boolean bluetooth;
    private String hpType;
    private String accessoryType;
    private int diameter;
    private int price;
    private int quantity;
    public Version() {
    }
    public Version(String id, String color, String storage, String ram, String hardDrive, String cpu, boolean bluetooth, String hpType, String accessoryType, int diameter, int price, int quantity) {
        this.id = id;
        this.color = color;
        this.storage = storage;
        this.ram = ram;
        this.hardDrive = hardDrive;
        this.cpu = cpu;
        this.bluetooth = bluetooth;
        this.hpType = hpType;
        this.accessoryType = accessoryType;
        this.diameter = diameter;
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

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getHardDrive() {
        return hardDrive;
    }

    public void setHardDrive(String hardDrive) {
        this.hardDrive = hardDrive;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public boolean isBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(boolean bluetooth) {
        this.bluetooth = bluetooth;
    }

    public String getHpType() {
        return hpType;
    }

    public void setHpType(String hpType) {
        this.hpType = hpType;
    }

    public String getAccessoryType() {
        return accessoryType;
    }

    public void setAccessoryType(String accessoryType) {
        this.accessoryType = accessoryType;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
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
        Version version = (Version) object;
        return Objects.equals(ram, version.ram) && Objects.equals(storage, version.storage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ram, storage);
    }
}
