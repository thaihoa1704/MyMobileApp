package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCartProductList;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends ViewModel implements FireStoreCartProductList {
    private OrderRepository repository;
    private MutableLiveData<List<CartProduct>> listMutableLiveData;
    private MutableLiveData<Boolean> checkOrder;

    public OrderViewModel(){
        this.repository = new OrderRepository(this);
        this.listMutableLiveData = new MutableLiveData<>();
        this.checkOrder = repository.getCheck();
    }
    public void getProductSelected(){
        repository.getProductSelected();
    }
    public void addOrder(List<CartProduct> list){
        repository.addOrder(list);
    }
    public void deleteProductInCart(List<CartProduct> list){
        repository.deleteProductInCart(list);
    }
    public void updateQuantityProduct(List<CartProduct> list){
        repository.updateQuantityProduct(list);
    }
    @Override
    public void onCallbackCartProductList(List<CartProduct> list) {
        listMutableLiveData.postValue(list);
    }
    public MutableLiveData<List<CartProduct>> getListMutableLiveData() {
        return listMutableLiveData;
    }
    public MutableLiveData<Boolean> getCheckOrder() {
        return checkOrder;
    }
}