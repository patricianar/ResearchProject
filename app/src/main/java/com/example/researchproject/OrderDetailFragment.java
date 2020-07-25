package com.example.researchproject;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.researchproject.Admin.AddProductFragment;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.Classes.Order;
import com.example.researchproject.Customer.OrdersCustomerActivity;
import com.example.researchproject.Customer.PaymentFragment;

import java.text.DecimalFormat;

public class OrderDetailFragment extends Fragment {
    private static final String ORDER = "Order details";
    private static final String ACTIVITY = "Activity";
    private static final String TAG = "OrderDetailFragment";
    private OrderDetailFragment.OnOrderDetailClickedListener mListener;

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
//        TextView tvOrderId = view.findViewById(R.id.tvOrderId);
        TextView tvOrderDate = view.findViewById(R.id.tvOrderDate);
        TextView tvFullName = view.findViewById(R.id.tvFullName);
        TextView tvAddress = view.findViewById(R.id.tvAddress);
        TextView tvCityProvPC = view.findViewById(R.id.tvCityProvPC);
        TextView tvCountry = view.findViewById(R.id.tvCountry);
        TextView tvSubtotal = view.findViewById(R.id.tvSubtotal);
        TextView tvShipping = view.findViewById(R.id.tvShipping);
        TextView tvTax = view.findViewById(R.id.tvTaxes);
        TextView tvTotal = view.findViewById(R.id.tvGrandTotal);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewItemShipped);
        Button btnStatus = view.findViewById(R.id.btnStatus);
        ImageView imgClose = view.findViewById(R.id.imgBack);

        imgClose.setOnClickListener(view1 -> getActivity().getSupportFragmentManager().popBackStack());

        if (mActivity.equals("Cart")) {
            btnStatus.setText("Place Order");
            btnStatus.setOnClickListener(v -> mListener.onOrderDetailClicked());
        } else if (mActivity.equals("OrderCustomer")) {
            btnStatus.setVisibility(View.INVISIBLE);
        } else if (mActivity.equals("OrderAdmin")) {
            if (mOrder.getStatus().equals("Awaiting Fulfillment")) {
                btnStatus.setText("Change to In Progress");
            } else if (mOrder.getStatus().equals("In Progress")) {
                btnStatus.setText("Change to Shipped");
            }
        }

//        tvOrderId.setText("Id: " + mOrder.getId());
        tvOrderDate.setText("Date: " + mOrder.getDate_created());
        tvFullName.setText(String.format("%s %s", mOrder.getCustomer().getFirst_name(), mOrder.getCustomer().getLast_name()));
        tvAddress.setText(mOrder.getCustomer().getAddress().getStreet());
        tvCityProvPC.setText(String.format("%s %s %s", mOrder.getCustomer().getAddress().getCity(),
                mOrder.getCustomer().getAddress().getProvince(), mOrder.getCustomer().getAddress().getPostal_code()));
        tvCountry.setText(mOrder.getCustomer().getAddress().getCountry());

        DecimalFormat decimalFormat = new DecimalFormat("$#.##");
        tvSubtotal.setText(decimalFormat.format(mOrder.getTotal()));
        tvShipping.setText(decimalFormat.format(mOrder.getShipping_cost()));
        tvTax.setText(decimalFormat.format(mOrder.getTotal_tax()));
        double total = mOrder.getTotal() + mOrder.getShipping_cost() + mOrder.getTotal_tax();
        tvTotal.setText(decimalFormat.format(total));

        OrderDetailAdapter myAdapter = new OrderDetailAdapter(mOrder.getProductsOrdered());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OrderDetailFragment.OnOrderDetailClickedListener) {
            mListener = (OrderDetailFragment.OnOrderDetailClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnOrderDetailClickedListener {
        void onOrderDetailClicked();
    }
}