package com.example.researchproject.Customer;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.researchproject.Classes.Cart;
import com.example.researchproject.Classes.ProductOrdered;
import com.example.researchproject.R;
import com.example.researchproject.SearchFragment;
import com.example.researchproject.VolleyService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ru.nikartm.support.ImageBadgeView;

public class BaseCustomerActivity extends AppCompatActivity implements SearchFragment.OnSearchListener {
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    protected static final int REQUEST_CAMERA = 10;
    protected static final int SELECT_FILE = 11;
    private Toolbar toolbar;
    protected SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(int toolbarId) {
        toolbar = (Toolbar) findViewById(toolbarId);
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
            case R.id.search:
                searchFragment = new SearchFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.frameCustomer, searchFragment).addToBackStack(null).commit();
                toolbar.setVisibility(View.INVISIBLE);
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

    public void showOptions(){
        if (allPermissionsGranted()) {
            final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("How would you like to search?");
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent,SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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
                        startActivity(new Intent(getApplicationContext(), AccountCustomerActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClose() {
        toolbar.setVisibility(View.VISIBLE);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onEnter(String word){
    }

    @Override
    public void onClick() {
        showOptions();
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}