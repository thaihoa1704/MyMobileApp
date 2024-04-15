package com.example.myapplication.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Models.CartProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class OrderRepository {
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private CollectionReference collectionReferenceOrder;
    private CollectionReference collectionReferenceCart;
    private CollectionReference collectionReferenceProduct;
    private String userId;
    private MutableLiveData<Boolean> check;

    public OrderRepository(){
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userId = firebaseAuth.getUid();
        this.collectionReferenceOrder = db.collection("User").document(userId).collection("Order");
        this.collectionReferenceCart = db.collection("User").document(userId).collection("Cart");
        this.collectionReferenceProduct = db.collection("Product");
        this.check = new MutableLiveData<>();
    }
    public MutableLiveData<Boolean> getCheck() {
        return check;
    }

    public void addOrder(List<CartProduct> list, String address, int tatol){
        long timestamp = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("dateTime", FieldValue.serverTimestamp());
        hashMap.put("address", address);
        hashMap.put("listOrder", list);
        hashMap.put("tatol", tatol);
        hashMap.put("status", "Chờ xác nhận");

        collectionReferenceOrder.document(String.valueOf(timestamp)).set(hashMap)
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
    public void deleteProductInCart(List<CartProduct> list){
        for (CartProduct item : list){
            collectionReferenceCart.document(item.getVersion().getId())
                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        }
    }
    public void updateQuantityProduct(List<CartProduct> list){
        for (CartProduct item : list){
            int quantity = item.getQuantity();
            collectionReferenceProduct.document(item.getVersion().getId())
                    .update("quantity", FieldValue.increment(- quantity))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        }
    }
}
