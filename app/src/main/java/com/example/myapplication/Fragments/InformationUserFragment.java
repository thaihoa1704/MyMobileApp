package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Models.Order;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartViewModel;
import com.example.myapplication.ViewModels.OrderViewModel;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentInformationUserBinding;

import java.util.List;

public class InformationUserFragment extends Fragment {
    private UserViewModel viewModel;
    private CartViewModel cartViewModel;
    private OrderViewModel orderViewModel;
    private FragmentInformationUserBinding binding;
    private NavController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInformationUserBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        cartViewModel.selectNoneAllProduct();

        setQuantityConfirmOrder();
        setQuantityShippingOrder();
        setQuantityRateOrder();

        binding.imgConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNewFragment(1);
            }
        });
        binding.imgShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNewFragment(2);
            }
        });
        binding.imgRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNewFragment(3);
            }
        });

        binding.tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.action_informationUserFragment_to_purchaseHistoryFragment);
            }
        });

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.userLogout();
                viewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean loggedOut) {
                        if (loggedOut) {
                            Toast.makeText(getContext(), "Tài khoản đã đăng xuất!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(requireActivity(), MainActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    }
                });
            }
        });
    }

    private void setQuantityConfirmOrder() {
        orderViewModel.getConfirm();
        orderViewModel.getConfirmOrder().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> list) {
                int quantity = list.size();
                if (quantity != 0){
                    binding.tvQuantityConfirm.setText(String.valueOf(quantity));
                    binding.cvConfirm.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void setQuantityShippingOrder() {
        orderViewModel.getShipping();
        orderViewModel.getShippingOrder().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> list) {
                int quantity = list.size();
                if (quantity != 0){
                    binding.tvQuantityShipping.setText(String.valueOf(quantity));
                    binding.cvShipping.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void setQuantityRateOrder() {
        orderViewModel.getRate();
        orderViewModel.getRateOrder().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> list) {
                int quantity = list.size();
                if (quantity != 0){
                    binding.tvQuantityRate.setText(String.valueOf(quantity));
                    binding.cvRate.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void moveToNewFragment(int id){
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        controller.navigate(R.id.action_informationUserFragment_to_orderProcessFragment, bundle);
    }
}