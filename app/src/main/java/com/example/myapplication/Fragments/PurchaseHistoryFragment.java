package com.example.myapplication.Fragments;

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
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.OrderViewModel;
import com.example.myapplication.databinding.FragmentPurchaseHistoryBinding;

import java.util.List;

public class PurchaseHistoryFragment extends Fragment implements ClickItemOrderListener {
    private NavController controller;
    private FragmentPurchaseHistoryBinding binding;
    private OrderViewModel orderViewModel;
    private OrderAdapter orderAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPurchaseHistoryBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        controller = Navigation.findNavController(view);

        orderAdapter = new OrderAdapter(this);

        binding.rcvOrder.setHasFixedSize(true);
        binding.rcvOrder.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvOrder.setAdapter(orderAdapter);

        setOrderAdapter();
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.popBackStack();
            }
        });
    }

    @Override
    public void onClick(Order order) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Order", order);
        bundle.putString("From", "PurchaseHistoryFragment");
        controller.navigate(R.id.action_purchaseHistoryFragment_to_detailOrderFragment, bundle);
    }
    private void setOrderAdapter() {
        orderViewModel.getPurchaseHistory();
        orderViewModel.getPurchaseHistoryLiveData().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> list) {
                if (list != null){
                    orderAdapter.setData(requireActivity(), list);
                    orderAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}