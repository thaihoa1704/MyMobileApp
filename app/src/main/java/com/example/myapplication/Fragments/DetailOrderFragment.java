package com.example.myapplication.Fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.OrderProductAdapter;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.OrderViewModel;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentDetailOrderBinding;

public class DetailOrderFragment extends Fragment {
    private FragmentDetailOrderBinding binding;
    private OrderProductAdapter adapter;
    private UserViewModel userViewModel;
    private OrderViewModel orderViewModel;
    private NavController controller;
    private int id;

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
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        controller = Navigation.findNavController(view);

        Order order = (Order) getArguments().getSerializable("Order");
        String from = getArguments().getString("From");

        binding.tvContact.setText(order.getContact().toString());
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
                binding.tvLabel.setVisibility(View.VISIBLE);
                binding.tvLabel1.setVisibility(View.VISIBLE);
                binding.tvPrice.setVisibility(View.VISIBLE);
                binding.tvPriceShipping.setVisibility(View.VISIBLE);
                binding.constraintLayout2.setVisibility(View.GONE);
            }
        });

        String status = order.getStatus().toString();
        if (status.equals("Chờ xác nhận")){
            this.id = 1;
            binding.btnProcess.setText("Đơn hàng đang được xử lý");
        } else if (status.equals("Đơn hàng đang trên đường giao đến bạn")) {
            this.id = 2;
            binding.btnProcess.setText("Đã nhận được hàng");
        } else if (status.equals("Chưa đánh giá")){
            this.id = 3;
            binding.btnProcess.setText("Đánh giá");
        } else if (status.equals("Đã đánh giá")){
            binding.btnProcess.setText(status);
        }

        binding.btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.equals("Đơn hàng đang trên đường giao đến bạn")){

                    orderViewModel.updateReceiveOrder(order);
                } else if (status.equals("Chưa đánh giá")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Order", order);
                    bundle.putString("From", from);
                    controller.navigate(R.id.action_detailOrderFragment_to_rateOrderFragment, bundle);
                }
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from.equals("OrderProcessFragment")){
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    controller.navigate(R.id.action_detailOrderFragment_to_orderProcessFragment, bundle);
                } else if (from.equals("PurchaseHistoryFragment")){
                    controller.navigate(R.id.action_detailOrderFragment_to_purchaseHistoryFragment);
                }
            }
        });
    }
}