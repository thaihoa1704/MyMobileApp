package com.example.myapplication.Fragments;

import android.graphics.Color;
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

        orderAdapter = new OrderAdapter(this);

        binding.rcvOrder.setHasFixedSize(true);
        binding.rcvOrder.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvOrder.setAdapter(orderAdapter);
    }
    private void confirm(){
        binding.tvConfirm.setTextColor(Color.parseColor("#FF5722"));
        binding.lineConfirm.setVisibility(View.VISIBLE);
        binding.tvShipping.setTextColor(Color.parseColor("#FF000000"));
        binding.lineShipping.setVisibility(View.GONE);
        binding.tvRate.setTextColor(Color.parseColor("#FF000000"));
        binding.lineRate.setVisibility(View.GONE);

        setConfirmOrderAdapter();
    }

    private void shipping(){
        binding.tvShipping.setTextColor(Color.parseColor("#FF5722"));
        binding.lineShipping.setVisibility(View.VISIBLE);
        binding.tvConfirm.setTextColor(Color.parseColor("#FF000000"));
        binding.lineConfirm.setVisibility(View.GONE);
        binding.tvRate.setTextColor(Color.parseColor("#FF000000"));
        binding.lineRate.setVisibility(View.GONE);

        setShippingOrderAdapter();
    }
    private void rate(){
        binding.tvRate.setTextColor(Color.parseColor("#FF5722"));
        binding.lineRate.setVisibility(View.VISIBLE);
        binding.tvConfirm.setTextColor(Color.parseColor("#FF000000"));
        binding.lineConfirm.setVisibility(View.GONE);
        binding.tvShipping.setTextColor(Color.parseColor("#FF000000"));
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

                    listMutableLiveData.removeObserver(this);
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

                    listMutableLiveData.removeObserver(this);
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

                    listMutableLiveData.removeObserver(this);
                }
            }
        });
    }
    private void addFragment(Fragment fragment, Order order){
        Bundle bundle = new Bundle();
        bundle.putSerializable("Order", order);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_order, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onClick(Order order) {
        addFragment(new DetailOrderFragment(), order);
    }
}