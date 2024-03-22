package com.example.myapplication.Repositories;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Listener.FireStoreCallbackUser;
import com.example.myapplication.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<User> userLogin;
    private FirebaseFirestore db;
    private FireStoreCallbackUser fireStoreCallbackUser;
    public UserRepository(Application application, FireStoreCallbackUser fireStoreCallbackUser) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.userLogin = new MutableLiveData<>();
        this.fireStoreCallbackUser = fireStoreCallbackUser;

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }
    public FirebaseUser getCurrentUser(){return firebaseAuth.getCurrentUser();}
    public MutableLiveData<FirebaseUser> getUserLiveData() { return userLiveData;}
    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
    public MutableLiveData<User> getUserLogin(){return userLogin;}

    public void userRegister(User user){
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            updateUserInfo(user);
                            Toast.makeText(application.getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(application.getApplicationContext(), "Đăng ký thất bại! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUserInfo(User user) {
        String uid = firebaseAuth.getUid();
        String userType = "customer";

        user.setUserType(userType);
        user.setAddress(null);

        //Add info user into Cloud FireStore
        //Path: Users/uid/...
        db.collection("User").document(uid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(application.getApplicationContext(), "Đăng ký thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void userLogin(User user){
        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            userLiveData.postValue(firebaseAuth.getCurrentUser());
                            postUserLiveData();
                            Toast.makeText(application.getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(application.getApplicationContext(), "Email hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void postUserLiveData(){
        String uid = firebaseAuth.getUid();
        DocumentReference documentRef = db.collection("User").document(uid);
        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User userF = documentSnapshot.toObject(User.class);
                userLogin.postValue(userF);
            }
        });
    }
    public void userLogged(){
        String uid = firebaseAuth.getUid();
        DocumentReference documentRef = db.collection("User").document(uid);
        documentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    User userF = documentSnapshot.toObject(User.class);
                    fireStoreCallbackUser.onCallback(userF);
                }
            }
        });
    }
    public void userLogout(){
        firebaseAuth.signOut();
        userLogin.postValue(null);
        loggedOutLiveData.postValue(true);
    }

    public void resetPassword(String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(application.getApplicationContext(), "Liên kết đặt lại mật khẩu đã được gửi đến Email đăng ký của bạn!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
