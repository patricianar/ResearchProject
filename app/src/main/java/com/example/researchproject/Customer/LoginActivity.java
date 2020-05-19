package com.example.researchproject.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.researchproject.Classes.Customer;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

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
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            String url = "https://myprojectstore.000webhostapp.com/customer/";

            VolleyService request = new VolleyService(this);
            request.executePostRequest(url, new VolleyService.VolleyCallback() {
                @Override
                public void getResponse(String response) {
                    try {
                        Log.e(TAG, response);
                    } catch (Exception ex) {
                        Log.e("Request: ", ex.getMessage());
                    }
                }
            }, "customerInfo", email + "," + password);
        }
    }
}
