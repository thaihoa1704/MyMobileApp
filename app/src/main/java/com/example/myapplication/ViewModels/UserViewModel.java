package com.example.myapplication.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.Listener.FireStoreCallbackAddress;
import com.example.myapplication.Listener.FireStoreCallbackUser;
import com.example.myapplication.Models.Address;
import com.example.myapplication.Models.User;
import com.google.firebase.auth.FirebaseUser;
import com.example.myapplication.Repositories.UserRepository;

import java.util.List;

public class UserViewModel extends ViewModel implements FireStoreCallbackUser, FireStoreCallbackAddress {
    private UserRepository repository;
    private FirebaseUser currentUser;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<User> userlogin;
    private MutableLiveData<List<Address>> addressMutableLiveData;
    private MutableLiveData<Boolean> check;
    private User userLogged;
    public UserViewModel() {
        this.repository = new UserRepository(this, this);
        this.currentUser = repository.getCurrentUser();
        this.userLiveData = repository.getUserLiveData();
        this.loggedOutLiveData = repository.getLoggedOutLiveData();
        this.userlogin = repository.getUserLogin();
        this.userLogged = new User();
        this.addressMutableLiveData = new MutableLiveData<>();
        this.check = repository.getCheck();
    }
    public FirebaseUser getCurrentUser() {
        return currentUser;
    }
    public MutableLiveData<Boolean> getCheck() {
        return check;
    }

    public void userLogin(User user){
        repository.userLogin(user);}
    public void userRegister(User user){
        repository.userRegister(user);
    }
    public void userLogout(){
        repository.userLogout();
    }
    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
    public MutableLiveData<User> getUserLogin(){return userlogin;}
    public void userLogged(){
        repository.userLogged();
    }
    public User getUserLogged(){return userLogged;}
    @Override
    public void onCallback(User user) {
        userlogin.postValue(user);
    }
    public void resetPassword(String email){
        repository.resetPassword(email);
    }
    public void setAddressSelected(Address address){
        repository.setAddressSelected(address);
    }
    public void getAddress(){
        repository.getAddress();
    }

    @Override
    public void onCallback(List<Address> addressList) {
        addressMutableLiveData.postValue(addressList);
    }

    public MutableLiveData<List<Address>> getAddressMutableLiveData() {
        return addressMutableLiveData;
    }
    public void addAddress(String string){
        repository.addAddress(string);
    }
    public void changePassword(String oldPassword, String newPassword){
        repository.changePassword(oldPassword, newPassword);
    }
}
