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
import com.example.myapplication.Listener.ClickItemBrandListener;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.databinding.FiltersDialogBinding;

import java.util.List;

public class FiltersDialog extends DialogFragment implements ClickItemBrandListener {
    private FiltersDialogBinding binding;
    private BrandAdapter brandAdapter;
    private List<Brand> brands;
    private Brand brand;
    public interface GetBrand{
        void getData(Brand brand);
    }
    public GetBrand getBrand;
    public FiltersDialog(List<Brand> brands){
        this.brands = brands;
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

        binding.btnApply.setVisibility(View.GONE);
        binding.tvBrandName.addTextChangedListener(textWatcher);
        setBrandAdapter();

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
                if (brand != null){
                    getBrand.getData(brand);
                }
                dialog.dismiss();
            }
        });

        binding.tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBrandAdapter();
                binding.tvBrandName.setText("");
            }
        });
        return dialog;
    }

    private void setBrandAdapter() {
        brandAdapter = new BrandAdapter(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 3);
        binding.rcvBrand.setHasFixedSize(true);
        binding.rcvBrand.setLayoutManager(gridLayoutManager);
        binding.rcvBrand.setAdapter(brandAdapter);
        brandAdapter.setData(requireActivity(), brands);
        brandAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickItemBrand(Brand brandSelected) {
        binding.tvBrandName.setText(brandSelected.getBrandName());
        this.brand = brandSelected;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String name = binding.tvBrandName.getText().toString().trim();
            if (!name.equals("")){
                binding.btnApply.setVisibility(View.VISIBLE);
            } else {
                binding.btnApply.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        getBrand = (GetBrand) getParentFragment();
    }
}