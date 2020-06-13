package com.example.researchproject.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import ru.nikartm.support.ImageBadgeView;

public class ProductCustomerActivity extends AppCompatActivity implements ProductCustomerAdapter.Callback {
    private static final String TAG = "ProductCustomerActivity";
    private ImageBadgeView badgeView;
    VolleyService request;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_customer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.mipmap.ic_launcher_round);

        url = "http://100.25.155.48/product/";
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);
        //menuItem.setActionView(R.layout.actionbar_badge_layout);
        badgeView = menuItem.getActionView().findViewById(R.id.ibv_icon);
        badgeView.setBadgeValue(0);
        menuItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductCustomerActivity.this.onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("test", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {

            case R.id.cart:
                //textViewContent.setText("Search was selected");
                Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAddClick(int id) {
        int newBadgeValue = badgeView.getBadgeValue() + 1;
        badgeView.setBadgeValue(newBadgeValue);

        String url = "http://100.25.155.48/customer/";

        VolleyService request = new VolleyService(this);
        request.executePostRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
//                    if (response.equals("true")) {
//                        Toast.makeText(getContext(), "New Product has been added!", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(getContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
//                    }
                    Log.d(TAG, response);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }, "addProductToCart", String.valueOf(id));
    }
}
