package com.example.myapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Listener.ClickItemBrandListener;
import com.example.myapplication.Models.Brand;
import com.example.myapplication.databinding.ItemBrandBinding;

import java.util.ArrayList;
import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder>{
    private Context context;
    private List<Brand> brandList;
    private ClickItemBrandListener clickItemBrandListener;

    public void setData(Context context, List<Brand> brandList){
        this.context = context;
        this.brandList = brandList;
    }
    public BrandAdapter(ClickItemBrandListener clickItemBrandListener){
        this.clickItemBrandListener = clickItemBrandListener;
    }
    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBrandBinding binding = ItemBrandBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BrandViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        if (brand == null){
            return;
        }
        holder.bind(brand);
    }

    @Override
    public int getItemCount() {
        if (brandList != null){
            return brandList.size();
        }
        return 0;
    }
    public List<Brand> getListSelected(){
        List<Brand> selectedList = new ArrayList<>();
        for (Brand item : brandList){
            if (item.isSelected()){
                selectedList.add(item);
            }
        }
        return selectedList;
    }

    public class BrandViewHolder extends RecyclerView.ViewHolder{
        private ItemBrandBinding binding;

        public BrandViewHolder(ItemBrandBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Brand brand) {
            Glide.with(context).load(brand.getImage()).into(binding.image);

            if (brand.isSelected()){
                binding.btnSelect.setSelected(true);
            } else {
                binding.btnSelect.setSelected(false);
            }
            binding.btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.btnSelect.isSelected()){
                        binding.image.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        binding.btnSelect.setSelected(false);
                        brand.setSelected(false);
                    }else {
                        binding.image.setBackgroundColor(Color.parseColor("#FF968F"));
                        binding.btnSelect.setSelected(true);
                        brand.setSelected(true);
                    }
                }
            });
        }
    }
}
