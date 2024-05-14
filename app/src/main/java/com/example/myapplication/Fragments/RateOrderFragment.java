package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentRateOrderBinding;

public class RateOrderFragment extends Fragment {
    private FragmentRateOrderBinding binding;

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

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
            }
        });
    }
    private void removeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
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