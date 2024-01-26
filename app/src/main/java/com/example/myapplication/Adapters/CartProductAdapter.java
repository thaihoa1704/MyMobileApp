package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Listener.ChangeQuantityListener;
import com.example.myapplication.Listener.ChangeSelectProductListener;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.databinding.ItemCardProductBinding;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder> {
    private List<CartProduct> list;
    private Context context;
    private ClickItemProductListener clickItemProductListener;
    private ChangeQuantityListener changeQuantityListener;
    private ChangeSelectProductListener changeSelectProductListener;
    public void setData(Context context, List<CartProduct> list){
        this.context = context;
        this.list = list;
    }
    public CartProductAdapter(ClickItemProductListener clickItemProductListener,
                              ChangeQuantityListener changeQuantityListener,
                              ChangeSelectProductListener changeSelectProductListener){
        this.clickItemProductListener = clickItemProductListener;
        this.changeQuantityListener = changeQuantityListener;
        this.changeSelectProductListener = changeSelectProductListener;
    }
    @NonNull
    @Override
    public CartProductAdapter.CartProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCardProductBinding binding = ItemCardProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductAdapter.CartProductViewHolder holder, int position) {
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

    public class CartProductViewHolder extends RecyclerView.ViewHolder {
        private ItemCardProductBinding binding;
        public CartProductViewHolder(ItemCardProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(CartProduct cartProduct) {
            Glide.with(context).load(cartProduct.getProduct().getImage()).into(binding.imgProduct);
            binding.tvProductName.setText(cartProduct.getProduct().getProductName());
            binding.tvPrice.setText(Convert.DinhDangTien(cartProduct.getProduct().getPrice()) + " VND");
            binding.tvQuantity.setText(cartProduct.getQuantity() + "");
            binding.checkBox.setChecked(cartProduct.isSelect());

            binding.imgProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemProductListener.onClickItemProduct(cartProduct.getProduct());
                }
            });

            binding.tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeQuantityListener.incrementQuantity(cartProduct);
                }
            });

            binding.tvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cartProduct.getQuantity() == 1){
                        changeQuantityListener.deleteProduct(cartProduct);
                    }else{
                        changeQuantityListener.decrementQuantity(cartProduct);
                    }
                }
            });

            binding.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeSelectProductListener.onChangeSelect(cartProduct, binding.checkBox.isChecked());
                }
            });
        }
    }
}
