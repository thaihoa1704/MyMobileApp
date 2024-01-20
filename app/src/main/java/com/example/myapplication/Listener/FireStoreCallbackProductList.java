package com.example.myapplication.Listener;

import com.example.myapplication.Models.Product;

import java.util.List;

public interface FireStoreCallbackProductList {
    void onProductListLoad(List<Product> list);
}
