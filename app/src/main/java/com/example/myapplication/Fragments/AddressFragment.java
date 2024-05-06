package com.example.myapplication.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.AddressAdapter;
import com.example.myapplication.Listener.ClickItemAddressListener;
import com.example.myapplication.Models.Address;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentAddressBinding;

public class AddressFragment extends Fragment implements ClickItemAddressListener {
    private FragmentAddressBinding binding;
    private AddressAdapter adapter;
    private UserViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        adapter = new AddressAdapter(this);
        binding.rcvAddress.setHasFixedSize(true);
        binding.rcvAddress.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvAddress.setAdapter(adapter);

        setAddressAdapter();
    }

    private void setAddressAdapter() {

    }

    @Override
    public void onClick(Address address) {

    }
}