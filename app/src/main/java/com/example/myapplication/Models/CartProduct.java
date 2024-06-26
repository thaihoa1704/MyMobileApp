package com.example.myapplication.Models;

public class CartProduct extends CartProductWithoutVersion{
    private Version version;

    public CartProduct() {
    }

    public CartProduct(Product product, Version version, int quantity, boolean select) {
        super(product, quantity, select);
        this.version = version;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    @Override
    public Product getProduct() {
        return super.getProduct();
    }

    @Override
    public void setProduct(Product product) {
        super.setProduct(product);
    }

    @Override
    public int getQuantity() {
        return super.getQuantity();
    }

    @Override
    public void setQuantity(int quantity) {
        super.setQuantity(quantity);
    }

    @Override
    public boolean isSelect() {
        return super.isSelect();
    }

    @Override
    public void setSelect(boolean select) {
        super.setSelect(select);
    }
}
