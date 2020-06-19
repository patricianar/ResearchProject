package com.example.researchproject.Customer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import ru.nikartm.support.ImageBadgeView;

public class ProductCustomerActivity extends BaseCustomerActivity implements ProductCustomerAdapter.Callback {
    private static final String TAG = "ProductCustomerActivity";
    private ImageBadgeView badgeView;
    VolleyService request;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_customer);

        initToolbar(R.id.toolbar);
        initBottomNavigationView(R.id.bottom_navigation, R.id.home);

        url = "https://myprojectstore.000webhostapp.com/product/";
        request = new VolleyService(this);
        getProducts();
    }

    //get products from firebase and display them in recycler view
    private void getProducts() {
        request.executeGetRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    Gson gson = new Gson();
                    Catalogue catalogue = gson.fromJson(response, Catalogue.class);
                    ProductCustomerAdapter myAdapter = new ProductCustomerAdapter(catalogue.getProducts());
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ProductCustomerActivity.this));
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.setListener(ProductCustomerActivity.this);
                    Log.d("Response", response);
                } catch (Exception ex) {
                    Log.e("Request: ", ex.getMessage());
                }
            }
        });
    }

    @Override
    public void onAddClick(int id) {
//        final MenuItem menuItem = menu.findItem(R.id.cart);
        //menuItem.setActionView(R.layout.actionbar_badge_layout);
        ImageBadgeView badgeView = findViewById(R.id.ibv_icon);
        int newBadgeValue = badgeView.getBadgeValue() + 1;
        badgeView.setBadgeValue(newBadgeValue);

        SharedPreferences sharedPrefCart = getSharedPreferences("Cart", MODE_PRIVATE);
        SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
        int qty = sharedPrefCart.getInt(String.valueOf(id), 0);
        //add qty of products
        qty++;
        SharedPreferences.Editor editorCart = sharedPrefCart.edit();
        editorCart.putInt(String.valueOf(id), qty);
        editorCart.apply();
        SharedPreferences.Editor editorUser = sharedPrefUser.edit();
        int totalItems = sharedPrefUser.getInt("Items", 0);
        editorUser.putInt("Items", totalItems + 1);
        editorUser.apply();
    }
}
