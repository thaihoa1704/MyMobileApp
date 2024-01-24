package com.example.myapplication.Repositories;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Listener.FireStoreCartProductList;
import com.example.myapplication.Models.CartProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CartProductRepository {
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private CollectionReference collectionReference;
    private MutableLiveData<Boolean> check;
    private FireStoreCartProductList fireStoreCartProductList;
    public CartProductRepository(FireStoreCartProductList fireStoreCartProductList) {
        this.fireStoreCartProductList = fireStoreCartProductList;
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userId = firebaseAuth.getUid();
        this.check = new MutableLiveData<>();
        this.collectionReference = db.collection("User").document(userId).collection("Cart");
    }
    public MutableLiveData<Boolean> getCheck() {
        return check;
    }
    public void checkProductInCart(CartProduct cartProduct){
        String documentId = cartProduct.getProduct().getProductId();
        collectionReference.document(documentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        incrementQuantityProductInCart(cartProduct);
                    }else {
                        addProductToCart(cartProduct);
                    }
                }else {
                    check.postValue(false);
                }
            }
        });
    }
    public void addProductToCart(CartProduct cartProduct){
        collectionReference.document(cartProduct.getProduct().getProductId()).set(cartProduct)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void incrementQuantityProductInCart(CartProduct cartProduct){
        collectionReference.document(cartProduct.getProduct().getProductId())
                .update("quantity", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void decrementQuantityProductInCart(CartProduct cartProduct){
        collectionReference.document(cartProduct.getProduct().getProductId())
                .update("quantity", FieldValue.increment(-1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void deleteProductInCart(CartProduct cartProduct){
        collectionReference.document(cartProduct.getProduct().getProductId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }
    public void getCartProductList(){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    fireStoreCartProductList.onCallbackCartProductList(task.getResult().toObjects(CartProduct.class));
                }else {

                }
            }
        });
    }
}
