package com.example.researchproject.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.Classes.Order;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.Classes.ProductOrdered;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";
    RecyclerView recyclerView;
    TextView tvSubtotal;
    SharedPreferences sharedPref;
    List<Product> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getProducts();
        recyclerView = findViewById(R.id.recyclerViewCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        ImageView imgBack = findViewById(R.id.imgBack);
        Button btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        sharedPref = getSharedPreferences("Cart", MODE_PRIVATE);

         imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //mListener.onClose();
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
        SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
        customer.setEmail(sharedPrefUser.getString("Email", ""));
        order.setCustomer(customer);

        List<ProductOrdered> prodOrderedList = new ArrayList<>();

        for(Product product: productsList){
            int qty = sharedPref.getInt(String.valueOf(product.getId()), 0);
            ProductOrdered prodOrdered = new ProductOrdered();
            prodOrdered.setId(product.getId());
            prodOrdered.setName(product.getName());
            prodOrdered.setPrice(product.getPrice());
            prodOrdered.setQuantity(qty);
            prodOrderedList.add(prodOrdered);
        }

//        private String customer_message;
//        private String date_created;
//        private String date_shipped;
//        private int id;
//        private String payment_method;
//        private List<ProductOrdered> productsOrdered;
//        private Double shipping_cost;
//        private String status;
//        private Double total;
//        private Double total_tax;

    }

    private void getProducts() {
        String url = "https://myprojectstore.000webhostapp.com/product/";
        String productsIds = "";

        //gets the contents of shared preferences
        Map<String, ?> keys = sharedPref.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            productsIds += entry.getKey() + ",";
        }
        productsIds = productsIds.substring(0, productsIds.length() - 1);
        Log.e("test", productsIds);

        VolleyService request = new VolleyService(this);
        request.executePostRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    if (response.contains("products")) {
                        Gson gson = new Gson();
                        Catalogue catalogue = gson.fromJson(response, Catalogue.class);
                        productsList = catalogue.getProducts();
                        ProductCartAdapter myAdapter = new ProductCartAdapter(productsList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                        recyclerView.setAdapter(myAdapter);
                        double subTotal = 0;
                        for(Product product: catalogue.getProducts()){
                            int qty = sharedPref.getInt(String.valueOf(product.getId()), 0);
                            subTotal += qty * product.getPrice();
                        }
                        tvSubtotal.setText(String.format("$%.2f", subTotal));
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
