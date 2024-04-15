package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Listener.ClickItemColorListener;
import com.example.myapplication.Models.ProductColor;
import com.example.myapplication.databinding.ItemColorBinding;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder>{
    private Context context;
    private List<ProductColor> colors;
    private int selectedPosition = -1;
    private ClickItemColorListener clickItemColorListener;
    public ColorAdapter(ClickItemColorListener clickItemColorListener){
        this.clickItemColorListener = clickItemColorListener;
    }
    public void setData(Context context, List<ProductColor> colors){
        this.context = context;
        this.colors = colors;
    }
    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemColorBinding binding = ItemColorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ColorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        ProductColor color = colors.get(position);
        if (color == null){
            return;
        }
        holder.bind(color, position);
    }

    @Override
    public int getItemCount() {
        if(colors != null){
            return colors.size();
        }
        return 0;
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {
        private ItemColorBinding binding;

        public ColorViewHolder(ItemColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductColor color, int position) {
            binding.color.setCardBackgroundColor(Color.parseColor(color.getColorCode()));

            if (selectedPosition == position){
                binding.selected.setVisibility(View.VISIBLE);
            }else {
                binding.selected.setVisibility(View.INVISIBLE);
            }

            binding.layoutItemColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemColorListener.onClickColor(color);
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
