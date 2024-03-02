package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCallbackProductList;
import com.example.myapplication.Models.Product;
import com.example.myapplication.Repositories.ProductListRepository;

import java.util.List;

public class ProductListViewModel extends ViewModel implements FireStoreCallbackProductList {
    private ProductListRepository repository;
    private MutableLiveData<List<Product>> listMutableLiveData;
    public ProductListViewModel(){
        this.repository = new ProductListRepository(this);
        this.listMutableLiveData = new MutableLiveData<>();
    }
    public void getProductList(String categoryName){
        repository.getProductData(categoryName);
    }
    public void getAllProduct(){
        repository.getAllProductData();
    }
    @Override
    public void onProductListLoad(List<Product> list) {
        listMutableLiveData.postValue(list);
    }
    public MutableLiveData<List<Product>> getListMutableLiveData() {
        return listMutableLiveData;
    }
    public void orderByPriceAscending(String category){
        repository.orderByPriceAscending(category);
    }
    public void orderByPriceDescending(String category){
        repository.orderByPriceDescending(category);
    }
}
