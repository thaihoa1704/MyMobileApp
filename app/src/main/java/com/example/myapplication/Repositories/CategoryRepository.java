package com.example.myapplication.Repositories;

import androidx.annotation.NonNull;

import com.example.myapplication.Listener.FireStoreCallbackBrandList;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Category;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private FirebaseFirestore db;
    private CollectionReference collectionProduct;
    private FireStoreCallbackBrandList fireStoreCallbackBrandList;
    public CategoryRepository(FireStoreCallbackBrandList fireStoreCallbackBrandList){
        this.db = FirebaseFirestore.getInstance();
        this.collectionProduct = db.collection("Category");
        this.fireStoreCallbackBrandList = fireStoreCallbackBrandList;
    }
    public void getCategoryData(String category){
        collectionProduct.whereEqualTo("categoryName", category)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<Category> list = task.getResult().toObjects(Category.class);
                            List<Brand> list1 = new ArrayList<>();
                            for (Category category : list){
                                List<Brand> brandList = category.getBrandList();
                                for (Brand item : brandList){
                                    Brand brand = new Brand(item.getBrandName(), item.getImage(), false);
                                    list1.add(brand);
                                }
                            }
                            fireStoreCallbackBrandList.onBrandListLoad(list1);
                        }
                    }
                });
    }
}
