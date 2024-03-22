package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Activities.AdminActivity;
import com.example.myapplication.Activities.ShoppingActivity;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartProductViewModel;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentShomeBinding;

public class SHomeFragment extends Fragment {
    private UserViewModel viewModel;
    private CartProductViewModel cartProductViewModel;
    private FragmentShomeBinding binding;
    private NavController controller;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShomeBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = Navigation.findNavController(view);

        cartProductViewModel = new ViewModelProvider(this).get(CartProductViewModel.class);
        cartProductViewModel.selectNoneAllProduct();

        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        if(viewModel.getCurrentUser() != null){
            viewModel.userLogged();
            viewModel.getUserLogin().observe(getViewLifecycleOwner(), new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    String name = user.getUserName();
                    binding.txtName.setText(name);
                }
            });
        }

        binding.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(R.id.action_SHomeFragment_to_searchFragment);
            }
        });

        binding.imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Điện thoại");
            }
        });
        binding.imgHeadPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Tai nghe");
            }
        });
        binding.imgLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Laptop");
            }
        });
        binding.imgWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Đồng hồ");
            }
        });
        binding.imageCable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Phụ kiện");
            }
        });
    }

    private void moveToNewFragment(String nameCategory){
        Bundle bundle = new Bundle();
        String nameFragment = "homeFragment";
        bundle.putString("StartFragment", nameFragment);
        bundle.putString("category", nameCategory);
        controller.navigate(R.id.action_SHomeFragment_to_productListFragment, bundle);
    }
    private void addFragment(Fragment fragment, String category){
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_shome, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }
}