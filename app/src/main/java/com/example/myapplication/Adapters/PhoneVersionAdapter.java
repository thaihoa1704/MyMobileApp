package com.example.myapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Listener.PhoneModel.ClickItemPhoneVersionListener;
import com.example.myapplication.Models.ProductVersion.PhoneVersion;
import com.example.myapplication.databinding.ItemPhoneAttributeBinding;

import java.util.List;

public class PhoneVersionAdapter extends RecyclerView.Adapter<PhoneVersionAdapter.PhoneVersionViewHolder>{
    private Context context;
    private List<PhoneVersion> list;
    private int selectedPosition = -1;
    private ClickItemPhoneVersionListener onClick;

    public PhoneVersionAdapter(ClickItemPhoneVersionListener onClick) {
        this.onClick = onClick;
    }

    public void setData(Context context, List<PhoneVersion> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public PhoneVersionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPhoneAttributeBinding binding = ItemPhoneAttributeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PhoneVersionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneVersionViewHolder holder, int position) {
        PhoneVersion memory = list.get(position);
        if (memory == null){
            return;
        }
        holder.bind(memory, position);
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class PhoneVersionViewHolder extends RecyclerView.ViewHolder{
        private ItemPhoneAttributeBinding binding;

        public PhoneVersionViewHolder(ItemPhoneAttributeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(PhoneVersion phoneVersion, int position) {
            binding.tvMemory.setText(phoneVersion.getRam() + "-" + phoneVersion.getStorage());

            if (selectedPosition == position){
                binding.tvMemory.setBackgroundColor(Color.parseColor("#FF968F"));
            }else {
                binding.tvMemory.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            }

            binding.tvMemory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick.onClick(phoneVersion);
                    setSingleSelection(getBindingAdapterPosition());
                }
            });
        }
    }
    private void setSingleSelection(int bindingAdapterPosition) {
        if (bindingAdapterPosition == RecyclerView.NO_POSITION) return;

        notifyItemChanged(selectedPosition);
        selectedPosition = bindingAdapterPosition;
        notifyItemChanged(selectedPosition);
    }
}
