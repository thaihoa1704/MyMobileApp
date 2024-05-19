package com.example.myapplication.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.AddressAdapter;
import com.example.myapplication.Listener.ClickItemAddressListener;
import com.example.myapplication.Models.Address;
import com.example.myapplication.Models.User;
import com.example.myapplication.R;
import com.example.myapplication.ViewModels.UserViewModel;
import com.example.myapplication.databinding.FragmentAddressBinding;

import java.util.List;

public class AddressFragment extends Fragment implements ClickItemAddressListener {
    private FragmentAddressBinding binding;
    private AddressAdapter adapter;
    private UserViewModel viewModel;
    private NavController controller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        controller = Navigation.findNavController(view);

        String fragmentName = getArguments().getString("fragmentName");
        if (fragmentName.equals("informationUser")){
            binding.label.setText("Địa chỉ của bạn");
        }else if (fragmentName.equals("orderFragment")){
            binding.label.setText("Chọn địa chỉ nhận hàng");
        }

        adapter = new AddressAdapter(this);
        binding.rcvAddress.setHasFixedSize(true);
        binding.rcvAddress.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.rcvAddress.setAdapter(adapter);

        setAddressAdapter();

        binding.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("fragmentName", fragmentName);
                controller.navigate(R.id.action_addressFragment_to_addAddressFragment, bundle);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentName.equals("informationUser")){
                    controller.navigate(R.id.action_addressFragment_to_userFragment);
                }else {
                    controller.navigate(R.id.action_addressFragment_to_orderFragment);
                }
            }
        });
    }

    private void setAddressAdapter() {
        viewModel.getAddress();
        viewModel.getAddressMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Address>>() {
            @Override
            public void onChanged(List<Address> addressList) {
                int position = 0;
                for (int i = 0; i < addressList.size(); i++){
                    if (addressList.get(i).isSelect()){
                        position = i;
                        break;
                    }
                }
                adapter.setData(addressList, requireActivity(), position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(Address address) {
        viewModel.setAddressSelected(address);
    }
}