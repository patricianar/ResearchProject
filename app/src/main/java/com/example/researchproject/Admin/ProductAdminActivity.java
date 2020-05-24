package com.example.researchproject.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.Customer.ProductCustomerActivity;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;


public class ProductAdminActivity extends AppCompatActivity {
    private static final String TAG = "ProductAdminActivity";
    private static final int FILE_REQUEST_CODE = 10;

    private ArrayList<Product> productsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_admin);

        Button btnImport = findViewById(R.id.btnImportProducts);
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCSVFile();
            }
        });
    }

    private void selectCSVFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, FILE_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();

                readCSVFromUri(uri);

                String url = "https://myprojectstore.000webhostapp.com/product/";

                Catalogue catalogue = new Catalogue();
                catalogue.setProducts(productsList);

                Gson gson = new Gson();
                String jsonCatalogue = gson.toJson(catalogue);

                VolleyService request = new VolleyService(ProductAdminActivity.this);
                request.executePostRequest(url, new VolleyService.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            if (response.equals("true")) {
                                Toast.makeText(ProductAdminActivity.this, "Your catalogue has been updated", Toast.LENGTH_SHORT).show();
                                final Intent productIntent = new Intent(ProductAdminActivity.this, ProductCustomerActivity.class);
//                                Thread thread = new Thread() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            Thread.sleep(3500); // wait before going to products
//                                            startActivity(productIntent);
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                };
//                                thread.start();
                            }
                            productsList.clear();
                            Log.d(TAG, response);
                        } catch (Exception ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                    }
                }, "products", jsonCatalogue);
            }
        }
    }

    private void readCSVFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream)));

            String line;
            if ((line = reader.readLine()) != null) {
//                //here csv Line contains the header line and can be used if needed
            }
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                Log.e("test", row[1]);
                String barcode = row[0];
                String brand = row[1];
                String category = row[2];
                Double costPrice = Double.parseDouble(row[3]);
                String description = row[4];
                int id = Integer.parseInt(row[5]);
                int invLevel = Integer.parseInt(row[6]);
                int invWarnLevel = Integer.parseInt(row[7]);
                String name = row[8];
                Double price = Double.parseDouble(row[9]);
                String urlImg = row[10];

                Product product = new Product(barcode, brand, category, costPrice, description, id, invLevel, invWarnLevel, name, price, urlImg);
                productsList.add(product);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file..." + ex.getMessage());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Error in id parsing..." + ex.getMessage());
        }
    }

}
