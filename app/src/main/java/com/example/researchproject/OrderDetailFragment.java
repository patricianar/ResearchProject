package com.example.researchproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.researchproject.Classes.Order;
import com.example.researchproject.Customer.OrdersCustomerActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailFragment extends Fragment {
    private static final String ORDER = "Order details";
    private static final String ACTIVITY = "Activity";

    private Order mOrder;
    private String mActivity;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    public static OrderDetailFragment newInstance(Order order, String activity) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ORDER, order);
        args.putString(ACTIVITY, activity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOrder = (Order) getArguments().getSerializable(ORDER);
            mActivity = getArguments().getString(ACTIVITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvOrderId = view.findViewById(R.id.tvOrderId);
        TextView tvOrderDate = view.findViewById(R.id.tvOrderDate);
        TextView tvFullName = view.findViewById(R.id.tvFullName);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        TextView tvCityProvPC = view.findViewById(R.id.tvCityProvPC);
        TextView tvCountry = view.findViewById(R.id.tvCountry);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewItemShipped);

        tvOrderId.setText("Order Id: " + mOrder.getId());
        tvOrderDate.setText("Order Date: " + mOrder.getDate_created());
        tvFullName.setText(String.format("%s %s", mOrder.getCustomer().getFirst_name(), mOrder.getCustomer().getLast_name()));
        tvAddress.setText(mOrder.getCustomer().getAddress().getStreet());
        tvCityProvPC.setText(String.format("%s %s %s", mOrder.getCustomer().getAddress().getCity(),
                mOrder.getCustomer().getAddress().getProvince(), mOrder.getCustomer().getAddress().getPostal_code()));
        tvCountry.setText(mOrder.getCustomer().getAddress().getCountry());


        OrderDetailAdapter myAdapter = new OrderDetailAdapter(mOrder.getProductsOrdered());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);

    }
}