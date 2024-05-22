package com.example.myapplication.Repositories;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Listener.FireStoreCallbackAddress;
import com.example.myapplication.Listener.FireStoreCallbackUser;
import com.example.myapplication.Models.Address;
import com.example.myapplication.Models.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<User> userLogin;
    private MutableLiveData<Boolean> check;
    private FirebaseFirestore db;
    private FireStoreCallbackUser fireStoreCallbackUser;
    private String userId;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;
    private FireStoreCallbackAddress fireStoreCallbackAddress;
    public UserRepository(FireStoreCallbackUser fireStoreCallbackUser, FireStoreCallbackAddress fireStoreCallbackAddress) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.userLogin = new MutableLiveData<>();
        this.check = new MutableLiveData<>();
        this.fireStoreCallbackUser = fireStoreCallbackUser;
        this.fireStoreCallbackAddress = fireStoreCallbackAddress;

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
            this.userId = firebaseAuth.getUid();
            this.documentReference = db.collection("User").document(userId);
            this.collectionReference = db.collection("User").document(userId).collection("Address");
        }
    }
    public FirebaseUser getCurrentUser(){return firebaseAuth.getCurrentUser();}
    public MutableLiveData<FirebaseUser> getUserLiveData() { return userLiveData;}
    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
    public MutableLiveData<User> getUserLogin(){return userLogin;}
    public MutableLiveData<Boolean> getCheck() {
        return check;
    }

    public void userRegister(User user){
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            updateUserInfo(user);
                            check.postValue(true);
                        }else {
                            check.postValue(false);
                        }
                    }
                });
    }

    private void updateUserInfo(User user) {
        String uid = firebaseAuth.getUid();
        String userType = "customer";

        user.setId(uid);
        user.setType(userType);

        //Add info user into Cloud FireStore
        //Path: Users/uid/...
        db.collection("User").document(uid).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        check.postValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        check.postValue(false);
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
                            check.postValue(true);
                        }else {
                            check.postValue(false);
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
                    check.postValue(true);
                }else {
                    check.postValue(false);
                }
            }
        });
    }

    public void changePassword(String oldPass, String newPass){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPass);
        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        updatePassword(newPass);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        check.postValue(false);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                check.postValue(false);
            }
        });
    }
    private void updatePassword(String newPass){
        documentReference.update("password", newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    check.postValue(true);
                } else {
                    check.postValue(false);
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
    public void addAddress(String string){
        String id = collectionReference.document().getId();
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult() == null){
                        Address address = new Address(id, string, true);
                        collectionReference.document(id).set(address).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                check.postValue(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                check.postValue(false);
                            }
                        });
                    }else {
                        Address address = new Address(id, string, false);
                        collectionReference.document(id).set(address).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                check.postValue(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                check.postValue(false);
                            }
                        });
                    }
                }
            }
        });
    }
}
