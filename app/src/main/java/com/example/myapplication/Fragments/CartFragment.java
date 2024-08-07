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
import android.widget.Toast;

import com.example.myapplication.Adapters.CartAdapter;
import com.example.myapplication.Dialog.ChoiceDialog;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Listener.ChangeQuantityCartProduct;
import com.example.myapplication.Listener.ChangeSelectProductListener;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Listener.OnClickChoice;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Product;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartViewModel;
import com.example.myapplication.databinding.FragmentCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements ChangeQuantityCartProduct, ChangeSelectProductListener, ClickItemProductListener {
    private FragmentCartBinding binding;
    private CartAdapter cartAdapter;
    private CartViewModel cartViewModel;
    private NavController controller;
    private List<CartProduct> list = new ArrayList<>();
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
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartAdapter = new CartAdapter(this, this, this);

        binding.rcvCartProductList.setHasFixedSize(true);
        binding.rcvCartProductList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvCartProductList.setAdapter(cartAdapter);

        setCartAdapter();

        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tvEmpty.getVisibility() == View.VISIBLE){
                    Toast.makeText(requireContext(), "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                } else if (binding.tvTotal.getText().equals("0 đ")){
                    Toast.makeText(requireContext(), "Chọn sản phẩm bạn muốn thanh toán!", Toast.LENGTH_SHORT).show();
                } else {
                    controller.navigate(R.id.action_cartFragment_to_orderFragment);
                }
            }
        });
        binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoiceDialog deleteDialog = new ChoiceDialog("CartFragment", new OnClickChoice() {
                    @Override
                    public void onClick(Boolean choice) {
                        if (choice){
                            delete();
                            cartViewModel.getAllProductsInCart();
                        }
                    }
                });
                deleteDialog.show(requireActivity().getSupportFragmentManager(), null);
            }
        });
    }

    private void delete() {
        for(CartProduct item : list){
            if (item.isSelect()){
                cartViewModel.deleteProductInCart(item.getVersion().getId());
            }
        }
    }

    private void setCartAdapter() {
        cartViewModel.getAllProductsInCart();
        MutableLiveData<List<CartProduct>> listMutableLiveData = cartViewModel.getListMutableLiveData();
        listMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<CartProduct>>() {
            @Override
            public void onChanged(List<CartProduct> cartProducts) {
                cartAdapter.setData(requireActivity(), cartProducts);
                cartAdapter.notifyDataSetChanged();

                if (cartProducts.isEmpty()){
                    list.clear();
                    binding.rcvCartProductList.setVisibility(View.GONE);
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.tvTotal.setText("0 đ");
                    binding.imgDelete.setVisibility(View.GONE);
                }else {
                    list.clear();
                    list.addAll(cartProducts);
                    binding.tvEmpty.setVisibility(View.GONE);
                    binding.rcvCartProductList.setVisibility(View.VISIBLE);
                    int total = 0;
                    for (CartProduct item : cartProducts){
                        if (item.isSelect()){
                            int a = item.getQuantity();
                            int b = item.getVersion().getPrice();
                            total += a * b;
                        }
                    }
                    if (total == 0){
                        binding.imgDelete.setVisibility(View.GONE);
                        binding.tvTotal.setText("0 đ");
                    }else {
                        binding.imgDelete.setVisibility(View.VISIBLE);
                        binding.tvTotal.setText(Convert.DinhDangTien(total) + " đ");
                    }
                }
            }
        });
    }

    @Override
    public void incrementQuantity(String documentId) {
        cartViewModel.incrementQuantity(documentId);
        cartViewModel.getAllProductsInCart();
        //setCartAdapter();
    }

    @Override
    public void decrementQuantity(String documentId) {
        cartViewModel.decrementQuantity(documentId);
        cartViewModel.getAllProductsInCart();
        //setCartAdapter();
    }

    @Override
    public void deleteProduct(String documentId) {
        cartViewModel.deleteProductInCart(documentId);
        cartViewModel.getAllProductsInCart();
        //setCartAdapter();
    }

    @Override
    public void onChangeSelect(String documentId, boolean aBoolean) {
        cartViewModel.selectProduct(documentId, aBoolean);
        cartViewModel.getAllProductsInCart();
        //setCartAdapter();
    }

    @Override
    public void onClickItemProduct(Product product) {
        addFragment(new DetailProductFragment(), product, R.id.frame_layout_cart);
    }
    private void addFragment(Fragment fragment, Product product, int frameLayout){
        Bundle bundle = new Bundle();
        bundle.putSerializable("ProductModel", product);
        //bundle.putString("StartFragment", nameFragment);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}