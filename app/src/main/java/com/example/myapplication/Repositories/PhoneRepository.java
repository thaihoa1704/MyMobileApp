package com.example.myapplication.Repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Listener.PhoneModel.FireStoreCallbackPhoneVersion;
import com.example.myapplication.Listener.PhoneModel.FireStorePhoneVersionList;
import com.example.myapplication.Models.CartPhone;
import com.example.myapplication.Models.ProductVersion.PhoneVersion;
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

import java.util.List;

public class PhoneRepository {
    private FirebaseFirestore db;
    private String userId;
    private CollectionReference collectionReferenceProduct;
    private CollectionReference collectionReferenceCart;
    private FireStoreCallbackPhoneVersion fireStoreCallbackPhoneVersion;
    private FireStorePhoneVersionList fireStorePhoneVersionList;
    private MutableLiveData<Boolean> check;

    public PhoneRepository(FireStorePhoneVersionList fireStorePhoneVersionList, FireStoreCallbackPhoneVersion fireStoreCallbackPhoneVersion) {
        this.db = FirebaseFirestore.getInstance();
        this.userId = FirebaseAuth.getInstance().getUid();
        this.collectionReferenceProduct = db.collection("Product");
        this.collectionReferenceCart = db.collection("User").document(userId).collection("Cart");
        this.fireStoreCallbackPhoneVersion = fireStoreCallbackPhoneVersion;
        this.fireStorePhoneVersionList = fireStorePhoneVersionList;
        this.check = new MutableLiveData<>();
    }
    public MutableLiveData<Boolean> getCheck() {
        return check;
    }

    public void getPhonePrice(String productId, String color, String ram, String storage){
        collectionReferenceProduct.document(productId).collection("Version")
                .whereEqualTo("color", color)
                .whereEqualTo("ram", ram)
                .whereEqualTo("storage", storage)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<PhoneVersion> list = task.getResult().toObjects(PhoneVersion.class);
                            if (!list.isEmpty()){
                                fireStoreCallbackPhoneVersion.onCallBackPhoneVersion(list.get(0));
                            }else {
                                fireStoreCallbackPhoneVersion.onCallBackPhoneVersion(null);
                            }
                        }
                    }
                });
    }
    public void getPhoneVersions(String productId){
        collectionReferenceProduct.document(productId).collection("Version")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            fireStorePhoneVersionList.onCallbackPhoneAttributeVersion(task.getResult().toObjects(PhoneVersion.class));
                        }
                    }
                });
    }
    public void checkProductInCart(CartPhone cartPhone){
        String documentId = cartPhone.getVersion().getId();
        collectionReferenceCart.document(documentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        incrementQuantityProductInCart(cartPhone);
                    }else {
                        addProductToCart(cartPhone);
                    }
                }else {
                    check.postValue(false);
                }
            }
        });
    }

    private void addProductToCart(CartPhone cartPhone) {
        String documentId = cartPhone.getVersion().getId();
        collectionReferenceCart.document(documentId).set(cartPhone).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    private void incrementQuantityProductInCart(CartPhone cartPhone) {
        String documentId = cartPhone.getVersion().getId();
        collectionReferenceCart.document(documentId).update("quantity", FieldValue.increment(1))
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
