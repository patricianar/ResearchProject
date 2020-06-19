package com.example.researchproject.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.researchproject.Admin.AddProductFragment;
import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.Classes.Order;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.Classes.ProductOrdered;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import ru.nikartm.support.ImageBadgeView;

public class CartActivity extends AppCompatActivity implements ProductCartAdapter.CallbackCart {
    private static final String TAG = "CartActivity";
    RecyclerView recyclerView;
    TextView tvSubtotal;
    SharedPreferences sharedPrefCart;
    VolleyService request;
    List<Product> productsList;
    DecimalFormat decimalFormat;
    double subTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        ImageView imgBack = findViewById(R.id.imgBack);
        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        sharedPrefCart = getSharedPreferences("Cart", MODE_PRIVATE);
        request = new VolleyService(this);
        decimalFormat = new DecimalFormat("$#.##");
        getProducts();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOrder();
            }
        });
    }

    private void postOrder() {
        String url = "https://myprojectstore.000webhostapp.com/order/";

        Order order = new Order();
        Customer customer = new Customer();
        final SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
        customer.setEmail(sharedPrefUser.getString("Email", ""));
        order.setCustomer(customer);

        List<ProductOrdered> prodOrderedList = new ArrayList<>();

        for (Product product : productsList) {
            int qty = sharedPrefCart.getInt(String.valueOf(product.getId()), 0);
            ProductOrdered prodOrdered = new ProductOrdered();
            prodOrdered.setId(product.getId());
            prodOrdered.setName(product.getName());
            prodOrdered.setPrice(product.getPrice());
            prodOrdered.setQuantity(qty);
            prodOrderedList.add(prodOrdered);
        }
        order.setProductsOrdered(prodOrderedList);

        double tax = subTotal * 0.05;
        double total = subTotal + tax;
        order.setTotal(total);
        order.setTotal_tax(tax);

        Gson gson = new Gson();
        final String jsonInString = gson.toJson(order);
        request.executePostRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    if (response.equals("true")) {
                        Toast.makeText(CartActivity.this, "Your order has been placed!", Toast.LENGTH_SHORT).show();
                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    sharedPrefCart = getSharedPreferences("Cart", MODE_PRIVATE);
                                    SharedPreferences.Editor editorCart = sharedPrefCart.edit();
                                    editorCart.clear();
                                    editorCart.apply();
                                    SharedPreferences.Editor editorUser = sharedPrefUser.edit();
                                    editorUser.clear();
                                    editorUser.apply();
                                    Thread.sleep(2500); // wait before going to order
                                    startActivity(new Intent(CartActivity.this, OrdersCustomerActivity.class));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        thread.start();
                    } else {
                        Toast.makeText(CartActivity.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, response);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }, "newOrder", jsonInString);
    }

    private void getProducts() {
        String url = "https://myprojectstore.000webhostapp.com/product/";
        String productsIds = "";

        //gets the contents of shared preferences
        Map<String, ?> keys = sharedPrefCart.getAll();

        if (sharedPrefCart.getAll().isEmpty()) {

        } else {
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                productsIds += entry.getKey() + ",";
            }
            productsIds = productsIds.substring(0, productsIds.length() - 1);
            Log.e("test", productsIds);

            request.executePostRequest(url, new VolleyService.VolleyCallback() {
                @Override
                public void getResponse(String response) {
                    try {
                        if (response.contains("products")) {
                            Gson gson = new Gson();
                            Catalogue catalogue = gson.fromJson(response, Catalogue.class);
                            productsList = catalogue.getProducts();
                            ProductCartAdapter myAdapter = new ProductCartAdapter(productsList);
                            myAdapter.setListener(CartActivity.this);
                            recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                            recyclerView.setAdapter(myAdapter);
                            for (Product product : catalogue.getProducts()) {
                                int qty = sharedPrefCart.getInt(String.valueOf(product.getId()), 0);
                                subTotal += qty * product.getPrice();
                            }
                            tvSubtotal.setText(decimalFormat.format(subTotal));
                            //myAdapter.setListener(ProductCustomerActivity.this);
                        } else {
                            Toast.makeText(CartActivity.this, "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG, response);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            }, "productsCart", productsIds);
        }
    }

    @Override
    public void onAddClick(double total) {
        subTotal += total;
        tvSubtotal.setText(decimalFormat.format(subTotal));
    }

    @Override
    public void onRemoveClick(double total) {
        subTotal -= total;
        tvSubtotal.setText(decimalFormat.format(subTotal));
    }
}
