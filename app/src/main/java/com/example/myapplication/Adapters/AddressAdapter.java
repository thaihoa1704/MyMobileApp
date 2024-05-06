package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Listener.ClickItemAddressListener;
import com.example.myapplication.Models.Address;
import com.example.myapplication.databinding.ItemLayoutAddressBinding;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    private List<Address> list;
    private Context context;
    private int selectedPosition = -1;
    private ClickItemAddressListener clickItemAddressListener;

    public void setData(List<Address> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public AddressAdapter(ClickItemAddressListener clickItemAddressListener) {
        this.clickItemAddressListener = clickItemAddressListener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutAddressBinding binding = ItemLayoutAddressBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AddressViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = list.get(position);
        if (address == null){
            return;
        }
        holder.bind(address, position);
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        private ItemLayoutAddressBinding binding;

        public AddressViewHolder(ItemLayoutAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Address address, int position) {
            binding.radioButton.setChecked(address.isSelect());
            binding.tvAddress.setText(address.getString());

            if (selectedPosition == position){
                binding.radioButton.setChecked(true);
            }else {
                binding.radioButton.setChecked(false);
            }
            binding.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickItemAddressListener.onClick(address);
                    setSingleSelection(getBindingAdapterPosition());
                }
            });
        }

        private void setSingleSelection(int bindingAdapterPosition) {
            if (bindingAdapterPosition == selectedPosition) return;

            notifyItemChanged(selectedPosition);
            selectedPosition = bindingAdapterPosition;
            notifyItemChanged(selectedPosition);
        }
    }
}
