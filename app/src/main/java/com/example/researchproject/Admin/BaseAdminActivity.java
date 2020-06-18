package com.example.researchproject.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.researchproject.Customer.OrdersCustomerActivity;
import com.example.researchproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

class BaseAdminActivity extends AppCompatActivity
{
    protected static final int FILE_REQUEST_CODE_ADD = 01;
    protected static final int FILE_REQUEST_CODE_UPDATE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void initToolbar(int toolbarId)
    {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);
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

}