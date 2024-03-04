package com.example.myapplication.Repositories;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.myapplication.Listener.FireStoreCallbackProductList;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

public class ProductListRepository {
    private Application application;
    private FirebaseFirestore db;
    private CollectionReference collectionProduct;
    private FireStoreCallbackProductList fireStoreCallbackListProduct;
    public ProductListRepository(FireStoreCallbackProductList fireStoreCallbackListProduct){
        this.fireStoreCallbackListProduct = fireStoreCallbackListProduct;
        this.application = new Application();
        this.db = FirebaseFirestore.getInstance();
        this.collectionProduct = db.collection("Product");
    }
    public void getProductData(String category){
        collectionProduct.whereEqualTo("category", category)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                        }else {
                            Toast.makeText(application.getApplicationContext(), "Lỗi hiển thị dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
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
                        }else {
                            Toast.makeText(application.getApplicationContext(), "Lỗi hiển thị dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void orderByPriceDescending(String category){
        collectionProduct.whereEqualTo("category", category).orderBy("price", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                        }else {
                            Toast.makeText(application.getApplicationContext(), "Lỗi hiển thị dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void orderByPriceAscending(String category) {
        collectionProduct.whereEqualTo("category", category).orderBy("price")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                        }else {
                            Toast.makeText(application.getApplicationContext(), "Lỗi hiển thị dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void getProductData(String category, Brand brand){
        collectionProduct.whereEqualTo("category", category).whereEqualTo("brand", brand.getBrandName())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStoreCallbackListProduct.onProductListLoad(task.getResult().toObjects(Product.class));
                        }else {
                            Toast.makeText(application.getApplicationContext(), "Lỗi hiển thị dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
