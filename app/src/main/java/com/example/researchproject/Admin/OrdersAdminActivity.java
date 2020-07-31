package com.example.researchproject.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchproject.Classes.Order;
import com.example.researchproject.Customer.CartActivity;
import com.example.researchproject.Customer.ProductCustomerActivity;
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
        request.executeGetRequest(url, response -> {
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
        });

    }

    @Override
    public void onOrderDetailClicked(String statusInfo) {
        postStatusUpdate(statusInfo);
    }

    private void postStatusUpdate(String statusInfo) {
        String url = "http://100.25.155.48/order/";

        request.executePostRequest(url, response -> {
            try {
                Log.e("test", response);
                if (response.equals("true")) {
                    Toast.makeText(OrdersAdminActivity.this, "Customer notified", Toast.LENGTH_SHORT).show();
                    getOrders();
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000);
                                getSupportFragmentManager().popBackStack();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                } else {
                    Toast.makeText(OrdersAdminActivity.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, response);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }, "statusChange", statusInfo);
    }
}
