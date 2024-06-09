package com.example.myapplication.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.Adapters.BrandAdapter;
import com.example.myapplication.Adapters.PriceAdapter;
import com.example.myapplication.Listener.ClickItemBrandListener;
import com.example.myapplication.Listener.ClickItemPriceListener;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.Models.Price;
import com.example.myapplication.databinding.FiltersDialogBinding;

import java.util.List;

public class FiltersDialog extends DialogFragment implements ClickItemBrandListener, ClickItemPriceListener {
    private FiltersDialogBinding binding;
    private BrandAdapter brandAdapter;
    private PriceAdapter priceAdapter;
    private List<Brand> brands;
    private List<Price> price;
    private int selectedBrandPosition;
    private int selectedPricePosition;
    private Brand brandSelected;
    private Price priceSelected;

    public interface GetFilters{
        void getData(Brand brandSelected, int brandPosition, Price priceSelected, int pricePosition);
    }
    public GetFilters getFilters;
    public FiltersDialog(List<Brand> brands, int brandPosition, List<Price> price, int pricePosition){
        this.brands = brands;
        this.selectedBrandPosition = brandPosition;
        this.price = price;
        this.selectedPricePosition = pricePosition;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        binding = FiltersDialogBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(binding.getRoot());

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setBrandAdapter(selectedBrandPosition);
        setPriceAdapter(selectedPricePosition);

        binding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send data to fragment
                getFilters.getData(brandSelected, selectedBrandPosition, priceSelected, selectedPricePosition);
                dialog.dismiss();
            }
        });

        binding.tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBrandAdapter(-1);
                brandSelected = null;
                selectedBrandPosition = -1;
                setPriceAdapter(-1);
                priceSelected = null;
                selectedPricePosition = -1;
            }
        });
        return dialog;
    }

    private void setPriceAdapter(int position) {
        priceAdapter = new PriceAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 3);
        binding.rcvPrice.setHasFixedSize(true);
        binding.rcvPrice.setLayoutManager(gridLayoutManager);
        binding.rcvPrice.setAdapter(priceAdapter);
        priceAdapter.setData(requireActivity(), price, position);
        priceAdapter.notifyDataSetChanged();
    }

    private void setBrandAdapter(int position) {
        brandAdapter = new BrandAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 3);
        binding.rcvBrand.setHasFixedSize(true);
        binding.rcvBrand.setLayoutManager(gridLayoutManager);
        binding.rcvBrand.setAdapter(brandAdapter);
        brandAdapter.setData(requireActivity(), brands, position);
        brandAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickItemBrand(Brand brandSelected, int position) {
        this.brandSelected = brandSelected;
        this.selectedBrandPosition = position;
    }
    @Override
    public void onPriceClick(Price price, int position) {
        this.priceSelected = price;
        this.selectedPricePosition = position;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getFilters = (GetFilters) getParentFragment();
    }
}