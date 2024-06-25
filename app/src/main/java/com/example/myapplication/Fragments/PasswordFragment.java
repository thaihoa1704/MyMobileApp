package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
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
import com.example.myapplication.databinding.FragmentPasswordBinding;

public class PasswordFragment extends Fragment {
    private FragmentPasswordBinding binding;
    private UserViewModel userViewModel;
    private NavController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPasswordBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        controller = Navigation.findNavController(view);

        binding.btnChange.setEnabled(false);
        binding.edtOldPass.addTextChangedListener(textWatcher);
        binding.edtNewPass1.addTextChangedListener(textWatcher);
        binding.edtNewPass2.addTextChangedListener(textWatcher);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.popBackStack();
            }
        });

        binding.btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = binding.edtOldPass.getText().toString().trim();
                String newPass1 = binding.edtNewPass1.getText().toString().trim();
                String newPass2 = binding.edtNewPass2.getText().toString().trim();
                if (!newPass1.equals(newPass2)) {
                    binding.tvMessage.setText("Mật khẩu mới không khớp!");
                    binding.tvMessage.setVisibility(View.VISIBLE);
                } else {
                    userViewModel.changePassword(oldPass, newPass1);
                    userViewModel.getCheck().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean aBoolean) {
                            if (aBoolean) {
                                binding.tvMessage.setText("Đổi mật khẩu thành công!");
                                binding.tvMessage.setVisibility(View.VISIBLE);
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.popBackStack();
                                    }
                                }, 2000);
                            } else {
                                binding.tvMessage.setText("Mật khẩu cũ không đúng!");
                                binding.tvMessage.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });

    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String oldPass = binding.edtOldPass.getText().toString().trim();
            String newPass1 = binding.edtNewPass1.getText().toString().trim();
            String newPass2 = binding.edtNewPass2.getText().toString().trim();
            binding.btnChange.setEnabled(!oldPass.isEmpty() && !newPass1.isEmpty() && !newPass2.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}