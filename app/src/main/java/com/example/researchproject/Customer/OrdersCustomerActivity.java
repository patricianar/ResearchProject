package com.example.researchproject.Customer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.researchproject.Classes.Order;
import com.example.researchproject.OrderAdapter;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

public class OrdersCustomerActivity extends BaseCustomerActivity {
    private static final String TAG = "OrdersCustomerActivity";
    VolleyService request;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_customer);

        initToolbar(R.id.toolbar);
        initBottomNavigationView(R.id.bottom_navigation, R.id.orders);

//        url = "https://myprojectstore.000webhostapp.com/order/";
        url = "http://100.25.155.48/order/";
        request = new VolleyService(this);
        getOrders();
    }

    //get products from firebase and display them in recycler view
    private void getOrders() {
        SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
        String email = sharedPrefUser.getString("Email", "");

        request.executePostRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    Gson gson = new Gson();
                    Order[] order = gson.fromJson(response, Order[].class);
                    OrderAdapter myAdapter = new OrderAdapter(order);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewOrders);
                    recyclerView.setLayoutManager(new LinearLayoutManager(OrdersCustomerActivity.this));
                    recyclerView.setAdapter(myAdapter);
                    Log.d("Response", response);
                } catch (Exception ex) {
                    Log.e("Request: ", ex.getMessage());
                }
            }
        }, "getOrderByEmail", email);

    }
}
