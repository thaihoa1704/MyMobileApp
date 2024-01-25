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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.ProductAdapter;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.Product;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.ProductListViewModel;
import com.example.myapplication.databinding.FragmentProducListBinding;

import java.util.List;

public class ProducListFragment extends Fragment implements ClickItemProductListener {
    private FragmentProducListBinding binding;
    private ProductListViewModel viewModel;
    private ProductAdapter adapter;
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

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);
        adapter = new ProductAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
        binding.rvProduct.setHasFixedSize(true);
        binding.rvProduct.setLayoutManager(gridLayoutManager);

        binding.rvProduct.setAdapter(adapter);
        viewModel.getProductList(category);
        viewModel.getListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> list) {
                adapter.setData(requireActivity(), list);
                adapter.notifyDataSetChanged();
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
        fragmentTransaction.addToBackStack(DetailProductFragment.class.getName());
        fragmentTransaction.commit();
    }
}