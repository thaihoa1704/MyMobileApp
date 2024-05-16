package com.example.myapplication.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Listener.FireStoreCallbackCartProducts;
import com.example.myapplication.Listener.FireStoreCallbackConfirmOrder;
import com.example.myapplication.Listener.FireStoreCallbackPurchaseHistory;
import com.example.myapplication.Listener.FireStoreCallbackRateOrder;
import com.example.myapplication.Listener.FireStoreCallbackShippingOrder;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
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
    private FireStoreCallbackConfirmOrder fireStoreCallbackConfirmOrder;
    private FireStoreCallbackShippingOrder fireStoreCallbackShippingOrder;
    private FireStoreCallbackRateOrder fireStoreCallbackRateOrder;
    private FireStoreCallbackPurchaseHistory fireStoreCallbackPurchaseHistory;

    public OrderRepository(FireStoreCallbackConfirmOrder fireStoreCallbackConfirmOrder,
                           FireStoreCallbackShippingOrder fireStoreCallbackShippingOrder,
                           FireStoreCallbackRateOrder fireStoreCallbackRateOrder,
                           FireStoreCallbackPurchaseHistory fireStoreCallbackPurchaseHistory){
        this.db = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userId = firebaseAuth.getUid();
        this.collectionReferenceOrder = db.collection("User").document(userId).collection("Order");
        this.collectionReferenceCart = db.collection("User").document(userId).collection("Cart");
        this.collectionReferenceProduct = db.collection("Product");
        this.check = new MutableLiveData<>();
        this.fireStoreCallbackConfirmOrder = fireStoreCallbackConfirmOrder;
        this.fireStoreCallbackShippingOrder = fireStoreCallbackShippingOrder;
        this.fireStoreCallbackRateOrder = fireStoreCallbackRateOrder;
        this.fireStoreCallbackPurchaseHistory = fireStoreCallbackPurchaseHistory;
    }
    public MutableLiveData<Boolean> getCheck() {
        return check;
    }

    public void addOrder(List<CartProduct> list, String address, int total){
        long timestamp = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("dateTime", timestamp);
        hashMap.put("address", address);
        hashMap.put("listProduct", list);
        hashMap.put("total", total);
        hashMap.put("status", "Chờ xác nhận");
        hashMap.put("rateStar", 0);
        hashMap.put("note", "");

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
    public void getConfirmOrder(){
        collectionReferenceOrder.whereEqualTo("status", "Chờ xác nhận")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackConfirmOrder.onCallbackC(task.getResult().toObjects(Order.class));
                        }
                    }
                });
    }
    public void getShippingOrder(){
        collectionReferenceOrder.whereEqualTo("status", "Đơn hàng đang trên đường giao đến bạn")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackShippingOrder.onCallbackS(task.getResult().toObjects(Order.class));
                        }
                    }
                });
    }
    public void getRateOrder(){
        collectionReferenceOrder.whereEqualTo("status", "Chưa đánh giá")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackRateOrder.onCallbackR(task.getResult().toObjects(Order.class));
                        }
                    }
                });
    }
    public void getPurchaseHistory(){
        collectionReferenceOrder.whereIn("status", Arrays.asList("Chưa đánh giá", "Đã đánh giá"))
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackPurchaseHistory.onCallback(task.getResult().toObjects(Order.class));
                        }
                    }
                });
    }
    public void updateReceiveOrder(Order order){
        collectionReferenceOrder.document(String.valueOf(order.getDateTime()))
                .update("status", "Chưa đánh giá")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }
    public void updateRateOrder(Order order, int star, String note){
        collectionReferenceOrder.document(String.valueOf(order.getDateTime()))
                .update("rateStar", star
                        , "note", note
                        , "status", "Đã đánh giá")
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
}
