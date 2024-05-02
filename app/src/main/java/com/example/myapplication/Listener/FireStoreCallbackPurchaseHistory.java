package com.example.myapplication.Listener;

import com.example.myapplication.Models.Order;

import java.util.List;

public interface FireStoreCallbackPurchaseHistory {
    void onCallback(List<Order> list);
}
