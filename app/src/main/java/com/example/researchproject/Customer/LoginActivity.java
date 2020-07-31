package com.example.researchproject.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.researchproject.Admin.ProductAdminActivity;
import com.example.researchproject.Classes.Cart;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.Classes.Order;
import com.example.researchproject.Classes.ProductOrdered;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvLinkRegister;
    int totalItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Components
        InitializeComponents();

        // Setting up listener
        btnLogin.setOnClickListener(this);
        tvLinkRegister.setOnClickListener(this);

    }

    private void InitializeComponents() {
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvLinkRegister = findViewById(R.id.tvLinkRegister);
    }

    @Override
    public void onClick(View v) {
        if (v == btnLogin) {
            final String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

//            String url = "https://myprojectstore.000webhostapp.com/customer/";
            String url = "http://100.25.155.48/customer/";

            VolleyService request = new VolleyService(this);
            request.executePostRequest(url, new VolleyService.VolleyCallback() {
                @Override
                public void getResponse(String response) {
                    try {
                        if (response.equals("false")) {
                            Toast.makeText(LoginActivity.this, "Your account or password is incorrect.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Hello " + response, Toast.LENGTH_SHORT).show();
                            updateToken(email);
                            getCart();
                            if (email.equals("admin@gmail.com")) {
                                startActivity(new Intent(LoginActivity.this, ProductAdminActivity.class));
                            } else {
                                Thread.sleep(500);
                                startActivity(new Intent(LoginActivity.this, ProductCustomerActivity.class));
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            }, "customerInfo", email + "," + password);
        } else {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        }
    }

    //getting cart from firebase and writing it to shared preferences
    private void getCart() {
        final SharedPreferences sharedPrefCart = getSharedPreferences("Cart", MODE_PRIVATE);
        SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
        final SharedPreferences.Editor editorUser = sharedPrefUser.edit();
        editorUser.putString("Email", etEmail.getText().toString());

        if(!etEmail.getText().toString().equals("admin@gmail.com") ) {
            if (sharedPrefCart.getAll().isEmpty()) {
                String url = "https://myprojectstore.000webhostapp.com/customer/cart/";

                VolleyService request = new VolleyService(this);
                request.executePostRequest(url, response -> {
                    try {
                        Gson gson = new Gson();
                        ProductOrdered[] productsCart = gson.fromJson(response, ProductOrdered[].class);
                        SharedPreferences.Editor editorCart = sharedPrefCart.edit();
                        for (int i = 0; i < productsCart.length; i++) {
                            editorCart.putInt(String.valueOf(productsCart[i].getId()),
                                    productsCart[i].getQuantity());
                            editorCart.apply();
                            totalItems += productsCart[i].getQuantity();
                        }
                        editorUser.putInt("Items", totalItems);
                        editorUser.apply();
                        Log.d(TAG, response);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage() + response);
                    }
                }, "getCartByEmail", etEmail.getText().toString());
            }
        }
        editorUser.apply();
    }

    //send token to update admin user
    private void updateToken(String email){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    String url = "http://100.25.155.48/customer/";
                    VolleyService request = new VolleyService(this);
                    request.executePostRequest(url, response -> {
                        try {
                            Log.d(TAG, response);
                        } catch (Exception ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                    }, "updateToken", email + "," + token);
                });
    }
}
