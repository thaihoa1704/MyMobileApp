package com.example.myapplication.Repositories;

import androidx.annotation.NonNull;

import com.example.myapplication.Listener.FireStoreCallbackBrandList;
import com.example.myapplication.Listener.FireStoreCallbackPriceList;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Price;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class CategoryRepository {
    private FirebaseFirestore db;
    private CollectionReference collectionCategory;
    private FireStoreCallbackBrandList fireStoreCallbackBrandList;
    private FireStoreCallbackPriceList fireStoreCallbackPriceList;
    public CategoryRepository(FireStoreCallbackBrandList fireStoreCallbackBrandList, FireStoreCallbackPriceList fireStoreCallbackPriceList){
        this.db = FirebaseFirestore.getInstance();
        this.collectionCategory = db.collection("Category");
        this.fireStoreCallbackBrandList = fireStoreCallbackBrandList;
        this.fireStoreCallbackPriceList = fireStoreCallbackPriceList;
    }
    public void getBrandList(String category){
        collectionCategory.document(category).collection("Brand")
                .orderBy("name")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackBrandList.onBrandListLoad(task.getResult().toObjects(Brand.class));
                        }
                    }
                });
    }
    public void getPriceList(String category){
        collectionCategory.document(category).collection("Price")
                .orderBy("min")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackPriceList.onPriceListLoad(task.getResult().toObjects(Price.class));
                        }
                    }
                });
    }
}
