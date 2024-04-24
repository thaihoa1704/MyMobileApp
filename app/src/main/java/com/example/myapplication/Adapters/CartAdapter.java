package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Listener.ChangeQuantityCartProduct;
import com.example.myapplication.Listener.ChangeSelectProductListener;
import com.example.myapplication.Listener.ClickItemProductListener;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.databinding.ItemCartBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private Context context;
    private List<CartProduct> list;
    private ChangeQuantityCartProduct changeQuantityCartProduct;
    private ChangeSelectProductListener changeSelectProductListener;
    private ClickItemProductListener clickItemProductListener;
    public void setData(Context context, List<CartProduct> list){
        this.context = context;
        this.list = list;
    }
    public CartAdapter(ChangeQuantityCartProduct changeQuantityCartProduct,
                       ChangeSelectProductListener changeSelectProductListener,
                       ClickItemProductListener clickItemProductListener){
        this.changeQuantityCartProduct = changeQuantityCartProduct;
        this.changeSelectProductListener = changeSelectProductListener;
        this.clickItemProductListener = clickItemProductListener;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCartBinding binding = ItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
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

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private ItemCartBinding binding;
        private String documentId;
        public CartViewHolder(ItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CartProduct cartProduct) {
            binding.tvProductName.setText(cartProduct.getProduct().getName());
            Glide.with(context).load(cartProduct.getProduct().getImages().get(0)).into(binding.imgProduct);
            binding.tvQuantity.setText(String.valueOf(cartProduct.getQuantity()));
            binding.checkBox.setChecked(cartProduct.isSelect());
            String price = Convert.DinhDangTien(cartProduct.getVersion().getPrice()) + " đ";
            binding.tvPrice.setText(price);

            documentId = cartProduct.getVersion().getId();

            String category = cartProduct.getProduct().getCategory().toString();
            if (category.equals("Điện thoại")){
                setPhoneVersionData(cartProduct);
            } else if (category.equals("Laptop")) {
                setLaptopVersionData(cartProduct);
            } else if (category.equals("Tai nghe")) {
                setWatchVersionData(cartProduct);
            } else if (category.equals("Đồng hồ")) {
                setHeadPhoneVersionData(cartProduct);
            } else {
                setAccessoryVersionData(cartProduct);
            }
            binding.tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (documentId != null){
                        changeQuantityCartProduct.incrementQuantity(documentId);
                    }
                }
            });
            binding.tvMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (documentId != null){
                        if (cartProduct.getQuantity() == 1){
                            changeQuantityCartProduct.deleteProduct(documentId);
                        }else {
                            changeQuantityCartProduct.decrementQuantity(documentId);
                        }
                    }
                }
            });
            binding.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeSelectProductListener.onChangeSelect(documentId, binding.checkBox.isChecked());
                }
            });
            binding.imgProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItemProductListener.onClickItemProduct(cartProduct.getProduct());
                }
            });
        }

        private void setPhoneVersionData(CartProduct cartProduct) {
            String version = cartProduct.getVersion().getColor().toString() + " - "
                    + cartProduct.getVersion().getRam().toString() + " - "
                    + cartProduct.getVersion().getStorage().toString();
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
