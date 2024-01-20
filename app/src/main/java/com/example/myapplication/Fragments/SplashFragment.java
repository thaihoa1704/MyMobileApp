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
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.myapplication.Activities.AdminActivity;
import com.example.myapplication.Activities.ShoppingActivity;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.UserViewModel;

public class SplashFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        navController = Navigation.findNavController(view);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(viewModel.getCurrentUser() != null){
                    User user = viewModel.getUserLogged();
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
                }else {
                    navController.navigate(R.id.action_splashFragment_to_loginFragment);
                }
            }
        }, 3000);
    }
}