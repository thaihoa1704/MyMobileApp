package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.Product;
import com.example.myapplication.databinding.ItemSearchProductBinding;

import java.util.List;

public class SearchProductAdapter extends RecyclerView.Adapter<SearchProductAdapter.SearchProductViewHolder>{
    private Context context;
    private List<Product> productList;
    private ClickItemProductListener clickItemProductListener;

    public void setData(Context context, List<Product> productList){
        this.context = context;
        this.productList = productList;
    }
    public SearchProductAdapter(ClickItemProductListener clickItemProductListener){
        this.clickItemProductListener = clickItemProductListener;
    }
    @NonNull
    @Override
    public SearchProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchProductBinding binding = ItemSearchProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null){
            return;
        }
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        if (productList != null){
            return productList.size();
        }
        return 0;
    }

    public class SearchProductViewHolder extends RecyclerView.ViewHolder{
        private ItemSearchProductBinding binding;

        public SearchProductViewHolder(ItemSearchProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Product product) {
            Glide.with(context).load(product.getImage()).into(binding.imgProduct);
            binding.tvProductName.setText(product.getProductName());

            binding.layoutItemSearchProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemProductListener.onClickItemProduct(product);
                }
            });
        }
    }
}
