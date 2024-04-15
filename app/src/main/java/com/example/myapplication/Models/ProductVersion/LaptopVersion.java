package com.example.myapplication.Models.ProductVersion;

public class LaptopVersion {
    private String color;
    private String hardDrive;
    private String ram;
    private String cpu;
    private int price;
    private int quantity;

    public LaptopVersion() {
    }

    public LaptopVersion(String color, String hardDrive, String ram, String cpu, int price, int quantity) {
        this.color = color;
        this.hardDrive = hardDrive;
        this.ram = ram;
        this.cpu = cpu;
        this.price = price;
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getHardDrive() {
        return hardDrive;
    }

    public void setHardDrive(String hardDrive) {
        this.hardDrive = hardDrive;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }
}
