package com.example.myapplication.Helper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.CartProductAdapter;

public class SwipeToDeleteItem extends ItemTouchHelper.SimpleCallback {
    private CartProductAdapter cartProductAdapter;
    private RecyclerView.Adapter adapter;
    public SwipeToDeleteItem(RecyclerView.Adapter adapter) {
        super(0, ItemTouchHelper.LEFT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getLayoutPosition();
        adapter.notifyItemRemoved(position);
        cartProductAdapter = (CartProductAdapter) adapter;
        cartProductAdapter.deleteItemAt(position);
    }
}
