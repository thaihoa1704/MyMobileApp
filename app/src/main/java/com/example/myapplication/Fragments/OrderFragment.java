package com.example.myapplication.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.Adapters.OrderProductAdapter;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.Product;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartProductViewModel;
import com.example.myapplication.ViewModels.OrderViewModel;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentOrderBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;
    private UserViewModel userViewModel;
    private CartProductViewModel cartViewModel;
    private OrderViewModel orderViewModel;
    private OrderProductAdapter adapter;
    private NavController controller;
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

        controller = Navigation.findNavController(view);

        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        if(userViewModel.getCurrentUser() != null){
            userViewModel.userLogged();
            userViewModel.getUserLogin().observe(getViewLifecycleOwner(), new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    binding.tvUserName.setText(user.getUserName());
                    binding.tvPhone.setText(user.getPhone());
                    String address = user.getAddress();
                    if (address != ""){
                        binding.tvAddress.setText(address);
                        binding.linearLayoutAddAddress.setVisibility(View.INVISIBLE);
                    }else {
                        binding.tvAddress.setVisibility(View.INVISIBLE);
                        binding.linearLayoutAddAddress.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        cartViewModel = new ViewModelProvider(this).get(CartProductViewModel.class);

        adapter = new OrderProductAdapter();

        binding.rcvCartProductList.setHasFixedSize(true);
        binding.rcvCartProductList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvCartProductList.setAdapter(adapter);

        List<CartProduct> orderList = new ArrayList<>();

        cartViewModel.getList();
        cartViewModel.getCartProductList().observe(getViewLifecycleOwner(), new Observer<List<CartProduct>>() {
            @Override
            public void onChanged(List<CartProduct> list) {
                int price = 0;
                for (CartProduct item : list){
                    if (item.isSelect()){
                        orderList.add(item);

                        int b = item.getQuantity();
                        int a = item.getProduct().getPrice();
                        price += a * b;
                    }
                }
                binding.tvPriceProduct.setText(Convert.DinhDangTien(price) + " VND");
                int total = price + 50000;
                binding.tvTotal.setText(Convert.DinhDangTien(total) + " VND");
                binding.tvTotal1.setText(Convert.DinhDangTien(total) + " VND");

                adapter.setData(requireActivity(), orderList);
                adapter.notifyDataSetChanged();
            }
        });

        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tvAddress.getText() == null){
                    Toast.makeText(requireContext(), "Bạn chưa chọn địa điểm giao hàng!", Toast.LENGTH_SHORT).show();
                }else {
                    openDialog(Gravity.CENTER, orderList);
                }
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(R.id.action_orderFragment_to_cartFragment);
            }
        });
    }

    private void openDialog(int gravity, List<CartProduct> list){
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_layout);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.CENTER == gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }

        Button btnReturn = dialog.findViewById(R.id.btn_return);
        Button btnOrder = dialog.findViewById(R.id.btn_order_dialog);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                controller.navigate(R.id.action_orderFragment_to_handleOrderFragment);

                String address = binding.tvAddress.getText().toString().trim();
                int tatol = Convert.ChuyenTien(binding.tvTotal.getText().toString().trim());

                orderViewModel.addOrder(list, address, tatol);
                orderViewModel.deleteProductInCart(list);
                orderViewModel.updateQuantityProduct(list);
            }
        });
        dialog.show();
    }
}