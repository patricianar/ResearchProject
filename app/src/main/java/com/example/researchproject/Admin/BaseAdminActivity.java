package com.example.researchproject.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.researchproject.Classes.Customer;
import com.example.researchproject.Customer.LoginActivity;
import com.example.researchproject.Customer.BaseCustomerActivity;
import com.example.researchproject.R;
import com.example.researchproject.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

class BaseAdminActivity extends AppCompatActivity implements SearchFragment.OnSearchListener
{
    protected static final int FILE_REQUEST_CODE_ADD = 01;
    protected static final int FILE_REQUEST_CODE_UPDATE = 10;
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    protected static final int REQUEST_CAMERA = 100;
    protected static final int SELECT_FILE = 11;
    private Toolbar toolbar;
    protected SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(int toolbarId)
    {
        toolbar = findViewById(toolbarId);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.mipmap.ic_launcher_round);
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
                searchFragment = new SearchFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.prodDetailFrag, searchFragment).addToBackStack(null).commit();
                toolbar.setVisibility(View.INVISIBLE);
                return true;
            case R.id.logout:
                startActivity(new Intent(this, LoginActivity.class));
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
                        startActivity(new Intent(getApplicationContext(), ProductAdminActivity.class));
                        finish(); // avoid going back to the same selected tab many times - save memory
                        return true;
                    case R.id.orders:
                        startActivity(new Intent(getApplicationContext(), OrdersAdminActivity.class));
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

    @Override
    public void onClose() {
        toolbar.setVisibility(View.VISIBLE);
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onEnter(String word) {

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
}