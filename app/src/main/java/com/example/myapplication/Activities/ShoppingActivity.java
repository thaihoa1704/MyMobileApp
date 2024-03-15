package com.example.myapplication.Activities;

import static androidx.navigation.ActivityKt.findNavController;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.myapplication.Fragments.CartFragment;
import com.example.myapplication.Fragments.CategoryFragment;
import com.example.myapplication.Fragments.ProfileUserFragment;
import com.example.myapplication.Fragments.SHomeFragment;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityShoppingBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShoppingActivity extends AppCompatActivity {
    private NavController navController;
    private ActivityShoppingBinding binding;
    private NavHostFragment navHostFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewShopping);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
    }
}