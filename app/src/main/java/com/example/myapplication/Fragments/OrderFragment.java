package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.OrderProductAdapter;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Product;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartProductViewModel;
import com.example.myapplication.databinding.FragmentOrderBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;
    private CartProductViewModel viewModel;
    private OrderProductAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User user = (User) getArguments().getSerializable("UserLogin");
        binding.tvUserName.setText(user.getUserName());
        binding.tvPhone.setText(user.getPhone());
        binding.tvAddress.setText(user.getAddress());

        viewModel = new ViewModelProvider(this).get(CartProductViewModel.class);
        adapter = new OrderProductAdapter();

        binding.rcvCartProductList.setHasFixedSize(true);
        binding.rcvCartProductList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvCartProductList.setAdapter(adapter);

        viewModel.getList();
        viewModel.getCartProductList().observe(getViewLifecycleOwner(), new Observer<List<CartProduct>>() {
            @Override
            public void onChanged(List<CartProduct> list) {
                List<CartProduct> orderList = new ArrayList<>();
                int price = 0;
                for (CartProduct item : list){
                    if (item.isSelect()){
                        orderList.add(item);

                        int b = item.getQuantity();
                        int a = item.getProduct().getPrice();
                        price += a * b;
                    }
                }
                binding.tvPriceProduct.setText(Convert.DinhDangTien(price) + "VND");
                int total = price + 50000;
                binding.tvTotal.setText(Convert.DinhDangTien(total) + "VND");
                binding.tvTotal1.setText(Convert.DinhDangTien(total) + "VND");

                adapter.setData(requireActivity(), orderList);
                adapter.notifyDataSetChanged();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToFragment();
            }
        });
    }
    private void backToFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }
}