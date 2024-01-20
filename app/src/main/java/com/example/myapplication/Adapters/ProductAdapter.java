package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.Product;
import com.example.myapplication.databinding.ItemProductBinding;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;
    private ClickItemProductListener clickItemProductListener;
    public ProductAdapter(ClickItemProductListener clickItemProductListener){
        this.clickItemProductListener = clickItemProductListener;
    }
    public void setData(Context context, List<Product> list){
        this.context = context;
        this.productList = list;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null){
            return;
        }
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        if(productList != null){
            return productList.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ItemProductBinding binding;
        public ProductViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Product product) {
            Glide.with(context).load(product.getImage()).into(binding.image);
            binding.tvProductName.setText(product.getProductName());
            binding.tvPrice.setText(Convert.DinhDangTien(product.getPrice()) + " VND");

            binding.layoutItemProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemProductListener.onClickItemProduct(product);
                }
            });
        }
    }
}
