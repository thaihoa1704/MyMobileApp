package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.myapplication.Models.Order;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.OrderViewModel;
import com.example.myapplication.databinding.FragmentRateOrderBinding;

public class RateOrderFragment extends Fragment {
    private FragmentRateOrderBinding binding;
    private OrderViewModel orderViewModel;
    private NavController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRateOrderBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Order order = (Order) getArguments().getSerializable("Order");
        String from = getArguments().getString("From");

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        controller = Navigation.findNavController(view);

        binding.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                binding.tvRate.setText(String.valueOf(rating));
                switch ((int) ratingBar.getRating()){
                    case 1:
                        binding.tvRate.setText("Tệ");
                        break;
                    case 2:
                        binding.tvRate.setText("Không hài lòng");
                        break;
                    case 3:
                        binding.tvRate.setText("Bình thường");
                        break;
                    case 4:
                        binding.tvRate.setText("Hài lòng");
                        break;
                    case 5:
                        binding.tvRate.setText("Tuyệt vời");
                        break;
                    default:
                        binding.tvRate.setText(" ");
                        break;
                }
            }
        });

        binding.edtNote.addTextChangedListener(textWatcher);

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int star = (int) binding.ratingBar.getRating();
                String note = binding.edtNote.getText().toString().trim();
                orderViewModel.updateRateOrder(order, star, note);
                orderViewModel.getCheckOrder().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (aBoolean){
                            Toast.makeText(getContext(), "Gửi đánh giá thành công", Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    removeFragment(order, from);
                                }
                            }, 2000);
                        } else {
                            Toast.makeText(getContext(), "Gửi đánh giá thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment(order, from);
            }
        });
    }
    private void removeFragment(Order order, String from) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Order", order);
        bundle.putString("From", from);
        controller.navigate(R.id.action_rateOrderFragment_to_detailOrderFragment, bundle);
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String note = binding.edtNote.getText().toString().trim();
            if (!note.isEmpty()){
                binding.tvNote.setVisibility(View.GONE);
                binding.tvNote1.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}