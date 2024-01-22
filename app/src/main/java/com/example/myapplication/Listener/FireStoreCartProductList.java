package com.example.myapplication.Listener;

import com.example.myapplication.Models.CartProduct;

import java.util.List;

public interface FireStoreCartProductList {
    void onCallbackCartProductList(List<CartProduct> list);
}
