package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.Activities.AdminActivity;
import com.example.myapplication.Activities.ShoppingActivity;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private NavController navController;
    private UserViewModel viewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        navController = Navigation.findNavController(view);

        binding.btnLogin.setEnabled(false);
        binding.textEmail.addTextChangedListener(textWatcher);
        binding.textPass.addTextChangedListener(textWatcher);

        //Underline textView
        SpannableString content = new SpannableString("Quên mật khẩu?");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        binding.tvForgotPassword.setText(content);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setEmail(binding.textEmail.getText().toString().trim());
                user.setPassword(binding.textPass.getText().toString().trim());

                viewModel.userLogin(user);
                viewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
                    @Override
                    public void onChanged(FirebaseUser firebaseUser) {
                        if(firebaseUser != null){
                            viewModel.getUserLogin().observe(getViewLifecycleOwner(), new Observer<User>() {
                                @Override
                                public void onChanged(User user) {
                                    String type = user.getUserType();
                                    if(type.equals("admin")){
                                        Intent intent = new Intent(requireActivity(), AdminActivity.class);
                                        //implements Serializable in class before send an object
                                        intent.putExtra("UserLogin", user);
                                        startActivity(intent);
                                        requireActivity().finish();
                                    }else {
                                        Intent intent = new Intent(requireActivity(), ShoppingActivity.class);
                                        intent.putExtra("UserLogin", user);
                                        startActivity(intent);
                                        requireActivity().finish();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        binding.tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String email = binding.textEmail.getText().toString().trim();
            String pass = binding.textPass.getText().toString().trim();
            binding.btnLogin.setEnabled(!email.isEmpty() && !pass.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}