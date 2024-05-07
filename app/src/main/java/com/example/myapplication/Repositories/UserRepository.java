package com.example.myapplication.Repositories;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Listener.FireStoreCallbackAddress;
import com.example.myapplication.Listener.FireStoreCallbackUser;
import com.example.myapplication.Models.Address;
import com.example.myapplication.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<User> userLogin;
    private FirebaseFirestore db;
    private FireStoreCallbackUser fireStoreCallbackUser;
    private String userId;
    private CollectionReference collectionReference;
    private FireStoreCallbackAddress fireStoreCallbackAddress;
    public UserRepository(Application application, FireStoreCallbackUser fireStoreCallbackUser, FireStoreCallbackAddress fireStoreCallbackAddress) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.userLogin = new MutableLiveData<>();
        this.fireStoreCallbackUser = fireStoreCallbackUser;
        this.fireStoreCallbackAddress = fireStoreCallbackAddress;

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
            this.userId = firebaseAuth.getUid();
            this.collectionReference = db.collection("User").document(userId).collection("Address");
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

        user.setType(userType);

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
    public void getAddress(){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    fireStoreCallbackAddress.onCallback(task.getResult().toObjects(Address.class));
                }
            }
        });
    }
    public void addAddress(String address){

    }
    public void setAddressSelected(Address address){
        collectionReference.document(address.getId()).update("select", true)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
        collectionReference.whereNotEqualTo("id", address.getId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<String> list = new ArrayList<>();
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                list.add(queryDocumentSnapshot.getId());
                            }
                            updateData(list);
                        }
                    }
                });
    }
    private void updateData(List<String> list) {
        WriteBatch batch = db.batch();
        for (int i = 0; i < list.size(); i++){
            DocumentReference document = collectionReference.document(list.get(i));
            batch.update(document, "select", false);
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}
