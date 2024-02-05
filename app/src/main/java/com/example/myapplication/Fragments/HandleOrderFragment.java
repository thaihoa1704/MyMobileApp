package com.example.myapplication.Fragments;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHandleOrderBinding;

public class HandleOrderFragment extends Fragment {
    private FragmentHandleOrderBinding binding;
    private User user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHandleOrderBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = (User) getArguments().getSerializable("UserLogin");
        binding.constraintLayout.setVisibility(View.INVISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.INVISIBLE);
                binding.constraintLayout.setVisibility(View.VISIBLE);
                AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) binding.imgDone.getDrawable();
                drawable.start();
                binding.tvNotification.setText("Đặt hàng thành công!");
                //replaceFragment(new SHomeFragment(), user);
            }
        }, 4000);

        /*Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                replaceFragment(new SHomeFragment(), user);
            }
        }, 8000);*/
    }
    private void replaceFragment(Fragment fragment, User user){
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserLogin", user);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_shopping, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}