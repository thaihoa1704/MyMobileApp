package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCartProductList;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Repositories.CartProductRepository;

import java.util.List;

public class CartProductViewModel extends ViewModel implements FireStoreCartProductList {
    private CartProductRepository repository;
    private MutableLiveData<Boolean> checkCartProduct;
    private MutableLiveData<List<CartProduct>> cartProductList;
    public CartProductViewModel(){
        this.repository = new CartProductRepository(this);
        this.cartProductList = new MutableLiveData<>();
        this.checkCartProduct = repository.getCheck();
    }
    public void checkProductInCart(CartProduct cartProduct){
        repository.checkProductInCart(cartProduct);
    }
    public MutableLiveData<Boolean> getCheckCartProduct() {
        return checkCartProduct;
    }
    @Override
    public void onCallbackCartProductList(List<CartProduct> list) {
        cartProductList.postValue(list);
    }
    public MutableLiveData<List<CartProduct>> getCartProductList() {
        return cartProductList;
    }
}
