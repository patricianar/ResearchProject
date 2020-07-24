package com.example.researchproject.Customer;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.researchproject.Classes.Address;
import com.example.researchproject.Classes.Card;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

public class AccountCustomerActivity extends BaseCustomerActivity implements  AccountFragment.OnAccountClickedListener,
        MapsFragment.OnMapsListener, PaymentFragment.OnPaymentClickedListener {
    private static final String TAG = "AccountCustomerActivity";
    AccountFragment accountFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_customer);

        initToolbar(R.id.toolbar);
        initBottomNavigationView(R.id.bottom_navigation, R.id.account);

        accountFragment = new AccountFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frameAccount, accountFragment).commit();

    }

    @Override
    public void onAddressClicked() {
        getSupportFragmentManager().beginTransaction().add(R.id.frameAccount, new MapsFragment()).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().remove(accountFragment).commit();
    }

    @Override
    public void onAccountClicked(Customer customer) {
        getSupportFragmentManager().beginTransaction().addToBackStack("AccountFrag")
                .add(R.id.frameAccount, PaymentFragment.newInstance(customer, "Account")).commit();
    }

    @Override
    public void onVerifyAddress(Address address) {
        getSupportFragmentManager().beginTransaction().add(R.id.frameAccount, AccountFragment.newInstance(address)).commit();
    }

    @Override
    public void onPaymentClicked(Customer customer) {
        updateCustomer(customer);
    }

    private void updateCustomer(Customer customer){
        Gson gson = new Gson();
        final String jsonInString = gson.toJson(customer);
        VolleyService request = new VolleyService(AccountCustomerActivity.this);
        String url = "http://100.25.155.48/customer/";
        request.executePostRequest(url, response -> {
            try {
                Toast.makeText(AccountCustomerActivity.this, "Your account has been updated!", Toast.LENGTH_LONG).show();
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2500); // wait before going to login
                            getSupportFragmentManager().popBackStack();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                Log.d(TAG, response);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }, "addCustomerDetails", jsonInString);
    }
}