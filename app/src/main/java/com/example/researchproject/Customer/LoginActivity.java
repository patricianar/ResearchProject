package com.example.researchproject.Customer;

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
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvLinkRegister;

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

            String url = "https://myprojectstore.000webhostapp.com/customer/";

            VolleyService request = new VolleyService(this);
            request.executePostRequest(url, new VolleyService.VolleyCallback() {
                @Override
                public void getResponse(String response) {
                    try {
                        if(response.equals("false")){
                            Toast.makeText(LoginActivity.this, "Your account or password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Hello " + response, Toast.LENGTH_SHORT).show();
                            if(email.equals("admin@gmail.com")){
                                startActivity(new Intent(LoginActivity.this, ProductAdminActivity.class));
                            }else{
                                SharedPreferences sharedPref = getSharedPreferences("User", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("Email", email);
                                editor.apply();
                                startActivity(new Intent(LoginActivity.this, ProductCustomerActivity.class));
                            }
                        }
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            }, "customerInfo", email + "," + password);
        }
        else {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        }
    }
}
