package com.example.myapplication.Listener;

import com.example.myapplication.Models.Order;

import java.util.List;

public interface FireStoreCallbackConfirmOrder {
    void onCallback(List<Order> list);
}
