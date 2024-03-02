package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCallbackBrandList;
import com.example.myapplication.Listener.FireStoreCallbackCategory;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Category;
import com.example.myapplication.Repositories.CategoryRepository;

import java.util.List;

public class CategoryViewModel extends ViewModel implements FireStoreCallbackCategory, FireStoreCallbackBrandList {
    private CategoryRepository repository;
    private MutableLiveData<List<Brand>> mutableLiveData;
    public CategoryViewModel(){
        this.repository = new CategoryRepository(this, this);
        this.mutableLiveData = new MutableLiveData<>();
    }
    public void getCategoryData(String category){
        repository.getCategoryData(category);
    }

    @Override
    public void onCategoryLoad(Category category) {

    }

    public MutableLiveData<List<Brand>> getMutableLiveData() {
        return mutableLiveData;
    }

    @Override
    public void onBrandListLoad(List<Brand> list) {
        mutableLiveData.postValue(list);
    }
}
