package com.example.researchproject.Customer;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.researchproject.Admin.AddProductFragment;
import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.PictureFragment;
import com.example.researchproject.R;
import com.example.researchproject.TestCamActivity;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

//        url = "https://myprojectstore.000webhostapp.com/product/";
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                if (requestCode == REQUEST_CAMERA) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
//                        String url = "https://myprojectstore.000webhostapp.com/";
                    String url = "http://100.25.155.48/";

                    //Converting Bitmap to string
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    assert photo != null;
                    photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                    VolleyService request = new VolleyService(ProductCustomerActivity.this);
                    request.executePostRequest(url, new VolleyService.VolleyCallback() {
                        @Override
                        public void getResponse(String response) {
                            try {
                                if (response.equals("true")) {
                                    //Toast.makeText(getContext(), "New Product has been added!", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Toast.makeText(getContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                                }
                                Log.e(TAG, response);
                                Toast.makeText(ProductCustomerActivity.this, response, Toast.LENGTH_LONG).show();
                            } catch (Exception ex) {
                                Log.e(TAG, ex.getMessage());
                            }
                        }
                    }, "searchByImage", encodedImage);
                }
            }
        }
    }

    @Override
    public void onEnter(String word) {
        if (word.isEmpty()) {
            Toast.makeText(ProductCustomerActivity.this, "Please enter a word to start searching!", Toast.LENGTH_LONG).show();
        } else {
            String url = "http://100.25.155.48/";
            request.executePostRequest(url, response -> {
                try {
                    Log.d(TAG, response);
                    if (response.equals("no results")) {
                            //display nothing found
                    } else {
                        Gson gson = new Gson();
                        Catalogue catalogue = gson.fromJson(response, Catalogue.class);
                        ProductCustomerAdapter myAdapter = new ProductCustomerAdapter(catalogue.getProducts());
                        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ProductCustomerActivity.this));
                        recyclerView.setAdapter(myAdapter);
                        myAdapter.setListener(ProductCustomerActivity.this);
                    }
                    Log.d(TAG, response);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }, "searchProductsByName", word.trim());
        }
    }
}
