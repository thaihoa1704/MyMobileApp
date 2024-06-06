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
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.myapplication.Adapters.OrderPriceAdapter;
import com.example.myapplication.Adapters.ProductAdapter;
import com.example.myapplication.Dialog.FiltersDialog;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Product;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CategoryViewModel;
import com.example.myapplication.ViewModels.ProductListViewModel;
import com.example.myapplication.databinding.FragmentProductListBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment implements ClickItemProductListener, FiltersDialog.GetBrand {
    private FragmentProductListBinding binding;
    private ProductListViewModel viewModel;
    private CategoryViewModel categoryViewModel;
    private ProductAdapter adapter;
    private OrderPriceAdapter orderPriceAdapter;
    private List<Product> productList;
    private List<Product> productList1;
    private NavController controller;
    private String category;
    private Brand brand;
    private int selectedPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductListBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String startFragment = getArguments().getString("StartFragment");
        category = getArguments().getString("category");
        binding.tvCategoryName.setText(category);

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        controller = Navigation.findNavController(view);

        adapter = new ProductAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
        binding.rvProduct.setHasFixedSize(true);
        binding.rvProduct.setLayoutManager(gridLayoutManager);
        binding.rvProduct.setAdapter(adapter);

        productList = new ArrayList<>();
        productList1 = new ArrayList<>();

        setProductAdapter(category);

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

        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(category);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startFragment == "homeFragment"){
                    controller.navigate(R.id.action_productListFragment_to_SHomeFragment);
                }else if (startFragment == "categoryFragment"){
                    controller.navigate(R.id.action_productListFragment_to_categoryFragment);
                }
            }
        });
    }

    private void setProductAdapter(String category) {
        viewModel.getProductList(category);
        MutableLiveData<List<Product>> productMutableLiveData = viewModel.getListMutableLiveData();
        productMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> list) {
                adapter.setData(requireActivity(), list);
                adapter.notifyDataSetChanged();
                if (list.isEmpty()){
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.tvEmpty.setVisibility(View.GONE);
                    productList.clear();
                    productList.addAll(list);
                    productList1.clear();
                    productList1.addAll(list);
                }
            }
        });
    }
    private void setProductWithBrandAdapter(String category, String brandName){
        viewModel.getProductList(category, brandName);
        MutableLiveData<List<Product>> productMutableLiveData = viewModel.getListMutableLiveData();
        productMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> list) {
                adapter.setData(requireActivity(), list);
                adapter.notifyDataSetChanged();
                if (list.isEmpty()){
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.tvEmpty.setVisibility(View.GONE);
                    productList.clear();
                    productList.addAll(list);
                    productList1.clear();
                    productList1.addAll(list);
                }
                //productMutableLiveData.removeObserver(this);
            }
        });
    }

    private void showDialog(String category) {
        categoryViewModel.getCategoryData(category);
        MutableLiveData<List<Brand>> brandMutableLiveData = categoryViewModel.getMutableLiveData();
        categoryViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Brand>>() {
            @Override
            public void onChanged(List<Brand> brands) {
                if (brands != null){
                    FiltersDialog filtersDialog = new FiltersDialog(brands, selectedPosition);
                    filtersDialog.show(getChildFragmentManager(), "FiltersDialog");
                }
                brandMutableLiveData.removeObserver(this);
            }
        });
    }

    @Override
    public void onClickItemProduct(Product product) {
        //Creat DetailProductFragment overlaps this fragment
        //Purpose: Don't reload view of this fragment when close DetailProductFragmen
        addFragment(new DetailProductFragment(), product, "productListFragment");
    }

    private List<String> getList(){
        List<String> list = new ArrayList<>();
        list.add("Ngẫu nhiên");
        list.add("Giá tăng dần");
        list.add("Giá giảm dần");
        return list;
    }
    private void addFragment(Fragment fragment, Product product, String nameFragment){
        Bundle bundle = new Bundle();
        bundle.putSerializable("ProductModel", product);
        bundle.putString("StartFragment", nameFragment);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_product_list, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void getData(Brand brandSelected, int position) {
        if (position != -1){
            if (position != this.selectedPosition){
                setProductWithBrandAdapter(category, brandSelected.getBrandName());
                this.brand = brandSelected;
                this.selectedPosition = position;
                //setupSpinnerLikeBegin
                binding.spinner.setAdapter(orderPriceAdapter);
                binding.imgFilter.setImageResource(R.drawable.filter_filtering_icon);
            }
        } else {
            setProductAdapter(category);
            this.brand = null;
            this.selectedPosition = -1;
            binding.spinner.setAdapter(orderPriceAdapter);
            binding.imgFilter.setImageResource(R.drawable.filter_outline_icon);
        }
    }
}