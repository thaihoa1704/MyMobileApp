package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.databinding.ItemOrderProductBinding;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder>{
    private List<CartProduct> list;
    private Context context;
    public void setData(Context context, List<CartProduct> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderProductBinding binding = ItemOrderProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        CartProduct cartProduct = list.get(position);
        if (cartProduct == null){
            return;
        }
        holder.bind(cartProduct);
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class OrderProductViewHolder extends RecyclerView.ViewHolder {
        private ItemOrderProductBinding binding;
        public OrderProductViewHolder(ItemOrderProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartProduct cartProduct) {
            Glide.with(context).load(cartProduct.getProduct().getImage()).into(binding.imgProduct);
            binding.tvProductName.setText(cartProduct.getProduct().getProductName());
            binding.tvPrice.setText(Convert.DinhDangTien(cartProduct.getProduct().getPrice()) + "VND");
            binding.tvQuantity.setText(cartProduct.getQuantity() + "");
        }
    }
}
