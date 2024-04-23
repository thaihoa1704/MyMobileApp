package com.example.myapplication.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentOrderBinding;
import com.example.myapplication.databinding.FragmentOrderProcessBinding;

public class OrderProcessFragment extends Fragment {
    private FragmentOrderProcessBinding binding;
    private NavController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderProcessBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        int id = getArguments().getInt("id");
        if (id == 1){
            confirm();
        } else if (id == 2) {
            shipping();
        } else {
            rate();
        }
        binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        binding.tvShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shipping();
            }
        });
        binding.tvRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.action_orderProcessFragment_to_profileUserFragment);
            }
        });
    }
    private void confirm(){
        binding.tvConfirm.setTextColor(Color.parseColor("#FF5722"));
        binding.lineConfirm.setVisibility(View.VISIBLE);
        binding.tvShipping.setTextColor(Color.parseColor("#FF000000"));
        binding.lineShipping.setVisibility(View.GONE);
        binding.tvRate.setTextColor(Color.parseColor("#FF000000"));
        binding.lineRate.setVisibility(View.GONE);
    }
    private void shipping(){
        binding.tvShipping.setTextColor(Color.parseColor("#FF5722"));
        binding.lineShipping.setVisibility(View.VISIBLE);
        binding.tvConfirm.setTextColor(Color.parseColor("#FF000000"));
        binding.lineConfirm.setVisibility(View.GONE);
        binding.tvRate.setTextColor(Color.parseColor("#FF000000"));
        binding.lineRate.setVisibility(View.GONE);
    }
    private void rate(){
        binding.tvRate.setTextColor(Color.parseColor("#FF5722"));
        binding.lineRate.setVisibility(View.VISIBLE);
        binding.tvConfirm.setTextColor(Color.parseColor("#FF000000"));
        binding.lineConfirm.setVisibility(View.GONE);
        binding.tvShipping.setTextColor(Color.parseColor("#FF000000"));
        binding.lineShipping.setVisibility(View.GONE);
    }
}