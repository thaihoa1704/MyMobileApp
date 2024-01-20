package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.Fragments.CartFragment;
import com.example.myapplication.Fragments.CategoryFragment;
import com.example.myapplication.Fragments.ProfileUserFragment;
import com.example.myapplication.Fragments.SHomeFragment;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityShoppingBinding;
import com.google.android.material.navigation.NavigationBarView;

public class ShoppingActivity extends AppCompatActivity {
    private ActivityShoppingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = (User) getIntent().getSerializableExtra("UserLogin");
        binding = ActivityShoppingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new SHomeFragment(), user);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new SHomeFragment(), user);
            } else if (itemId == R.id.category) {
                replaceFragment(new CategoryFragment(), user);
            } else if (itemId == R.id.cart) {
                replaceFragment(new CartFragment(), user);
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileUserFragment(), user);
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment, User user){
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserLogin", user);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_shopping, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}