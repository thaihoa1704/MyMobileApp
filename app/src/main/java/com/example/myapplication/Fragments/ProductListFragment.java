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
import com.example.myapplication.Models.Price;
import com.example.myapplication.Models.Product;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CategoryViewModel;
import com.example.myapplication.ViewModels.ProductListViewModel;
import com.example.myapplication.databinding.FragmentProductListBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends Fragment implements ClickItemProductListener, FiltersDialog.GetFilters {
    private FragmentProductListBinding binding;
    private ProductListViewModel viewModel;
    private CategoryViewModel categoryViewModel;
    private ProductAdapter adapter;
    private OrderPriceAdapter orderPriceAdapter;
    private List<Product> productList;
    private List<Product> productListWithoutOrder;
    private List<Brand> brandList;
    private List<Price> priceList;
    private NavController controller;
    private String category;
    private Brand brand;
    private Price price;
    private int selectedBrandPosition = -1;
    private int selectedPricePosition = -1;

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

        viewModel = new ViewModelProvider(requireActivity()).get(ProductListViewModel.class);
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);

        controller = Navigation.findNavController(view);

        adapter = new ProductAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
        binding.rvProduct.setHasFixedSize(true);
        binding.rvProduct.setLayoutManager(gridLayoutManager);
        binding.rvProduct.setAdapter(adapter);

        productList = new ArrayList<>();
        productListWithoutOrder = new ArrayList<>();
        brandList = new ArrayList<>();
        priceList = new ArrayList<>();

        setProductAdapter(category);
        getPriceList(category);
        getBrandList(category);

        orderPriceAdapter = new OrderPriceAdapter(requireActivity(), R.layout.item_selected, getList());
        binding.spinner.setAdapter(orderPriceAdapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String choice = orderPriceAdapter.getItem(i).toString();
                if (choice == "Ngẫu nhiên"){
                    if (!productListWithoutOrder.isEmpty()){
                        adapter.setData(requireActivity(), productListWithoutOrder);
                        adapter.notifyDataSetChanged();
                    }
                }else if (choice == "Giá tăng dần"){
                    if (!productList.isEmpty()){
                        viewModel.orderByPriceAscending(productList);
                        adapter.setData(requireActivity(), productList);
                        adapter.notifyDataSetChanged();
                    }
                }else if (choice == "Giá giảm dần"){
                    if (!productList.isEmpty()){
                        viewModel.orderByPriceDescending(productList);
                        adapter.setData(requireActivity(), productList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.popBackStack();
                /*if (startFragment == "homeFragment"){
                    controller.navigate(R.id.action_productListFragment_to_SHomeFragment);
                }else if (startFragment == "categoryFragment"){
                    controller.navigate(R.id.action_productListFragment_to_categoryFragment);
                }*/
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
                    productListWithoutOrder.clear();
                    productListWithoutOrder.addAll(list);
                }
                //productMutableLiveData.removeObserver(this);
            }
        });
    }
    private void setProductWithPriceAdapter(String category, Price price) {
        viewModel.getProductListWithPrice(category, price);
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
                    productListWithoutOrder.clear();
                    productListWithoutOrder.addAll(list);
                }
                //productMutableLiveData.removeObserver(this);
            }
        });
    }
    private void setProductWithBrandAndPriceAdapter(String category, Brand brand, Price price){
        viewModel.getProductListWithBrandAndPrice(category, brand, price);
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
                    productListWithoutOrder.clear();
                    productListWithoutOrder.addAll(list);
                }
                //productMutableLiveData.removeObserver(this);
            }
        });
    }
    private void setProductWithBrandAdapter(String category, Brand brand){
        viewModel.getProductListWithBrand(category, brand);
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
                    productListWithoutOrder.clear();
                    productListWithoutOrder.addAll(list);
                }
                //productMutableLiveData.removeObserver(this);
            }
        });
    }

    private void showDialog() {
        if (brandList.isEmpty() || priceList.isEmpty()){
            return;
        }
        FiltersDialog filtersDialog = new FiltersDialog(brandList, selectedBrandPosition, priceList, selectedPricePosition);
        filtersDialog.show(getChildFragmentManager(), "FiltersDialog");
    }

    @Override
    public void onClickItemProduct(Product product) {
        //Creat DetailProductFragment overlaps this fragment
        //Purpose: Don't reload view of this fragment when close DetailProductFragmen
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("ProductModel", product);
//        controller.navigate(R.id.action_productListFragment_to_detailProductFragment, bundle);
        addFragment(new DetailProductFragment(), product);
    }

    private List<String> getList(){
        List<String> list = new ArrayList<>();
        list.add("Ngẫu nhiên");
        list.add("Giá tăng dần");
        list.add("Giá giảm dần");
        return list;
    }
    private void addFragment(Fragment fragment, Product product){
        Bundle bundle = new Bundle();
        bundle.putSerializable("ProductModel", product);
        //bundle.putString("StartFragment", nameFragment);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_product_list, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void getData(Brand brandSelected, int brandPosition, Price price, int pricePosition) {
        // choose brand and price
        if (brandPosition != -1 && pricePosition != -1){
            binding.imgFilter1.setVisibility(View.VISIBLE);
            if (brandPosition == this.selectedBrandPosition && pricePosition == this.selectedPricePosition){
                Toast.makeText(requireActivity(), "Bạn đã chọn rồi", Toast.LENGTH_SHORT).show();
                return;
            }
            setProductWithBrandAndPriceAdapter(category, brandSelected, price);
            this.brand = brandSelected;
            this.selectedBrandPosition = brandPosition;
            this.price = price;
            this.selectedPricePosition = pricePosition;
            //setupSpinnerLikeBegin
            binding.spinner.setAdapter(orderPriceAdapter);
        } else //only choose brand
            if (brandPosition != -1 && pricePosition == -1){
                if (brandPosition == this.selectedBrandPosition && pricePosition == this.selectedPricePosition){
                    return;
                }
                binding.imgFilter1.setVisibility(View.VISIBLE);
                setProductWithBrandAdapter(category, brandSelected);
                this.brand = brandSelected;
                this.selectedBrandPosition = brandPosition;
                this.price = null;
                this.selectedPricePosition = pricePosition;
                //setupSpinnerLikeBegin
                binding.spinner.setAdapter(orderPriceAdapter);
        } else //only choose price
            if (brandPosition == -1 && pricePosition != -1){
                if (pricePosition == this.selectedPricePosition && brandPosition == this.selectedBrandPosition){
                    return;
                }
                binding.imgFilter1.setVisibility(View.VISIBLE);
                setProductWithPriceAdapter(category, price);
                this.price = price;
                this.selectedPricePosition = pricePosition;
                this.brand = null;
                this.selectedBrandPosition = brandPosition;
                //setupSpinnerLikeBegin
                binding.spinner.setAdapter(orderPriceAdapter);
        }
        else if (brandPosition == -1 && pricePosition == -1){
            setProductAdapter(category);
            this.brand = null;
            this.selectedBrandPosition = -1;
            this.price = null;
            this.selectedPricePosition = -1;
            binding.spinner.setAdapter(orderPriceAdapter);
            binding.imgFilter1.setVisibility(View.GONE);
        }
    }
    private void getPriceList(String category){
        categoryViewModel.getPriceList(changeName(category));
        MutableLiveData<List<Price>> priceMutableLiveData = categoryViewModel.getMutableLiveDataPrice();
        priceMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Price>>() {
            @Override
            public void onChanged(List<Price> price) {
                if (price.isEmpty()){
                    Toast.makeText(requireActivity(), "Không có dữ liệu về giá", Toast.LENGTH_SHORT).show();
                }
                priceList.addAll(price);
                //priceMutableLiveData.removeObserver(this);
            }
        });
    }
    private void getBrandList(String category){
        categoryViewModel.getBrandList(changeName(category));
        MutableLiveData<List<Brand>> brandMutableLiveData = categoryViewModel.getMutableLiveDataBrand();
        brandMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<Brand>>() {
            @Override
            public void onChanged(List<Brand> brands) {
                if (brands.isEmpty()){
                    Toast.makeText(requireActivity(), "Không có dữ liệu về thương hiệu", Toast.LENGTH_SHORT).show();
                }
                brandList.addAll(brands);
                //brandMutableLiveData.removeObserver(this);
            }
        });
    }
    private String changeName(String string){
        String name;
        if (string.equals("Điện thoại")){
            name = "Phone";
        }else if (string.equals("Laptop")){
            name = "Laptop";
        }else if (string.equals("Đồng hồ")){
            name = "Watch";
        }else if (string.equals("Tai nghe")){
            name = "Headphone";
        }else {
            name = "Accessory";
        }
        return name.trim();
    }
}