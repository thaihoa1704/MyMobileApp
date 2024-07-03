package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCallbackConfirmOrder;
import com.example.myapplication.Listener.FireStoreCallbackPurchaseHistory;
import com.example.myapplication.Listener.FireStoreCallbackRateOrder;
import com.example.myapplication.Listener.FireStoreCallbackShippingOrder;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Repositories.OrderRepository;

import java.util.List;

public class OrderViewModel extends ViewModel implements FireStoreCallbackConfirmOrder, FireStoreCallbackShippingOrder, FireStoreCallbackRateOrder, FireStoreCallbackPurchaseHistory {
    private OrderRepository repository;
    private MutableLiveData<Boolean> checkOrder;
    private MutableLiveData<List<Order>> confirmOrder;
    private MutableLiveData<List<Order>> shippingOrder;
    private MutableLiveData<List<Order>> rateOrder;
    private MutableLiveData<List<Order>> purchaseHistory;

    public OrderViewModel(){
        this.repository = new OrderRepository(this, this, this, this);
        this.checkOrder = repository.getCheck();
        this.confirmOrder = new MutableLiveData<>();
        this.shippingOrder = new MutableLiveData<>();
        this.rateOrder = new MutableLiveData<>();
        this.purchaseHistory = new MutableLiveData<>();
    }
    public void addOrder(List<CartProduct> list, String contact, String address, int tatol){
        repository.addOrder(list, contact, address, tatol);
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
    public void getConfirm(){
        repository.getConfirmOrder();
    }
    @Override
    public void onCallbackC(List<Order> list) {
        confirmOrder.postValue(list);
    }
    public MutableLiveData<List<Order>> getConfirmOrder() {
        return confirmOrder;
    }

    public void getShipping(){
        repository.getShippingOrder();
    }
    @Override
    public void onCallbackS(List<Order> list) {
        shippingOrder.postValue(list);
    }
    public MutableLiveData<List<Order>> getShippingOrder() {
        return shippingOrder;
    }

    public void getRate(){
        repository.getRateOrder();
    }
    @Override
    public void onCallbackR(List<Order> list) {
        rateOrder.postValue(list);
    }
    public MutableLiveData<List<Order>> getRateOrder() {
        return rateOrder;
    }

    public void getPurchaseHistory(){
        repository.getPurchaseHistory();
    }
    @Override
    public void onCallback(List<Order> list) {
        purchaseHistory.postValue(list);
    }
    public MutableLiveData<List<Order>> getPurchaseHistoryLiveData() {
        return purchaseHistory;
    }

    public void updateReceiveOrder(Order order){
        repository.updateReceiveOrder(order);
    }
    public void updateRateOrder(Order order, int star, String note){
        repository.updateRateOrder(order, star, note);
    }

    public void updateCanceleOrder(Order order) {
        repository.updateCanceleOrder(order);
    }
}