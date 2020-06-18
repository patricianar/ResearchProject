package com.example.researchproject.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.researchproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ru.nikartm.support.ImageBadgeView;

class BaseActivity extends AppCompatActivity
{
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        final MenuItem menuItem = menu.findItem(R.id.cart);
        //menuItem.setActionView(R.layout.actionbar_badge_layout);
        ImageBadgeView badgeView = menuItem.getActionView().findViewById(R.id.ibv_icon);
        badgeView.setBadgeValue(0);

        //we need this so we can get it onOptionsItemSelected
        menuItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.this.onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:
                //getSupportFragmentManager().beginTransaction().add(R.id.frameCustomer, CartFragment.newInstance())
                //      .addToBackStack(null).commit();
                startActivity(new Intent(this, CartActivity.class));
                return true;
            case R.id.logout:
                SharedPreferences sharedPref = getSharedPreferences("Cart", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();
                ///falta completar enviando a firebase el car
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                        startActivity(new Intent(getApplicationContext(), OrdersActivity.class));
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