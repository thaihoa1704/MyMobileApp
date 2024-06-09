package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCallbackBrandList;
import com.example.myapplication.Listener.FireStoreCallbackPriceList;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Category;
import com.example.myapplication.Models.Price;
import com.example.myapplication.Repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel implements FireStoreCallbackBrandList, FireStoreCallbackPriceList {
    private CategoryRepository repository;
    private MutableLiveData<List<Brand>> mutableLiveDataBrand;
    private MutableLiveData<List<Price>> mutableLiveDataPrice;
    public CategoryViewModel(){
        this.repository = new CategoryRepository(this, this);
        this.mutableLiveDataBrand = new MutableLiveData<>();
        this.mutableLiveDataPrice = new MutableLiveData<>();
    }
    public void getBrandList(String category){
        repository.getBrandList(category);
    }
    public void getPriceList(String category){
        repository.getPriceList(category);
    }

    public MutableLiveData<List<Brand>> getMutableLiveDataBrand() {
        return mutableLiveDataBrand;
    }
    public MutableLiveData<List<Price>> getMutableLiveDataPrice() {
        return mutableLiveDataPrice;
    }

    @Override
    public void onBrandListLoad(List<Brand> list) {
        mutableLiveDataBrand.postValue(list);
    }

    @Override
    public void onPriceListLoad(List<Price> list) {
        mutableLiveDataPrice.postValue(list);
    }
}
