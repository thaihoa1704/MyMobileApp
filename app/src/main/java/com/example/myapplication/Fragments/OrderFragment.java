package com.example.myapplication.Fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.myapplication.Models.Address;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartViewModel;
import com.example.myapplication.ViewModels.OrderViewModel;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.DialogOrderLayoutBinding;
import com.example.myapplication.databinding.FragmentOrderBinding;
import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;
    private DialogOrderLayoutBinding dialogBinding;
    private UserViewModel userViewModel;
    private CartViewModel cartViewModel;
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

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        setUserInfo();
        setCartProductAdapter();

        binding.imgChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                String fragmentName = "orderFragment";
                bundle.putString("fragmentName", fragmentName);
                controller.navigate(R.id.action_orderFragment_to_addressFragment, bundle);
            }
        });

        binding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tvAddress.getText() == null){
                    Toast.makeText(requireContext(), "Bạn chưa chọn địa điểm giao hàng!", Toast.LENGTH_SHORT).show();
                }else {
                    cartViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<CartProduct>>() {
                        @Override
                        public void onChanged(List<CartProduct> list) {
                            openDialog(Gravity.CENTER, list);
                        }
                    });
                }
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.popBackStack();
            }
        });
    }

    private void setUserInfo() {
        userViewModel.userLogged();
        userViewModel.getUserLogin().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.tvUserName.setText(user.getName());
                binding.tvPhone.setText(user.getPhone());
            }
        });

        userViewModel.getAddress();
        userViewModel.getAddressMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addressList) {
                for (Address item : addressList){
                    if (item.isSelect()){
                        String string = item.getString();
                        if (string != ""){
                            binding.tvAddress.setText(string);
                            binding.imgChangeAddress.setVisibility(View.VISIBLE);
                            binding.imgAddAddress.setVisibility(View.GONE);
                            binding.tvAddAddress.setVisibility(View.GONE);
                        }else {
                            binding.tvAddress.setVisibility(View.GONE);
                            binding.imgChangeAddress.setVisibility(View.GONE);
                            binding.imgAddAddress.setVisibility(View.VISIBLE);
                            binding.tvAddAddress.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    private void setCartProductAdapter() {
        adapter = new OrderProductAdapter();

        binding.rcvCart.setHasFixedSize(true);
        binding.rcvCart.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvCart.setAdapter(adapter);

        cartViewModel.getProductSelected();
        cartViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<CartProduct>>() {
            @Override
            public void onChanged(List<CartProduct> list) {
                adapter.setData(requireActivity(), list);
                adapter.notifyDataSetChanged();

                int price = 0;
                for (CartProduct item : list){
                    int a = item.getQuantity();
                    int b = item.getVersion().getPrice();
                    price += a * b;
                }
                binding.tvPriceProduct.setText(Convert.DinhDangTien(price) + " đ");
                int total = price + 50;
                binding.tvTotal.setText(Convert.DinhDangTien(total) + " đ");
                binding.tvTotal1.setText(Convert.DinhDangTien(total) + " đ");
            }
        });
    }

    private void openDialog(int gravity, List<CartProduct> list){
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBinding = DialogOrderLayoutBinding.inflate(LayoutInflater.from(requireContext()));
        dialog.setContentView(dialogBinding.getRoot());

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

        dialogBinding.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogBinding.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                controller.navigate(R.id.action_orderFragment_to_handleOrderFragment);

                String contact = binding.tvUserName.getText().toString().trim() + " - " + binding.tvPhone.getText().toString().trim();
                String address = binding.tvAddress.getText().toString().trim();
                int tatol = Convert.ChuyenTien(binding.tvTotal.getText().toString().trim()) / 1000;

                orderViewModel.addOrder(list, contact, address, tatol);
                orderViewModel.deleteProductInCart(list);
                orderViewModel.updateQuantityProduct(list);
            }
        });
        dialog.show();
    }
}