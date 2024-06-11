package com.example.myapplication.Repositories;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.myapplication.Listener.FireStoreCallbackProductList;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Price;
import com.example.myapplication.Models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.List;

public class ProductListRepository {
    private FirebaseFirestore db;
    private CollectionReference collectionProduct;
    private CollectionReference collectionCategory;

    private FireStoreCallbackProductList fireStoreCallbackListProduct;
    public ProductListRepository(FireStoreCallbackProductList fireStoreCallbackListProduct){
        this.fireStoreCallbackListProduct = fireStoreCallbackListProduct;
        this.db = FirebaseFirestore.getInstance();
        this.collectionProduct = db.collection("Product");
        this.collectionCategory = db.collection("Category");
    }
    public void getProductData(String category){
        collectionProduct.whereEqualTo("category", category)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                        }
                    }
                });
    }
    public void getAllProductData(){
        collectionProduct.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                        }
                    }
                });
    }

    public void getProductListWithBrand(String category, Brand brand){
        collectionProduct.whereEqualTo("category", category).whereEqualTo("brand", brand.getName())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                        }
                    }
                });
    }
    public void getProductListWithPrice(String category, Price price){
        Query query = collectionProduct.whereEqualTo("category", category)
                                        .whereGreaterThanOrEqualTo("price", price.getMin())
                                        .whereLessThanOrEqualTo("price", price.getMax());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                }
            }
        });
    }
    public void getProductListWithBrandAndPrice(String category, Brand brand, Price price){
        Query query = collectionProduct.whereEqualTo("category", category)
                .whereEqualTo("brand", brand.getName())
                .whereGreaterThanOrEqualTo("price", price.getMin())
                .whereLessThanOrEqualTo("price", price.getMax());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                }
            }
        });
    }
    public void getSpecialProductList(){
        collectionProduct.whereEqualTo("special", true)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                        }
                    }
                });
    }
}
