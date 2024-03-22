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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapters.CartProductAdapter;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Helper.SwipeItem;
import com.example.myapplication.Helper.SwipeToDeleteItem;
import com.example.myapplication.Listener.ChangeQuantityListener;
import com.example.myapplication.Listener.ChangeSelectProductListener;
import com.example.myapplication.Listener.ClickDeleteItem;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Listener.MyButtonClickListener;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Product;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartProductViewModel;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentCartBinding;

import java.util.List;


public class CartFragment extends Fragment implements ClickItemProductListener, ChangeQuantityListener
        , ChangeSelectProductListener, ClickDeleteItem {
    private FragmentCartBinding binding;
    private CartProductAdapter adapter;
    private CartProductViewModel viewModel;
    private SwipeToDeleteItem swipe;
    private NavController controller;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = Navigation.findNavController(view);

        viewModel = new ViewModelProvider(this).get(CartProductViewModel.class);

        adapter = new CartProductAdapter(this, this, this, this);

        binding.rcvCartProductList.setHasFixedSize(true);
        binding.rcvCartProductList.setLayoutManager(new LinearLayoutManager(requireActivity()));

        binding.rcvCartProductList.setAdapter(adapter);

        setDataAdapter();

        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tvTotal.getText().equals("0 VND")){
                    Toast.makeText(requireContext(), "Chọn sản phẩm bạn muốn thanh toán!", Toast.LENGTH_SHORT).show();
                }else {
                    controller.navigate(R.id.action_cartFragment_to_orderFragment);
                }
            }
        });
    }

    @Override
    public void onClickItemProduct(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ProductModel", product);
        String nameFragment = "cartFragment";
        bundle.putString("StartFragment", nameFragment);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_cartFragment_to_detailProductFragment, bundle);
    }

    @Override
    public void deleteProduct(CartProduct cartProductt) {
        viewModel.deleteProduct(cartProductt);
        setDataAdapter();
    }

    @Override
    public void incrementQuantity(CartProduct cartProduct) {
        viewModel.incrementQuantity(cartProduct);
        setDataAdapter();
    }

    @Override
    public void decrementQuantity(CartProduct cartProduct) {
        viewModel.decrementQuantity(cartProduct);
        setDataAdapter();
    }

    @Override
    public void onChangeSelect(CartProduct cartProduct, boolean aBoolean) {
        viewModel.selectProduct(cartProduct, aBoolean);
        setDataAdapter();
    }

    private void setDataAdapter(){
        viewModel.getList();
        viewModel.getCartProductList().observe(getViewLifecycleOwner(), new Observer<List<CartProduct>>() {
            @Override
            public void onChanged(List<CartProduct> list) {
                adapter.setData(requireActivity(), list);
                adapter.notifyDataSetChanged();
                if (list.isEmpty()){
                    binding.tvTotal.setText("0 VND");
                }else {
                    int total = 0;
                    for (CartProduct item : list){
                        if (item.isSelect()){
                            int b = item.getQuantity();
                            int a = item.getProduct().getPrice();
                            total += a * b;
                        }
                    }
                    binding.tvTotal.setText(Convert.DinhDangTien(total) + " VND");
                }
            }
        });
    }

    @Override
    public void onDeleteItem(CartProduct cartProduct) {
        viewModel.deleteProduct(cartProduct);
        setDataAdapter();
    }
}