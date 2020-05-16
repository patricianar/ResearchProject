package com.example.researchproject.Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_customer);

        String url = "https://myprojectstore.000webhostapp.com/";
        Log.e("here", "here");


        VolleyService request = new VolleyService(this);
        request.executeRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    //JSONObject jsonObj = new JSONObject(response);
                    //JSONArray products = jsonObj.getJSONArray("products");
                    Gson gson = new Gson();
                    Product[] products = gson.fromJson(response , Product[].class);
                    //Product[] vmNamesArray = gson.fromJson(response, Product[].class);
                    Log.e("response", "h" + products[0].getReview_message());

                    List<Product> productsList = new ArrayList<Product>(Arrays.asList(products));
                    ProductAdapter myAdapter = new ProductAdapter(productsList);
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ProductCustomerActivity.this));
                    recyclerView.setAdapter(myAdapter);

                } catch (Exception ex) {
                    Log.e("Request: ", ex.getMessage());
                }
            }
        });



    }
}
