package com.example.myapplication.Models;

import com.example.myapplication.Models.ProductVersion.PhoneVersion;

public class CartPhone extends CartProductWithoutVersion {
    private PhoneVersion version;

    public CartPhone() {
    }

    public CartPhone(Product product, PhoneVersion version, int quantity, boolean select) {
        super(product, quantity, select);
        this.version = version;
    }

    public PhoneVersion getVersion() {
        return version;
    }

    public void setVersion(PhoneVersion version) {
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
