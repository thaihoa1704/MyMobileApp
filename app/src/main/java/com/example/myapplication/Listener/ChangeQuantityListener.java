package com.example.myapplication.Listener;

import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Product;

public interface ChangeQuantityListener {
    void incrementQuantity(CartProduct cartProduct);
    void decrementQuantity(CartProduct cartProduct);
    void deleteProduct(CartProduct cartProduct);

}
