package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCallbackCartProducts;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Repositories.CartRepository;

import java.util.List;

public class CartViewModel extends ViewModel implements FireStoreCallbackCartProducts {
    private CartRepository repository;
    private MutableLiveData<List<CartProduct>> listMutableLiveData;
    private MutableLiveData<Boolean> check;

    public CartViewModel() {
        this.repository = new CartRepository(this);
        this.listMutableLiveData = new MutableLiveData<>();
        this.check = repository.getCheck();
    }
    public void getAllProductsInCart(){
        repository.getAllProductsInCart();
    }

    public MutableLiveData<Boolean> getCheck() {
        return check;
    }

    public void incrementQuantity(String documentId){
        repository.incrementQuantityProductInCart(documentId);
    }
    public void decrementQuantity(String documentId){
        repository.decrementQuantityProductInCart(documentId);
    }
    public void deleteProductInCart(String documentId){
        repository.deleteProductInCart(documentId);
    }
    @Override
    public void onCallback(List<CartProduct> cartProducts) {
        listMutableLiveData.postValue(cartProducts);
    }

    public MutableLiveData<List<CartProduct>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public void selectProduct(String documentId, boolean aBoolean) {
        repository.selectProduct(documentId, aBoolean);
    }
    public void selectNoneAllProduct(){
        repository.selectNoneAllProduct();
    }

    public void getProductSelected() {
        repository.getProductSelected();
    }
}
