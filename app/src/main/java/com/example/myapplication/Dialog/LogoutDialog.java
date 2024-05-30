package com.example.myapplication.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;

import com.example.myapplication.Listener.OnClickChoice;
import com.example.myapplication.databinding.LogoutDialogBinding;

public class LogoutDialog extends DialogFragment {
    private LogoutDialogBinding binding;
    private OnClickChoice onClickChoice;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        binding = LogoutDialogBinding.inflate(LayoutInflater.from(getActivity()));
        alertDialog.setView(binding.getRoot());
        Dialog dialog = alertDialog.create();
        onClickChoice = (OnClickChoice) getContext();

        binding.btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChoice.onClick(true);
            }
        });

        return dialog;
    }
}