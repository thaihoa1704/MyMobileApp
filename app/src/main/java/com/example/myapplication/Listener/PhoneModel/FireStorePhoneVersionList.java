package com.example.myapplication.Listener.PhoneModel;

import com.example.myapplication.Models.ProductVersion.PhoneVersion;

import java.util.List;

public interface FireStorePhoneVersionList {
    void onCallbackPhoneAttributeVersion(List<PhoneVersion> phoneVersionList);
}
