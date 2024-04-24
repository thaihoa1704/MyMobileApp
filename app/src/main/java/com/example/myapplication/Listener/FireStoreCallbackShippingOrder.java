package com.example.myapplication.Listener;

import com.example.myapplication.Models.Order;

import java.util.List;

public interface FireStoreCallbackShippingOrder {
    void onCallbackS(List<Order> list);
}
