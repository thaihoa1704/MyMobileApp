package com.example.myapplication.Dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.myapplication.Listener.OnClickChoice;
import com.example.myapplication.databinding.ChoiceDialogBinding;

public class ChoiceDialog extends DialogFragment {
    private String fragmentName;
    private OnClickChoice onClickChoice;
    private ChoiceDialogBinding binding;
    public ChoiceDialog(String fragmentName,OnClickChoice onClickChoice){
        this.fragmentName = fragmentName;
        this.onClickChoice = onClickChoice;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        binding = ChoiceDialogBinding.inflate(LayoutInflater.from(getActivity()));
        dialog.setContentView(binding.getRoot());

        if (fragmentName.equals("CartFragment")){
            binding.tvTitle.setText("Xoá sản phẩm khỏi giỏ hàng?");
            binding.btnYes.setText("Xoá");
        } else {
            binding.tvTitle.setText("Bạn có muốn đăng xuất không?");
            binding.btnYes.setText("Đăng xuất");
        }

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChoice.onClick(true);
                dialog.dismiss();
            }
        });

        return dialog;
    }
}