package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private NavController navController;
    private UserViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_blank2, container, false);
        binding = FragmentRegisterBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.btnRegister.setEnabled(false);
        binding.textName.addTextChangedListener(textWatcher);
        binding.textEmail.addTextChangedListener(textWatcher);
        binding.textPass.addTextChangedListener(textWatcher);
        binding.textPhone.addTextChangedListener(textWatcher);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setUserName(binding.textName.getText().toString());
                user.setEmail(binding.textEmail.getText().toString().trim());
                user.setPhone(binding.textPhone.getText().toString().trim());
                user.setPassword(binding.textPass.getText().toString().trim());

                viewModel.userRegister(user);
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String name = binding.textName.getText().toString().trim();
            String email = binding.textEmail.getText().toString().trim();
            String pass = binding.textPass.getText().toString().trim();
            String phone = binding.textPhone.getText().toString().trim();

            binding.btnRegister.setEnabled(!email.isEmpty() && !pass.isEmpty() && !name.isEmpty() && !phone.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}