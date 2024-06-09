package com.example.myapplication.Listener;

import com.example.myapplication.Models.Price;

import java.util.List;

public interface FireStoreCallbackPriceList {
    void onPriceListLoad(List<Price> list);
}
