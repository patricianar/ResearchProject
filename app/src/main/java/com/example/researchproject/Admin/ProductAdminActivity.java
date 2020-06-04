package com.example.researchproject.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.Customer.ProductCustomerActivity;
import com.example.researchproject.R;
import com.example.researchproject.SwipeToDeleteCallback;
import com.example.researchproject.VolleyService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class ProductAdminActivity extends AppCompatActivity implements ProductDetailFragment.OnCardViewClickedListener,
        AddProductFragment.OnCardViewClickedListener{
    private static final String TAG = "ProductAdminActivity";
    private static final int FILE_REQUEST_CODE_ADD = 01;
    private static final int FILE_REQUEST_CODE_UPDATE = 10;
    private ArrayList<Product> productsList = new ArrayList<>();
    ConstraintLayout constraintLayout;
    RecyclerView recyclerView;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    VolleyService request;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_admin);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.mipmap.ic_launcher_round);

        constraintLayout = findViewById(R.id.layoutProdAdmin);

        // Initialize BottomNavigationView
        initBottomNavigationView();

        url = "https://myprojectstore.000webhostapp.com/product/";
        request = new VolleyService(ProductAdminActivity.this);
        getProducts();

        fab = findViewById(R.id.fab);

        //click listeners needs to be customized
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().add(R.id.prodDetailFrag, AddProductFragment.newInstance("1", "1")).commit();
            }
        });
    }



    private void getProducts() {
        request.executeGetRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    Gson gson = new Gson();
                    Catalogue catalogue = gson.fromJson(response, Catalogue.class);
                    ProductAdminAdapter myAdapter = new ProductAdminAdapter(catalogue.getProducts());
                    recyclerView = findViewById(R.id.recyclerViewProductsAdmin);
                    recyclerView.setLayoutManager(new LinearLayoutManager(ProductAdminActivity.this));
                    recyclerView.setAdapter(myAdapter);
                    enableSwipeToDeleteAndUndo(myAdapter);
                    Log.d(TAG, response);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        });
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                //textViewContent.setText("Search was selected");
                //Toast.makeText(this, "Search", Toast.LENGTH_LONG).show();
                return true;
            case R.id.orders:
//                textViewContent.setText("Delete was selected");
//                Toast.makeText(this, "Delete", Toast.LENGTH_LONG).show();
                return true;
            case R.id.addProducts:
                selectCSVFile(FILE_REQUEST_CODE_ADD);
                return true;
            case R.id.updateProducts:
                selectCSVFile(FILE_REQUEST_CODE_UPDATE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectCSVFile(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == FILE_REQUEST_CODE_ADD && resultCode == RESULT_OK) {
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
                            if (response.contains("true")) {
                                Toast.makeText(ProductAdminActivity.this, "Your catalogue has been updated", Toast.LENGTH_SHORT).show();
                                getProducts();
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

    private void enableSwipeToDeleteAndUndo(final ProductAdminAdapter myAdapter) {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Product product = myAdapter.getData().get(position);

                myAdapter.removeItem(position);

                Snackbar snackbar = Snackbar.make(constraintLayout, "Product has been removed from the store", Snackbar.LENGTH_LONG)
                        .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                if (event != DISMISS_EVENT_ACTION) {
                                    // Dismiss wasn't because of tapping "UNDO"
                                    // so here delete the item from databse
                                    request.executePostRequest(url, new VolleyService.VolleyCallback() {
                                        @Override
                                        public void getResponse(String response) {
                                            try {
                                                if (response.equals("success")) {
                                                    //Toast.makeText(RegistrationActivity.this, "You have successfully registered \n You will now be redirected to LogIn Page.", Toast.LENGTH_LONG).show();
                                                } else {
                                                    //Toast.makeText(RegistrationActivity.this, "Your account already exits.", Toast.LENGTH_LONG).show();
                                                }
                                                Log.d(TAG, response);
                                            } catch (Exception ex) {
                                                Log.e(TAG, ex.getMessage());
                                            }
                                        }
                                    }, "productDelete", String.valueOf(product.getId()));
                                }
                            }
                        })
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                myAdapter.restoreItem(product, position);
                                recyclerView.scrollToPosition(position);
                            }
                        });
                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    /**
     * Init BottomNavigationView with 4 items:
     * home, orders, notifications, account
     */
    private void initBottomNavigationView() {
        //Initialize and Assign variable
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Result Selector
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), ProductAdminActivity.class));
                        finish(); // avoid going back to the same selected tab many times - save memory
                        return true;
                    case R.id.orders:
                        //startActivity(new Intent(getApplicationContext(), RegisterEvents.class));
                        //finish();
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

    @Override
    public void onOpen() {
        bottomNavigationView.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onClose() {
        bottomNavigationView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
