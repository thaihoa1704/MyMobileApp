package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapters.SearchProductAdapter;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.Product;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.ProductListViewModel;
import com.example.myapplication.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements ClickItemProductListener {
    private FragmentSearchBinding binding;
    private ProductListViewModel viewModel;
    private SearchProductAdapter adapter;
    private NavController controller;
    private List<Product> productList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = Navigation.findNavController(view);

        adapter = new SearchProductAdapter(this);
        binding.rvProduct.setHasFixedSize(true);
        binding.rvProduct.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rvProduct.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        viewModel.getAllProduct();
        viewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> list) {
                productList.addAll(list);
                adapter.setData(requireActivity(), list);
                adapter.notifyDataSetChanged();
            }
        });

        binding.searchView.clearFocus();
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(R.id.action_searchFragment_to_SHomeFragment);
            }
        });
    }

    private void filterList(String text) {
        List<Product> list = new ArrayList<>();
        for (Product product : productList){
            if (product.getName().toLowerCase().contains(text.toLowerCase())){
                list.add(product);
            }
        }
        if (list.isEmpty()){
            Toast.makeText(requireActivity(), "Không có sản phẩm cần tìm!", Toast.LENGTH_SHORT).show();
        }else {
            adapter.setData(requireActivity(), list);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClickItemProduct(Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("ProductModel", product);
        String nameFragment = "searchFragment";
        bundle.putString("StartFragment", nameFragment);
        //controller.navigate(R.id.action_searchFragment_to_detailProductFragment);
        //NavDirections action = SearchFragmentDirections.actionSearchFragmentToDetailProductFragment();
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_searchFragment_to_detailProductFragment, bundle);
    }
}