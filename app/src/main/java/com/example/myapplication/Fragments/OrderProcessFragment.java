package com.example.myapplication.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.OrderAdapter;
import com.example.myapplication.Listener.ClickItemOrderListener;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.Product;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.OrderViewModel;
import com.example.myapplication.databinding.FragmentOrderBinding;
import com.example.myapplication.databinding.FragmentOrderProcessBinding;

import java.util.List;

public class OrderProcessFragment extends Fragment implements ClickItemOrderListener {
    private FragmentOrderProcessBinding binding;
    private NavController controller;
    private OrderAdapter orderAdapter;
    private OrderViewModel orderViewModel;
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
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        int id = 0;
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
        if (id == 1){
            confirm();
        } else if (id == 2) {
            shipping();
        } else if (id == 3){
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
                controller.popBackStack();
            }
        });

        orderAdapter = new OrderAdapter(this);

        binding.rcvOrder.setHasFixedSize(true);
        binding.rcvOrder.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvOrder.setAdapter(orderAdapter);
    }
    private void confirm(){
        binding.tvConfirm.setTextColor(Color.parseColor("#FF5722"));
        binding.tvConfirm.setTypeface(Typeface.DEFAULT_BOLD);
        binding.lineConfirm.setVisibility(View.VISIBLE);
        binding.tvShipping.setTextColor(Color.parseColor("#FF000000"));
        binding.tvShipping.setTypeface(Typeface.DEFAULT);
        binding.lineShipping.setVisibility(View.GONE);
        binding.tvRate.setTextColor(Color.parseColor("#FF000000"));
        binding.tvRate.setTypeface(Typeface.DEFAULT);
        binding.lineRate.setVisibility(View.GONE);

        setConfirmOrderAdapter();
    }

    private void shipping(){
        binding.tvShipping.setTextColor(Color.parseColor("#FF5722"));
        binding.tvShipping.setTypeface(Typeface.DEFAULT_BOLD);
        binding.lineShipping.setVisibility(View.VISIBLE);
        binding.tvConfirm.setTextColor(Color.parseColor("#FF000000"));
        binding.tvConfirm.setTypeface(Typeface.DEFAULT);
        binding.lineConfirm.setVisibility(View.GONE);
        binding.tvRate.setTextColor(Color.parseColor("#FF000000"));
        binding.tvRate.setTypeface(Typeface.DEFAULT);
        binding.lineRate.setVisibility(View.GONE);

        setShippingOrderAdapter();
    }
    private void rate(){
        binding.tvRate.setTextColor(Color.parseColor("#FF5722"));
        binding.tvRate.setTypeface(Typeface.DEFAULT_BOLD);
        binding.lineRate.setVisibility(View.VISIBLE);
        binding.tvConfirm.setTextColor(Color.parseColor("#FF000000"));
        binding.tvConfirm.setTypeface(Typeface.DEFAULT);
        binding.lineConfirm.setVisibility(View.GONE);
        binding.tvShipping.setTextColor(Color.parseColor("#FF000000"));
        binding.tvShipping.setTypeface(Typeface.DEFAULT);
        binding.lineShipping.setVisibility(View.GONE);

        setRateOrderAdapter();
    }
    private void setConfirmOrderAdapter() {
        orderViewModel.getConfirm();
        MutableLiveData<List<Order>> listMutableLiveData = orderViewModel.getConfirmOrder();
        listMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> list) {
                if (list != null){
                    orderAdapter.setData(requireActivity(), list);
                    orderAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void setShippingOrderAdapter() {
        orderViewModel.getShipping();
        MutableLiveData<List<Order>> listMutableLiveData = orderViewModel.getShippingOrder();
        listMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> list) {
                if (list != null){
                    orderAdapter.setData(requireActivity(), list);
                    orderAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void setRateOrderAdapter() {
        orderViewModel.getRate();
        MutableLiveData<List<Order>> listMutableLiveData = orderViewModel.getRateOrder();
        listMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> list) {
                if (list != null){
                    orderAdapter.setData(requireActivity(), list);
                    orderAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    @Override
    public void onClick(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Order", order);
        bundle.putString("From", "OrderProcessFragment");
        controller.navigate(R.id.action_orderProcessFragment_to_detailOrderFragment, bundle);
    }
}