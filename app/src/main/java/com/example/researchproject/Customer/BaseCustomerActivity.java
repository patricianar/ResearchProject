package com.example.researchproject.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.researchproject.Admin.ProductAdminActivity;
import com.example.researchproject.Classes.Cart;
import com.example.researchproject.Classes.ProductOrdered;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.nikartm.support.ImageBadgeView;

class BaseCustomerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(int toolbarId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.mipmap.ic_launcher_round);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);
        //menuItem.setActionView(R.layout.actionbar_badge_layout);
        ImageBadgeView badgeView = menuItem.getActionView().findViewById(R.id.ibv_icon);
        SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
        badgeView.setBadgeValue(sharedPrefUser.getInt("Items", 0));

        //we need this so we can get it onOptionsItemSelected
        menuItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseCustomerActivity.this.onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ImageBadgeView badgeView = findViewById(R.id.ibv_icon);
        if(badgeView != null){
            SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
            badgeView.setBadgeValue(sharedPrefUser.getInt("Items", 0));
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                startActivity(new Intent(this, CartActivity.class));
                return true;
            case R.id.logout:
                postCart();
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void postCart() {
        String url = "https://myprojectstore.000webhostapp.com/customer/cart/";

        //gets the contents of shared preferences and save them to firebase if user logs out
        final SharedPreferences sharedPrefCart = getSharedPreferences("Cart", MODE_PRIVATE);
        final SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
        Map<String, ?> keys = sharedPrefCart.getAll();

        Cart cart = new Cart();
        cart.setEmail(sharedPrefUser.getString("Email", ""));

        List<ProductOrdered> productsCart = new ArrayList<>();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            ProductOrdered product = new ProductOrdered();
            product.setId(Integer.parseInt(entry.getKey()));
            product.setQuantity((Integer)entry.getValue());
            productsCart.add(product);
        }
        cart.setProductsCart(productsCart);

        SharedPreferences.Editor editorUser = sharedPrefUser.edit();
        editorUser.clear();
        editorUser.apply();

        Gson gson = new Gson();

        final String jsonInString = gson.toJson(cart);
        VolleyService request = new VolleyService(this);
        request.executePostRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    if (response.contains("success")) {
                        SharedPreferences.Editor editorCart = sharedPrefCart.edit();
                        editorCart.clear();
                        editorCart.apply();
                    }
                    Log.d("PostCart", response);
                } catch (Exception ex) {
                    Log.e("PostCart", ex.getMessage());
                }
            }
        }, "saveCart", jsonInString);
    }

    /**
     * Init BottomNavigationView with 4 items:
     * home, orders, notifications, account
     */
    public void initBottomNavigationView(int navigationId, int selectedItem) {
        //Initialize and Assign variable
        BottomNavigationView bottomNavigationView = findViewById(navigationId);

        //Set Result Selector
        bottomNavigationView.setSelectedItemId(selectedItem);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), ProductCustomerActivity.class));
                        finish(); // avoid going back to the same selected tab many times - save memory
                        return true;
                    case R.id.orders:
                        startActivity(new Intent(getApplicationContext(), OrdersCustomerActivity.class));
                        finish();
                        return true;
                    case R.id.notification:
                        //startActivity(new Intent(getApplicationContext(), EventsActivity.class));
                        //finish();
                        return true;
                    case R.id.account:
                        //startActivity(new Intent(getApplicationContext(), MyEventsActivity.class));
                        //finish();
                        return true;
                }
                return false;
            }
        });
    }
}