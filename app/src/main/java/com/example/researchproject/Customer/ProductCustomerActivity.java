package com.example.researchproject.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

public class ProductCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_customer);

        String url = "https://myprojectstore.000webhostapp.com/product";

        VolleyService request = new VolleyService(this);
        request.executeGetRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    Gson gson = new Gson();
                    Catalogue catalogue = gson.fromJson(response , Catalogue.class);
                    ProductAdapter myAdapter = new ProductAdapter(catalogue.getProducts());
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
