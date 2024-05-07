package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Listener.ChangeQuantityCartProduct;
import com.example.myapplication.Listener.ChangeSelectProductListener;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Product;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartViewModel;
import com.example.myapplication.databinding.FragmentCartBinding;

import java.util.List;


public class CartFragment extends Fragment implements ChangeQuantityCartProduct, ChangeSelectProductListener, ClickItemProductListener {
    private FragmentCartBinding binding;
    private CartAdapter cartAdapter;
    private CartViewModel cartViewModel;
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
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartAdapter = new CartAdapter(this, this, this);

        binding.rcvCartProductList.setHasFixedSize(true);
        binding.rcvCartProductList.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvCartProductList.setAdapter(cartAdapter);

        setCartAdapter();

        binding.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.tvTotal.getText().equals("0 đ")){
                    Toast.makeText(requireContext(), "Chọn sản phẩm bạn muốn thanh toán!", Toast.LENGTH_SHORT).show();
                }else {
                    controller.navigate(R.id.action_cartFragment_to_orderFragment);
                }
            }
        });
    }

    private void setCartAdapter() {
        cartViewModel.getAllProductsInCart();
        cartViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<CartProduct>>() {
            @Override
            public void onChanged(List<CartProduct> cartProducts) {
                cartAdapter.setData(requireActivity(), cartProducts);
                cartAdapter.notifyDataSetChanged();

                if (cartProducts.isEmpty()){
                    binding.tvTotal.setText("0 đ");
                }else {
                    int total = 0;
                    for (CartProduct item : cartProducts){
                        if (item.isSelect()){
                            int a = item.getQuantity();
                            int b = item.getVersion().getPrice();
                            total += a * b;
                        }
                    }
                    binding.tvTotal.setText(Convert.DinhDangTien(total) + " đ");
                }
            }
        });
    }

    @Override
    public void incrementQuantity(String documentId) {
        cartViewModel.incrementQuantity(documentId);
        setCartAdapter();
    }

    @Override
    public void decrementQuantity(String documentId) {
        cartViewModel.decrementQuantity(documentId);
        setCartAdapter();
    }

    @Override
    public void deleteProduct(String documentId) {
        cartViewModel.deleteProductInCart(documentId);
        setCartAdapter();
    }

    @Override
    public void onChangeSelect(String documentId, boolean aBoolean) {
        cartViewModel.selectProduct(documentId, aBoolean);
        setCartAdapter();
    }

    @Override
    public void onClickItemProduct(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ProductModel", product);
        bundle.putString("StartFragment", "cartFragment");
        controller.navigate(R.id.action_cartFragment_to_detailProductFragment, bundle);
    }
}