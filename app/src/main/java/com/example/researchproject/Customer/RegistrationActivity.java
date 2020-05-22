package com.example.researchproject.Customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity  implements View.OnClickListener{
    private static final String TAG = "RegistrationActivity";
    EditText etFistName, etLastName, etEmail, etPassword1, etPassword2;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Components
        InitializeComponents();

        // Setting up listener
        btnRegister.setOnClickListener(this);
    }

    private void InitializeComponents() {
        etFistName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPassword1 = findViewById(R.id.etPassword1);
        etPassword2 = findViewById(R.id.etPassword2);
        btnRegister = findViewById(R.id.btnRegister);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister) {
            String firstName = etFistName.getText().toString();
            String lastName = etLastName.getText().toString();
            String email = etEmail.getText().toString();
            String password1 = etPassword1.getText().toString();
            String password2 = etPassword2.getText().toString();

            final Customer customer = new Customer(email, firstName, lastName, password1);
            Gson gson = new Gson();
            final String jsonInString = gson.toJson(customer);
            String url = "https://myprojectstore.000webhostapp.com/customer/";

            VolleyService request = new VolleyService(this);
            request.executePostRequest(url, new VolleyService.VolleyCallback() {
                @Override
                public void getResponse(String response) {
                    try {
                        if(response.equals("true")){
                            Toast.makeText(RegistrationActivity.this, "You have successfully registered \n You will now be redirected to LogIn Page.", Toast.LENGTH_LONG).show();
                            final Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);

                            Thread thread = new Thread(){
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3500); // wait before going to login
                                        startActivity(loginIntent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            thread.start();
                        }
                        Log.d(TAG, response);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            }, "customer", jsonInString);
        }
    }
}
