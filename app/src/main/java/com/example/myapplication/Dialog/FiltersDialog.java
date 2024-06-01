package com.example.myapplication.Dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;

import com.example.myapplication.Adapters.BrandAdapter;
import com.example.myapplication.Listener.ClickItemBrandListener;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.databinding.FiltersDialogBinding;

import java.util.List;

public class FiltersDialog extends DialogFragment implements ClickItemBrandListener {
    private FiltersDialogBinding binding;
    private BrandAdapter brandAdapter;
    private List<Brand> brands;
    public FiltersDialog(List<Brand> brands){
        this.brands = brands;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        binding = FiltersDialogBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(binding.getRoot());

        brandAdapter = new BrandAdapter(this);
        return dialog;
    }

    @Override
    public void onClickItemBrand(Brand brand) {

    }
}