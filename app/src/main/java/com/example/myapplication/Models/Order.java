package com.example.myapplication.Models;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    private List<CartProduct> listProduct;
    private long dateTime;
    private String address;
    private String status;
    private int total;
    private String note;
    public Order() {
    }
    public Order(List<CartProduct> listProduct, long dateTime, String address, String status, int total, String note) {
        this.listProduct = listProduct;
        this.dateTime = dateTime;
        this.address = address;
        this.status = status;
        this.total = total;
        this.note = note;
    }

    public List<CartProduct> getListProduct() {
        return listProduct;
    }

    public void setListProduct(List<CartProduct> listProduct) {
        this.listProduct = listProduct;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
