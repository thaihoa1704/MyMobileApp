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
import com.example.myapplication.R;
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
            binding.label.setVisibility(View.GONE);
            binding.tvTitle1.setText("Xoá sản phẩm khỏi giỏ hàng?");
            binding.tvTitle2.setVisibility(View.GONE);
            binding.btnYes.setText("Xoá");
        } else if (fragmentName.equals("OrderFragment")){
            binding.label.setText("XÁC NHẬN ĐẶT HÀNG");
            binding.label.setVisibility(View.VISIBLE);
            binding.tvTitle1.setText("Bạn có chắc chắn muốn đặt hàng");
            binding.tvTitle2.setVisibility(View.GONE);
            binding.btnYes.setText("Thanh toán");
        } else if (fragmentName.equals("DetailOrderFragment")){
            binding.label.setText("XÁC NHẬN HUỶ ĐƠN HÀNG");
            binding.label.setVisibility(View.VISIBLE);
            binding.tvTitle1.setVisibility(View.GONE);
            binding.tvTitle2.setVisibility(View.VISIBLE);
            binding.btnYes.setText("Huỷ đơn");
        } else {
            binding.label.setVisibility(View.GONE);
            binding.tvTitle1.setText("Bạn có muốn đăng xuất không?");
            binding.tvTitle2.setVisibility(View.GONE);
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