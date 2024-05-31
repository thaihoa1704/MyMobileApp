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
import com.example.myapplication.Dialog.LogoutDialog;
import com.example.myapplication.Listener.OnClickChoice;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartViewModel;
import com.example.myapplication.ViewModels.OrderViewModel;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentUserBinding;

import java.util.List;

public class UserFragment extends Fragment {
    private UserViewModel userViewModel;
    private CartViewModel cartViewModel;
    private OrderViewModel orderViewModel;
    private FragmentUserBinding binding;
    private NavController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = Navigation.findNavController(view);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        cartViewModel.selectNoneAllProduct();

        if (userViewModel.getCurrentUser() != null){
            userViewModel.userLogged();
            userViewModel.getUserLogin().observe(getViewLifecycleOwner(), new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null){
                        binding.tvUserName.setText(user.getName());
                    }
                }
            });
        }

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
                controller.navigate(R.id.action_userFragment_to_purchaseHistoryFragment);
            }
        });

        binding.tvProfileUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.navigate(R.id.action_userFragment_to_profileFragment);
            }
        });

        binding.tvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("fragmentName", "informationUser");
                controller.navigate(R.id.action_userFragment_to_addressFragment, bundle);
            }
        });

        binding.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutDialog logoutDialog = new LogoutDialog(new OnClickChoice() {
                    @Override
                    public void onClick(Boolean choice) {
                        if (choice){
                            userLogout();
                        }
                    }
                });
                logoutDialog.show(requireActivity().getSupportFragmentManager(), "LogoutDialog");
            }
        });
    }

    private void userLogout() {
        userViewModel.userLogout();
        userViewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loggedOut) {
                if (loggedOut) {
                    Toast.makeText(requireContext(), "Tài khoản đã đăng xuất!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(requireActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
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
        controller.navigate(R.id.action_userFragment_to_orderProcessFragment, bundle);
    }
}