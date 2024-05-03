package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.Adapters.ColorAdapter;
import com.example.myapplication.Adapters.ImageAdapter;
import com.example.myapplication.Adapters.PhoneVersionAdapter;
import com.example.myapplication.Helper.Convert;
import com.example.myapplication.Listener.ClickItemColorListener;
import com.example.myapplication.Listener.PhoneModel.ClickItemPhoneVersionListener;
import com.example.myapplication.Models.CartPhone;
import com.example.myapplication.Models.Product;
import com.example.myapplication.Models.ProductColor;
import com.example.myapplication.Models.ProductVersion.PhoneVersion;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.PhoneViewModel;
import com.example.myapplication.databinding.FragmentDetailProductBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DetailProductFragment extends Fragment implements ClickItemColorListener, ClickItemPhoneVersionListener {
    private FragmentDetailProductBinding binding;
    private PhoneViewModel phoneViewModel;
    private NavController controller;
    private ColorAdapter colorAdapter;
    private PhoneVersionAdapter phoneVersionAdapter;
    private PhoneVersion phoneVersionSelected;
    private ImageAdapter imageAdapter;
    private Product product;
    private ProductColor productColorSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailProductBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //View Model
        phoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);

        controller = Navigation.findNavController(view);

        String startFragment = getArguments().getString("StartFragment");
        product = (Product) getArguments().getSerializable("ProductModel");
        binding.tvProductName.setText(product.getName());
        binding.tvDescription.setText(product.getDescription());

        //Slide image
        setSlideImage();
        //Show color
        setColorAdapter();
        //Show product attribute
        if (product.getCategory().equals("Điện thoại")){
            setPhoneVersionAdapter();
        }else if (product.getCategory().equals("Laptop")){
            setLaptopAttributeAdapter();
        }

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getCategory().equals("Điện thoại")){
                    addPhoneToCart();
                }else if (product.getCategory().equals("Laptop")){

                }
            }
        });
        
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startFragment == "searchFragment"){
                    controller.navigate(R.id.action_detailProductFragment_to_searchFragment);
                }else if (startFragment == "productListFragment"){
                    removeFragment();
                }else {
                    controller.navigate(R.id.action_detailProductFragment_to_cartFragment);
                }
            }
        });
    }

    private void setSlideImage() {
        imageAdapter = new ImageAdapter();
        imageAdapter.setData(requireActivity(), product.getImages());
        binding.viewPagerImage.setAdapter(imageAdapter);
        binding.circleIndicator.setViewPager(binding.viewPagerImage);
        imageAdapter.registerDataSetObserver(binding.circleIndicator.getDataSetObserver());
    }

    private void removeFragment() {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

    @Override
    public void onClickColor(ProductColor color) {
        productColorSelected = color;
        binding.tvColor.setText(color.getColor().toString());
        setPriceAndQuantity();
    }
    @Override
    public void onClick(PhoneVersion phoneVersion) {
        phoneVersionSelected = phoneVersion;
        setPriceAndQuantity();
    }
    private void setColorAdapter(){
        colorAdapter = new ColorAdapter(this);
        LinearLayoutManager layoutManagerColor = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvColor.setLayoutManager(layoutManagerColor);
        binding.rcvColor.setAdapter(colorAdapter);
        colorAdapter.setData(requireActivity(), product.getColors());
        colorAdapter.notifyDataSetChanged();
    }
    private void setPhoneVersionAdapter(){
        phoneVersionAdapter = new PhoneVersionAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvAttribute.setLayoutManager(layoutManager);
        binding.rcvAttribute.setAdapter(phoneVersionAdapter);
        phoneViewModel.getPhoneVersions(product.getId());
        phoneViewModel.getPhoneVersions().observe(getViewLifecycleOwner(), new Observer<List<PhoneVersion>>() {
            @Override
            public void onChanged(List<PhoneVersion> phoneVersionList) {
                if (phoneVersionList != null){
                    binding.tvVersion.setText("Phiên bản:");
                    binding.tvVersion.setVisibility(View.VISIBLE);

                    //List version without duplicate item
                    HashSet<PhoneVersion> list = new HashSet<>();
                    list.addAll(phoneVersionList);
                    List<PhoneVersion> phoneVersions = new ArrayList<>();
                    phoneVersions.addAll(list);

                    phoneVersionAdapter.setData(requireActivity(),phoneVersions);
                    phoneVersionAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    private void setLaptopAttributeAdapter() {
        //laptopAttributeAdapter = new LaptopAttributeAdapter(this);
        LinearLayoutManager layoutManagerAttribute = new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.rcvAttribute.setLayoutManager(layoutManagerAttribute);
        //binding.rcvAttribute.setAdapter(laptopAttributeAdapter);
    }
    private void setPriceAndQuantity(){
        if (productColorSelected != null && phoneVersionSelected != null){
            phoneViewModel.getPhonePrice(product.getId().toString(), productColorSelected.getColor().toString(),
                                        phoneVersionSelected.getRam().toString(), phoneVersionSelected.getStorage().toString());
            phoneViewModel.getPhoneVersion().observe(getViewLifecycleOwner(), new Observer<PhoneVersion>() {
                @Override
                public void onChanged(PhoneVersion phoneVersion) {
                    if (phoneVersion != null){
                        int price = phoneVersion.getPrice();
                        int quantity = phoneVersion.getQuantity();
                        binding.tvPrice.setText(Convert.DinhDangTien(price) + " đ");

                        if (quantity == 0){
                            setAddButtonOff();
                        }else {
                            setAddButtonOn();
                        }
                    }else {
                        binding.tvPrice.setText(null);
                        setAddButtonOff();
                    }
                }
            });
        }else if (productColorSelected == null || phoneVersionSelected == null){
            binding.tvPrice.setText(null);
            binding.btnAdd.setVisibility(View.GONE);
        }else {
            binding.tvPrice.setText(null);
            setAddButtonOff();
        }
    }
    private void setAddButtonOn(){
        binding.btnAdd.setText("Thêm vào giỏ hàng");
        binding.btnAdd.setEnabled(true);
        binding.btnAdd.setVisibility(View.VISIBLE);
    }
    private void setAddButtonOff(){
        binding.btnAdd.setText("Hết hàng");
        binding.btnAdd.setEnabled(false);
        binding.btnAdd.setVisibility(View.VISIBLE);
    }
    private void addPhoneToCart(){
        if (productColorSelected != null && phoneVersionSelected != null){
            phoneViewModel.getPhonePrice(product.getId().toString(), productColorSelected.getColor().toString(),
                    phoneVersionSelected.getRam().toString(), phoneVersionSelected.getStorage().toString());
            MutableLiveData<PhoneVersion> mutableLiveData = phoneViewModel.getPhoneVersion();
            mutableLiveData.observe(getViewLifecycleOwner(), new Observer<PhoneVersion>() {
                @Override
                public void onChanged(PhoneVersion phoneVersion) {
                    if (phoneVersion != null){
                        CartPhone cartPhone = new CartPhone(product, phoneVersion, 1, false);
                        phoneViewModel.checkProductInCart(cartPhone);
                        MutableLiveData<Boolean> check = phoneViewModel.getCheck();
                        check.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean){
                                    Toast.makeText(requireContext(), "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                                    check.removeObserver(this);
                                }else {
                                    Toast.makeText(requireContext(), "Không thêm vào được giỏ hàng!", Toast.LENGTH_SHORT).show();
                                    check.removeObserver(this);
                                }
                            }
                        });
                        mutableLiveData.removeObserver(this);
                    }
                }
            });
        }
    }
}