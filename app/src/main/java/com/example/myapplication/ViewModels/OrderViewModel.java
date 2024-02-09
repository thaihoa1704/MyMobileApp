package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCartProductList;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends ViewModel {
    private OrderRepository repository;
    private MutableLiveData<List<CartProduct>> listMutableLiveData;
    private MutableLiveData<Boolean> checkOrder;

    public OrderViewModel(){
        this.repository = new OrderRepository();
        this.listMutableLiveData = new MutableLiveData<>();
        this.checkOrder = repository.getCheck();
    }
    public void addOrder(List<CartProduct> list, String address, int tatol){
        repository.addOrder(list, address, tatol);
    }
    public void deleteProductInCart(List<CartProduct> list){
        repository.deleteProductInCart(list);
    }
    public void updateQuantityProduct(List<CartProduct> list){
        repository.updateQuantityProduct(list);
    }
    public MutableLiveData<Boolean> getCheckOrder() {
        return checkOrder;
    }
}