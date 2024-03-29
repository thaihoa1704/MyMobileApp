package com.example.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Product;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartProductViewModel;
import com.example.myapplication.databinding.FragmentDetailProductBinding;

public class DetailProductFragment extends Fragment {
    private FragmentDetailProductBinding binding;
    private CartProductViewModel viewModel;
    private NavController controller;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailProductBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CartProductViewModel.class);
        controller = Navigation.findNavController(view);

        Product product = (Product) getArguments().getSerializable("ProductModel");
        String startFragment = getArguments().getString("StartFragment");

        Glide.with(getContext()).load(product.getImage()).into(binding.imgProduct);
        binding.tvProductName.setText(product.getProductName());
        binding.tvPrice.setText(Convert.DinhDangTien(product.getPrice()) + " VND");
        binding.tvDescription.setText(product.getDescription());

        if (product.getQuantity() == 0){
            binding.btnAdd.setText("Hết hàng");
            binding.btnAdd.setEnabled(false);
        }

        CartProduct cartProduct = new CartProduct(product, 1, false);

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.checkProductInCart(cartProduct);
                MutableLiveData<Boolean> check = viewModel.getCheckCartProduct();
                check.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean){
                            Toast.makeText(requireContext(), "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                            check.removeObserver(this);
                        }else {
                            Toast.makeText(requireContext(), "Không thêm vào được giỏ hàng!", Toast.LENGTH_SHORT).show();
                            check.removeObserver(this);
                        }
                    }
                });
            }
        });
        
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startFragment == "searchFragment"){
                    controller.navigate(R.id.action_detailProductFragment_to_searchFragment);
                }else if (startFragment == "productListFragment"){
                    removeFragment();
                }else {
                    controller.navigate(R.id.action_detailProductFragment_to_cartFragment);
                }
            }
        });
    }
    private void removeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
    private void safelyNavigate(NavController navController, int resid) {
        try {
            navController.navigate(resid);
        } catch (Exception e) {

        }
    }
}