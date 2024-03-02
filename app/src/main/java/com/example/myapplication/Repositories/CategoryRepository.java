package com.example.myapplication.Repositories;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Listener.FireStoreCallbackBrandList;
import com.example.myapplication.Listener.FireStoreCallbackCategory;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Category;
import com.example.myapplication.Models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private FirebaseFirestore db;
    private CollectionReference collectionProduct;
    private FireStoreCallbackCategory fireStoreCallbackCategory;
    private FireStoreCallbackBrandList fireStoreCallbackBrandList;
    public CategoryRepository(FireStoreCallbackCategory fireStoreCallbackCategory, FireStoreCallbackBrandList fireStoreCallbackBrandList){
        this.db = FirebaseFirestore.getInstance();
        this.collectionProduct = db.collection("Category");
        this.fireStoreCallbackCategory = fireStoreCallbackCategory;
        this.fireStoreCallbackBrandList = fireStoreCallbackBrandList;
    }
    public void getCategoryData(String category){
        collectionProduct.whereEqualTo("categoryName", category)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<Category> list = task.getResult().toObjects(Category.class);
                            for (Category category : list){
                                fireStoreCallbackBrandList.onBrandListLoad(category.getBrandList());
                            }
                        }
                    }
                });
    }
}
