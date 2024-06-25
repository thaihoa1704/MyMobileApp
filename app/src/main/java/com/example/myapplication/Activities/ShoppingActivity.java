package com.example.myapplication.Activities;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivityShoppingBinding;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Stack;

public class ShoppingActivity extends AppCompatActivity {
    private ActivityShoppingBinding binding;
    private NavController navController;
    private NavHostFragment navHostFragment;
    private Stack<Integer> fragmentBackStack = new Stack<>();
    private Stack<Integer> idFragmentDestination = new Stack<>();
    private OnBackPressedCallback onBackPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewShopping);
        navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationUI.onNavDestinationSelected(item, navController);
                return true;
            }
        });

        defaultNavigation();
        //changeDestination();
        goToRootFragment();
        hideBottomNavigationView();
    }

    private void defaultNavigation() {
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                Integer fragmentId = navDestination.getId();
                fragmentBackStack.push(fragmentId);
                Log.e("Fragment", fragmentId.toString());
                Integer bottomBarId = findBottomBarIdFromFragment(fragmentId);
                if (bottomBarId != null) {
                    idFragmentDestination.push(bottomBarId);
                } else {
                    int length = idFragmentDestination.size();
                    idFragmentDestination.push(idFragmentDestination.get(length - 1));
                }
            }
        });
    }
    private Integer findBottomBarIdFromFragment(Integer fragmentId) {
        if (fragmentId != null) {
            Integer bottomBarId;
            if (fragmentId == R.id.SHomeFragment) {
                bottomBarId = R.id.SHomeFragment;
            } else if (fragmentId == R.id.categoryFragment) {
                bottomBarId = R.id.categoryFragment;
            } else if (fragmentId == R.id.cartFragment) {
                bottomBarId = R.id.cartFragment;
            } else if (fragmentId == R.id.userFragment) {
                bottomBarId = R.id.userFragment;
            } else {
                bottomBarId = null;
            }
            return bottomBarId;
        } else {
            return null;
        }
    }
    private void hideBottomNavigationView() {
        final ArrayList<Integer> listFragment = getIntegers();

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (listFragment.contains(navDestination.getId())) {
                    binding.bottomNavigationView.setVisibility(View.GONE);
                    binding.lineActivity.setVisibility(View.GONE);
                } else {
                    binding.bottomNavigationView.setVisibility(View.VISIBLE);
                    binding.lineActivity.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private static @NonNull ArrayList<Integer> getIntegers() {
        ArrayList<Integer> listFragment = new ArrayList<>();
        listFragment.add(R.id.addressFragment);
        listFragment.add(R.id.addAddressFragment);
        listFragment.add(R.id.changeNameFragment);
        listFragment.add(R.id.passwordFragment);
        listFragment.add(R.id.orderFragment);
        listFragment.add(R.id.detailOrderFragment);
        listFragment.add(R.id.rateOrderFragment);
        listFragment.add(R.id.detailProductFragment);
        listFragment.add(R.id.handleOrderFragment);
        return listFragment;
    }

    private void changeDestination() {
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int n = 0;
                Integer bottomBarId = item.getItemId();
                for (Integer id : idFragmentDestination){
                    if (id == bottomBarId){
                        n++;
                    }
                }
                navController.navigate(fragmentBackStack.get(n));
                return true;
            }
        });
    }
    private void goToRootFragment(){
        binding.bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                navController.popBackStack(item.getItemId(), false);
            }
        });
    }

}