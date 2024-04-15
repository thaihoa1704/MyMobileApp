package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.myapplication.databinding.ItemImageBinding;
import java.util.List;
import android.content.Context;

public class ImageAdapter extends PagerAdapter {
    private Context context;
    private List<String> images;
    public void setData(Context context, List<String> images) {
        this.context = context;
        this.images = images;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ItemImageBinding binding = ItemImageBinding.inflate(LayoutInflater.from(container.getContext()), container, false);

        String image = images.get(position);
        if (image != null){
            Glide.with(context).load(image).into(binding.image);
        }

        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public int getCount() {
        if (images != null){
            return images.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
