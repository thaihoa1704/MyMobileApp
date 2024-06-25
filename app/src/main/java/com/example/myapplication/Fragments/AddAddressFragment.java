package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentAddAddressBinding;

public class AddAddressFragment extends Fragment {
    private FragmentAddAddressBinding binding;
    private NavController controller;
    private UserViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddAddressBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        controller = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        //String fragmentName = getArguments().getString("fragmentName");

        binding.btnAdd.setEnabled(false);
        binding.edtAddress1.addTextChangedListener(textWatcher);
        binding.edtAddress2.addTextChangedListener(textWatcher);

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = binding.edtAddress1.getText().toString() + " " + binding.edtAddress2.getText().toString();
                viewModel.addAddress(address);
                viewModel.getCheck().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean) {
                            Toast.makeText(getContext(), "Thêm địa chỉ thành công", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Bundle bundle = new Bundle();
                                    //bundle.putString("fragmentName", fragmentName);
                                    controller.popBackStack();
                                }
                            }, 2000);
                        }else {
                            Toast.makeText(getContext(), "Thêm địa chỉ thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle bundle = new Bundle();
                //bundle.putString("fragmentName", fragmentName);
                controller.popBackStack();
            }
        });
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String email = binding.edtAddress1.getText().toString().trim();
            String pass = binding.edtAddress2.getText().toString().trim();
            binding.btnAdd.setEnabled(!email.isEmpty() && !pass.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}