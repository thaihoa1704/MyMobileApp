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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

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

    public void selectProduct(CartProduct cartProduct, boolean select){
        collectionReference.document(cartProduct.getProduct().getProductId())
                .update("select", select)
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

    public void getListSelected(){
        collectionReference.whereEqualTo("select", true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCartProductList.onCallbackListSelected(task.getResult().toObjects(CartProduct.class));
                        }
                    }
                });
    }
    public void selectNoneAllProduct(){
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                        list.add(queryDocumentSnapshot.getId());
                    }
                    updateData(list);
                }else {

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
