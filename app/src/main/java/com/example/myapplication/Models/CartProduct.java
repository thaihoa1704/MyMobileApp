package com.example.myapplication.Models;

public class CartProduct {
    private Product product;
    private int quantity;
    private boolean select;

    public CartProduct(Product product, int quantity, boolean select) {
        this.product = product;
        this.quantity = quantity;
        this.select = select;
    }

    public CartProduct() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
