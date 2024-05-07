package com.example.myapplication.Listener;

import com.example.myapplication.Models.Address;

import java.util.List;

public interface FireStoreCallbackAddress {
    void onCallback(List<Address> addressList);
}
