package com.example.myapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Listener.ClickItemOrderListener;
import com.example.myapplication.Models.CartProduct;
import com.example.myapplication.Models.Order;
import com.example.myapplication.Models.Product;
import com.example.myapplication.databinding.ItemOrderBinding;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder>{
    private Context context;
    private List<Order> list;
    private ClickItemOrderListener clickItemOrderListener;

    public OrderAdapter(ClickItemOrderListener clickItemOrderListener) {
        this.clickItemOrderListener = clickItemOrderListener;
    }

    public void setData(Context context, List<Order> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = list.get(position);
        if (order == null){
            return;
        }
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private ItemOrderBinding binding;
        public OrderViewHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Order order) {
            Product product = order.getListProduct().get(0).getProduct();
            String img = product.getImages().get(0).toString();
            Glide.with(context).load(img).into(binding.imgProduct);

            String productName = product.getName().toString();
            binding.tvProductName.setText(productName);

            String category = product.getCategory().toString();
            CartProduct cartProduct = order.getListProduct().get(0);
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

            int quantity = order.getListProduct().get(0).getQuantity();
            binding.tvQuantity.setText(String.valueOf(quantity));

            String price = Convert.DinhDangTien(cartProduct.getVersion().getPrice()) + " đ";
            binding.tvPrice.setText(price);

            if (order.getListProduct().size() > 1){
                binding.tvAnother.setVisibility(View.VISIBLE);
                binding.line1.setVisibility(View.VISIBLE);
            }else {
                binding.tvAnother.setVisibility(View.GONE);
                binding.line1.setVisibility(View.GONE);
            }

            int tatolQuantity = 0;
            for (CartProduct item : order.getListProduct()){
                tatolQuantity += item.getQuantity();
            }
            binding.tvTotalQuantity.setText(String.valueOf(tatolQuantity));

            String totalPrice = Convert.DinhDangTien(order.getTotal()) + " đ";
            binding.tvTatolPrice.setText(totalPrice);

            String process = order.getStatus().toString();
            if (process.equals("Chờ xác nhận")){
                binding.btnProcess.setText("Đang xử lý");
                binding.tvStatus.setVisibility(View.GONE);
                binding.btnProcess.setBackgroundColor(Color.parseColor("#CECFCF"));
            } else if (process.equals("Đơn hàng đang trên đường giao đến bạn")){
                binding.tvStatus.setText(process);
                binding.tvStatus.setVisibility(View.VISIBLE);
                binding.btnProcess.setText("Đã nhận được hàng");
                binding.btnProcess.setBackgroundColor(Color.parseColor("#49d7c8"));
            } else if (process.equals("Chưa đánh giá")) {
                binding.tvStatus.setVisibility(View.GONE);
                binding.btnProcess.setText("Đánh giá");
                binding.btnProcess.setBackgroundColor(Color.parseColor("#49d7c8"));
            } else if (process.equals("Đã đánh giá")){
                binding.tvStatus.setVisibility(View.GONE);
                binding.btnProcess.setText(process);
                binding.btnProcess.setBackgroundColor(Color.parseColor("#49d7c8"));
            }

            binding.layoutItemOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickItemOrderListener.onClick(order);
                }
            });
            binding.btnProcess.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickItemOrderListener.onClick(order);
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
