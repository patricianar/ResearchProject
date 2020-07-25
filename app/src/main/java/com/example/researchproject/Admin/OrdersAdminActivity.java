package com.example.researchproject.Admin;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchproject.Classes.Order;
import com.example.researchproject.OrderAdapter;
import com.example.researchproject.OrderDetailFragment;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

public class OrdersAdminActivity extends BaseAdminActivity implements OrderDetailFragment.OnOrderDetailClickedListener {
    private static final String TAG = "OrdersAdminActivity";
    VolleyService request;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_admin);

        initToolbar(R.id.toolbar);
        initBottomNavigationView(R.id.bottom_navigation, R.id.orders);

//        url = "https://myprojectstore.000webhostapp.com/order/";
        url = "http://100.25.155.48/order/";
        request = new VolleyService(this);
        getOrders();
    }

    //get products from firebase and display them in recycler view
    private void getOrders() {
        request.executeGetRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    Log.d("Response", response);
                    Gson gson = new Gson();
                    Order[] order = gson.fromJson(response, Order[].class);
                    OrderAdapter myAdapter = new OrderAdapter(order, "OrderAdmin");
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);
                    recyclerView.setLayoutManager(new LinearLayoutManager(OrdersAdminActivity.this));
                    recyclerView.setAdapter(myAdapter);
                    Log.d("Response", response);
                } catch (Exception ex) {
                    Log.e("Request: ", ex.getMessage());
                }
            }
        });

    }

    @Override
    public void onOrderDetailClicked() {

    }
}
