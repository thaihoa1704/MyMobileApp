package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.PhoneModel.FireStoreCallbackPhoneVersion;
import com.example.myapplication.Listener.PhoneModel.FireStorePhoneVersionList;
import com.example.myapplication.Models.CartPhone;
import com.example.myapplication.Models.ProductVersion.PhoneVersion;
import com.example.myapplication.Repositories.PhoneRepository;

import java.util.List;

public class PhoneViewModel extends ViewModel implements FireStorePhoneVersionList, FireStoreCallbackPhoneVersion {
    private PhoneRepository repository;
    private MutableLiveData<PhoneVersion> phoneVersion;
    private MutableLiveData<List<PhoneVersion>> phoneVersions;
    private MutableLiveData<Boolean> check;
    public PhoneViewModel() {
        this.repository = new PhoneRepository(this, this);
        this.phoneVersions = new MutableLiveData<>();
        this.phoneVersion = new MutableLiveData<>();
        this.check = repository.getCheck();
    }
    public void getPhonePrice(String productId, String color, String ram, String storage){
        repository.getPhonePrice(productId, color, ram, storage);
    }
    public void getPhoneVersions(String productId){
        repository.getPhoneVersions(productId);
    }
    public void checkProductInCart(CartPhone cartPhone){
        repository.checkProductInCart(cartPhone);
    }
    public MutableLiveData<PhoneVersion> getPhoneVersion() {
        return phoneVersion;
    }
    public MutableLiveData<List<PhoneVersion>> getPhoneVersions() {
        return phoneVersions;
    }
    public MutableLiveData<Boolean> getCheck() {
        return check;
    }
    @Override
    public void onCallBackPhoneVersion(PhoneVersion version) {
        phoneVersion.postValue(version);
    }
    @Override
    public void onCallbackPhoneAttributeVersion(List<PhoneVersion> phoneVersionList) {
        phoneVersions.postValue(phoneVersionList);
    }
}
