package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Repositories.CartProductRepository;

public class CartProductViewModel extends ViewModel {
    private CartProductRepository repository;
    private MutableLiveData<Boolean> checkCartProduct;
    public CartProductViewModel(){
        this.repository = new CartProductRepository();
        this.checkCartProduct = repository.getCheck();
    }
    public void checkProductInCart(CartProduct cartProduct){
        repository.checkProductInCart(cartProduct);
    }
    public MutableLiveData<Boolean> getCheckCartProduct() {
        return checkCartProduct;
    }
}
