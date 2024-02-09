package com.example.myapplication.Models;

import com.google.type.DateTime;

import java.util.Date;
import java.util.List;

public class Order {
    private List<CartProduct> list;
    private long dateTime;
    private String address;
    private String status;
    private int total;
    public Order() {
    }
    public Order(List<CartProduct> list, long dateTime, String address, String status, int total) {
        this.list = list;
        this.dateTime = dateTime;
        this.address = address;
        this.status = status;
        this.total = total;
    }

    public List<CartProduct> getList() {
        return list;
    }

    public void setList(List<CartProduct> list) {
        this.list = list;
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
}
