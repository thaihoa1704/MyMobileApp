package com.example.myapplication.Listener;

import com.example.myapplication.Models.CartProduct;

import java.util.List;

public interface FireStoreCallbackCartProducts {
    void onCallback(List<CartProduct> cartProducts);
}
