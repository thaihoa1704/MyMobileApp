package com.example.myapplication.Listener;

import com.example.myapplication.Models.Brand;
import java.util.List;

public interface FireStoreCallbackBrandList {
    void onBrandListLoad(List<Brand> list);
}
