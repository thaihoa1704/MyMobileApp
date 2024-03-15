package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.myapplication.Adapters.OrderPriceAdapter;
import com.example.myapplication.Adapters.ProductAdapter;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Product;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.ProductListViewModel;
import com.example.myapplication.databinding.FragmentProducListBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProducListFragment extends Fragment implements ClickItemProductListener {
    private FragmentProducListBinding binding;
    private ProductListViewModel viewModel;
    private ProductAdapter adapter;
    private OrderPriceAdapter orderPriceAdapter;
    private List<Product> productList;
    private List<Product> productList1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProducListBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String category = getArguments().getString("category");
        binding.tvCategoryName.setText(category);
        ArrayList<String> selectedList = getArguments().getStringArrayList("selectedBrand");
        int price = getArguments().getInt("price");

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);

        adapter = new ProductAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
        binding.rvProduct.setHasFixedSize(true);
        binding.rvProduct.setLayoutManager(gridLayoutManager);
        binding.rvProduct.setAdapter(adapter);

        productList = new ArrayList<>();
        productList1 = new ArrayList<>();

        if (selectedList == null){
            viewModel.getProductList(category);
            viewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                @Override
                public void onChanged(List<Product> list) {
                    productList.addAll(list);
                    productList1.addAll(list);
                    adapter.setData(requireActivity(), productList);
                    adapter.notifyDataSetChanged();
                }
            });
        }else {
            int n = 0;
            for (String string : selectedList){
                viewModel.getProductList(category, string);
                MutableLiveData<List<Product>> productMutableLiveData = viewModel.getListMutableLiveData();
                productMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> list) {
                        productList.addAll(list);
                        productList1.addAll(list);
                        adapter.setData(requireActivity(), productList);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(requireActivity(), String.valueOf(productList.size()), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }

        orderPriceAdapter = new OrderPriceAdapter(requireActivity(), R.layout.item_selected, getList());
        binding.spinner.setAdapter(orderPriceAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String choice = orderPriceAdapter.getItem(i).toString();
                if (choice == "Ngẫu nhiên"){
                    adapter.setData(requireActivity(), productList1);
                    adapter.notifyDataSetChanged();
                }else if (choice == "Giá tăng dần"){
                    viewModel.orderByPriceAscending(productList);
                    adapter.setData(requireActivity(), productList);
                    adapter.notifyDataSetChanged();
                }else if (choice == "Giá giảm dần"){
                    viewModel.orderByPriceDescending(productList);
                    adapter.setData(requireActivity(), productList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.linearLayoutFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new FiltersFragment(), category);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupOnBackPressed();
            }
        });
    }
    private void setupOnBackPressed(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onClickItemProduct(Product product) {
        addFragment(new DetailProductFragment(), product);
    }

    private void addFragment(Fragment fragment, Product product){
        Bundle bundle = new Bundle();
        bundle.putSerializable("ProductModel", product);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_product_list, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    private void replaceFragment(Fragment fragment, String category){
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_product_list, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }
    private List<String> getList(){
        List<String> list = new ArrayList<>();
        list.add("Ngẫu nhiên");
        list.add("Giá tăng dần");
        list.add("Giá giảm dần");
        return list;
    }
}