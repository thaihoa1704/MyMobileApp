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
import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.User;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentDetailOrderBinding;

public class DetailOrderFragment extends Fragment {
    private FragmentDetailOrderBinding binding;
    private OrderProductAdapter adapter;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailOrderBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.userLogged();
        userViewModel.getUserLogin().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.tvUserName.setText(user.getName());
                binding.tvPhone.setText(user.getPhone());
            }
        });

        Order order = (Order) getArguments().getSerializable("Order");

        binding.tvUserName.setText("");
        binding.tvAddress.setText(order.getAddress().toString());

        binding.tvTime.setText(Convert.getDateTime(order.getDateTime()));

        adapter = new OrderProductAdapter();
        binding.rcvOrder.setHasFixedSize(true);
        binding.rcvOrder.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvOrder.setAdapter(adapter);
        adapter.setData(requireActivity(), order.getListProduct());
        adapter.notifyDataSetChanged();

        int price = 0;
        for (CartProduct item : order.getListProduct()){
            price += item.getQuantity() * item.getVersion().getPrice();
        }
        binding.tvPrice.setText(Convert.DinhDangTien(price) + " đ");

        binding.tvTotal.setText(Convert.DinhDangTien(order.getTotal()) + " đ");

        binding.imgDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.constraintLayout.setVisibility(View.VISIBLE);
                binding.imgDropDown.setVisibility(View.GONE);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
            }
        });
    }
    private void removeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }
}