package com.example.researchproject.Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.researchproject.Classes.Products;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

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
                    Products products = gson.fromJson(response , Products.class);
                    //Product[] vmNamesArray = gson.fromJson(response, Product[].class);
                    Log.e("response", "h" + products.getProducts().get(0).getReview_message());

                    ProductAdapter myAdapter = new ProductAdapter(products.getProducts());
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
