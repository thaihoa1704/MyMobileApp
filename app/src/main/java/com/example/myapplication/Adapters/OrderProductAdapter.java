package com.example.myapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
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
            binding.tvProductName.setText(cartProduct.getProduct().getName());
            Glide.with(context).load(cartProduct.getProduct().getImages().get(0)).into(binding.imageProduct);
            binding.tvQuantity.setText(String.valueOf(cartProduct.getQuantity()));
            String price = Convert.DinhDangTien(cartProduct.getVersion().getPrice()) + " đ";
            binding.tvPrice.setText(price);

            if (cartProduct.getProduct().getCategory().equals("Điện thoại")){
                setPhoneVersionData(cartProduct);
            } else if (cartProduct.getProduct().getCategory().equals("Laptop")) {
                setLaptopVersionData(cartProduct);
            } else if (cartProduct.getProduct().getCategory().equals("Tai nghe")) {
                setWatchVersionData(cartProduct);
            } else if (cartProduct.getProduct().getCategory().equals("Đồng hồ")) {
                setHeadPhoneVersionData(cartProduct);
            } else {
                setAccessoryVersionData(cartProduct);
            }
        }
        private void setPhoneVersionData(CartProduct cartProduct) {
            String version = cartProduct.getVersion().getColor() + " - "
                    + cartProduct.getVersion().getRam() + " - "
                    + cartProduct.getVersion().getStorage();
            binding.tvVersion.setText(version);
        }
        private void setLaptopVersionData(CartProduct cartProduct) {
        }
        private void setWatchVersionData(CartProduct cartProduct) {
        }
        private void setHeadPhoneVersionData(CartProduct cartProduct) {
        }
        private void setAccessoryVersionData(CartProduct cartProduct) {
        }
    }
}
