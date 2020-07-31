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

import com.example.researchproject.Classes.Address;
import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.Classes.Order;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.Classes.ProductOrdered;
import com.example.researchproject.MessageFragment;
import com.example.researchproject.OrderDetailFragment;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements ProductCartAdapter.CallbackCart,
        AccountFragment.OnAccountClickedListener, PaymentFragment.OnPaymentClickedListener, MapsFragment.OnMapsListener,
        OrderDetailFragment.OnOrderDetailClickedListener {
    private static final String TAG = "CartActivity";
    RecyclerView recyclerView;
    TextView tvSubtotal;
    SharedPreferences sharedPrefCart;
    VolleyService request;
    List<Product> productsList;
    DecimalFormat decimalFormat;
    AccountFragment accountFragment;
    Order order;
    double subTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        ImageView imgBack = findViewById(R.id.imgBack);
        Button btnPlaceOrder = findViewById(R.id.btnContinue);
        sharedPrefCart = getSharedPreferences("Cart", MODE_PRIVATE);
        request = new VolleyService(this);
        decimalFormat = new DecimalFormat("$#.##");
        order = new Order();
        getProducts();

        imgBack.setOnClickListener(view -> finish());

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountFragment = new AccountFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.frameCart, accountFragment).addToBackStack(null).commit();
            }
        });
    }

    private void fillOrder() {
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

        double shippingCost = 7.99 + subTotal * 0.01;
        double tax = (subTotal + shippingCost) * 0.05;
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        order.setTotal(subTotal);
        order.setTotal_tax(tax);
        order.setShipping_cost(shippingCost);
        order.setDate_created(currentDate);
    }

    private void postOrder() {
        final SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
//        String url = "https://myprojectstore.000webhostapp.com/order/";
        String url = "http://100.25.155.48/order/";

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
                                    editorUser.remove("Items");
                                    editorUser.apply();
                                    Thread.sleep(2500);
                                    startActivity(new Intent(CartActivity.this, ProductCustomerActivity.class));
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
        String url = "http://100.25.155.48/product/";
        String productsIds = "";

        //gets the contents of shared preferences
        Map<String, ?> keys = sharedPrefCart.getAll();

        if (sharedPrefCart.getAll().isEmpty()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameCart, MessageFragment.newInstance(R.drawable.logo, "cart")).commit();

        } else {
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                productsIds += entry.getKey() + ",";
            }
            productsIds = productsIds.substring(0, productsIds.length() - 1);
            Log.e("test", productsIds);

            request.executePostRequest(url, response -> {
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
        if (subTotal == 0) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameCart, MessageFragment.newInstance(R.drawable.logo, "cart")).commit();
        }
    }

    @Override
    public void onAddressClicked() {
        getSupportFragmentManager().beginTransaction().add(R.id.frameCart, new MapsFragment()).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().remove(accountFragment).commit();
    }

    @Override
    public void onAccountClicked(Customer customer) {
        getSupportFragmentManager().beginTransaction().add(R.id.frameCart, PaymentFragment.newInstance(customer, "Cart")).addToBackStack(null).commit();
    }

    @Override
    public void onPaymentClicked(Customer customer) {
        order.setCustomer(customer);
        fillOrder();
        getSupportFragmentManager().beginTransaction().add(R.id.frameOrder, OrderDetailFragment.newInstance(order, "Cart")).addToBackStack(null).commit();
    }

    @Override
    public void onVerifyAddress(Address address) {
        getSupportFragmentManager().beginTransaction().add(R.id.frameCart, AccountFragment.newInstance(address)).addToBackStack(null).commit();
    }

    @Override
    public void onOrderDetailClicked(String statusInfo) {
        postOrder();
    }
}
