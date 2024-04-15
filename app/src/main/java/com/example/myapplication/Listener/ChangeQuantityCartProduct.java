package com.example.myapplication.Listener;

public interface ChangeQuantityCartProduct {
    void incrementQuantity(String documentId);
    void decrementQuantity(String documentId);
    void deleteProduct(String documentId);
}
