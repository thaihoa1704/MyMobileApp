package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartViewModel;
import com.example.myapplication.databinding.FragmentCategoryBinding;

public class CategoryFragment extends Fragment {
    private FragmentCategoryBinding binding;
    private NavController controller;
    private CartViewModel cartViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartViewModel.selectNoneAllProduct();

        controller = Navigation.findNavController(view);

        binding.imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Điện thoại");
            }
        });
        binding.imgLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Laptop");
            }
        });
        binding.imgHeadphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Tai nghe");
            }
        });
        binding.imgWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Đồng hồ");
            }
        });
        binding.imgAccessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Phụ kiện");
            }
        });
    }

    private void moveToNewFragment(String nameCategory){
        Bundle bundle = new Bundle();
        String nameFragment = "categoryFragment";
        bundle.putString("StartFragment", nameFragment);
        bundle.putString("category", nameCategory);
        controller.navigate(R.id.action_categoryFragment_to_productListFragment, bundle);
    }
}