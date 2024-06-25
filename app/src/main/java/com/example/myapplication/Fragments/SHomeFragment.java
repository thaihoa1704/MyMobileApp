package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import android.widget.Toast;

import com.example.myapplication.Adapters.ProductAdapter;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.Product;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CartViewModel;
import com.example.myapplication.ViewModels.ProductListViewModel;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentShomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class SHomeFragment extends Fragment implements ClickItemProductListener {
    private UserViewModel viewModel;
    private CartViewModel cartViewModel;
    private ProductListViewModel productViewModel;
    private FragmentShomeBinding binding;
    private NavController controller;
    private ProductAdapter adapter;
    private BottomNavigationView bottomNavigationView;
    private View view;
    private int count = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShomeBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = Navigation.findNavController(view);

        productViewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        cartViewModel.selectNoneAllProduct();

        adapter = new ProductAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvProduct.setLayoutManager(layoutManager);
        binding.rcvProduct.setAdapter(adapter);
        setSpecialProductList();

        viewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        viewModel.userLogged();
        viewModel.getUserLogin().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String name = user.getName();
                binding.txtName.setText(name);
            }
        });
        binding.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(R.id.action_SHomeFragment_to_searchFragment);
            }
        });
        binding.imgPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Điện thoại");
            }
        });
        binding.imgHeadPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Tai nghe");
            }
        });
        binding.imgLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Laptop");
            }
        });
        binding.imgWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Đồng hồ");
            }
        });
        binding.imageAccessory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToNewFragment("Phụ kiện");
            }
        });


        //if back stack null => back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                int backStackCount = requireActivity().getSupportFragmentManager().getBackStackEntryCount();
                if (backStackCount == 0){
                    count++;
                    if (count == 2){
                        getActivity().finishAndRemoveTask();
                    }
                } else {
                    controller.popBackStack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private void setSpecialProductList(){
        productViewModel.getSpecialProductList();
        productViewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if (!products.isEmpty()){
                    adapter.setData(requireActivity(), products);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Không có sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void moveToNewFragment(String nameCategory){
        Bundle bundle = new Bundle();
        String nameFragment = "homeFragment";
        bundle.putString("StartFragment", nameFragment);
        bundle.putString("category", nameCategory);
        controller.navigate(R.id.action_SHomeFragment_to_productListFragment, bundle);
    }
    private void addFragment(Fragment fragment, Product product){
        Bundle bundle = new Bundle();
        bundle.putSerializable("ProductModel", product);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_shome, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    public void onClickItemProduct(Product product) {
        addFragment(new DetailProductFragment(), product);
        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setVisibility(View.GONE);
        view = getActivity().findViewById(R.id.line_activity);
        view.setVisibility(View.GONE);
    }
}