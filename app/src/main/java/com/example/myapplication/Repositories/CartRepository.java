package com.example.myapplication.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Listener.FireStoreCallbackCartProducts;
import com.example.myapplication.Models.CartProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private FirebaseFirestore db;
    private String userId;
    private MutableLiveData<Boolean> check;
    private CollectionReference collectionReferenceCart;
    private FireStoreCallbackCartProducts fireStoreCallbackCartProducts;
    public CartRepository(FireStoreCallbackCartProducts fireStoreCallbackCartProducts) {
        this.db = FirebaseFirestore.getInstance();
        this.userId = FirebaseAuth.getInstance().getUid();
        this.check = new MutableLiveData<>();
        this.collectionReferenceCart = db.collection("User").document(userId).collection("Cart");
        this.fireStoreCallbackCartProducts = fireStoreCallbackCartProducts;
    }
    public MutableLiveData<Boolean> getCheck() {
        return check;
    }
    public void getAllProductsInCart(){
        collectionReferenceCart.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    fireStoreCallbackCartProducts.onCallback(task.getResult().toObjects(CartProduct.class));
                }
            }
        });
    }
    public void incrementQuantityProductInCart(String documentId){
        collectionReferenceCart.document(documentId).update("quantity", FieldValue.increment(1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void decrementQuantityProductInCart(String documentId){
        collectionReferenceCart.document(documentId).update("quantity", FieldValue.increment(-1))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    public void deleteProductInCart(String documentId){
        collectionReferenceCart.document(documentId).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    public void selectProduct(String documentId, boolean aBoolean) {
        collectionReferenceCart.document(documentId)
                .update("select", aBoolean)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void selectNoneAllProduct() {
        collectionReferenceCart.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
            DocumentReference document = collectionReferenceCart.document(list.get(i));
            batch.update(document, "select", false);
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }

    public void getProductSelected() {
        collectionReferenceCart.whereEqualTo("select", true).get()
                 .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    fireStoreCallbackCartProducts.onCallback(task.getResult().toObjects(CartProduct.class));
                }
            }
        });
    }
}
