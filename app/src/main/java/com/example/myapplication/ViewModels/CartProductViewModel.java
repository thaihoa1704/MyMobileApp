package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCartProductList;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Product;
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
    public void getList(){
        repository.getCartProductList();
    }

    public void incrementQuantity(CartProduct cartProduct){
        repository.incrementQuantityProductInCart(cartProduct);
    }
    public void decrementQuantity(CartProduct cartProduct){
        repository.decrementQuantityProductInCart(cartProduct);
    }
    public MutableLiveData<List<CartProduct>> getCartProductList() {
        return cartProductList;
    }
    public void deleteProduct(CartProduct cartProduct) {
        repository.deleteProductInCart(cartProduct);
    }
    public void selectProduct(CartProduct cartProduct, boolean select){
        repository.selectProduct(cartProduct, select);
    }
    public void selectNoneAllProduct(){
        repository.selectNoneAllProduct();
    }
}
