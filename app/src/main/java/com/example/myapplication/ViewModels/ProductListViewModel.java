package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Helper.ComparatorPrice;
import com.example.myapplication.Listener.FireStoreCallbackProductList;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Product;
import com.example.myapplication.Repositories.ProductListRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public void getProductList(String categoryName, ArrayList<String> list){
        repository.getProductData(categoryName, list);
    }
    public void getProductList(String categoryName, String brandname){
        repository.getProductData(categoryName, brandname);
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
    public void orderByPriceDescending(List<Product> productList){
        Collections.sort(productList, ComparatorPrice.descending);
    }
    public void orderByPriceAscending(List<Product> productList){
        Collections.sort(productList, ComparatorPrice.ascending);
    }
}
