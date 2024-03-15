package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapters.BrandAdapter;
import com.example.myapplication.Listener.ClickItemBrandListener;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Category;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.CategoryViewModel;
import com.example.myapplication.databinding.FragmentFiltersBinding;

import java.util.ArrayList;
import java.util.List;

public class FiltersFragment extends Fragment implements ClickItemBrandListener {
    private FragmentFiltersBinding binding;
    private CategoryViewModel viewModel;
    private BrandAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFiltersBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new BrandAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 3);
        binding.rcvBrand.setHasFixedSize(true);
        binding.rcvBrand.setLayoutManager(gridLayoutManager);
        binding.rcvBrand.setAdapter(adapter);

        String category = getArguments().getString("category");

        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        viewModel.getCategoryData(category);
        viewModel.getMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Brand>>() {
            @Override
            public void onChanged(List<Brand> brands) {
                adapter.setData(requireActivity(), brands);
                adapter.notifyDataSetChanged();
            }
        });
        binding.btnApplyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Brand> selectedBrands = adapter.getListSelected();
                ArrayList<String> listBrandName = new ArrayList<>();
                for (Brand item : selectedBrands){
                    listBrandName.add(item.getBrandName());
                }
                replaceFragment(new ProducListFragment(), category, listBrandName);
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
    public void onClickItemBrand(Brand brandSelect) {
    }
    private void replaceFragment(Fragment fragment, String category, ArrayList<String> brandList){
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putStringArrayList("selectedBrand", brandList);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_product_list, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }
}