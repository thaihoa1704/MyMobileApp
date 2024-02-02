package com.example.myapplication.ViewModels;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Listener.FireStoreCallbackUser;
import com.example.myapplication.Models.User;
import com.google.firebase.auth.FirebaseUser;
import com.example.myapplication.Repositories.UserRepository;

public class UserViewModel extends AndroidViewModel implements FireStoreCallbackUser {
    private UserRepository repository;
    private FirebaseUser currentUser;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<User> userlogin;
    private User userLogged;
    public UserViewModel(@NonNull Application application) {
        super(application);
        this.repository = new UserRepository(application, this);
        this.currentUser = repository.getCurrentUser();
        this.userLiveData = repository.getUserLiveData();
        this.loggedOutLiveData = repository.getLoggedOutLiveData();
        this.userlogin = repository.getUserLogin();
        this.userLogged = new User();
    }
    public FirebaseUser getCurrentUser() {
        return currentUser;
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
}
