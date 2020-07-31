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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegistrationActivity";
    EditText etFistName, etLastName, etEmail, etPassword1, etPassword2;
    Button btnRegister;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Components
        InitializeComponents();

        // Setting up listener
        btnRegister.setOnClickListener(this);

        //Create token for cloud messaging
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    token = task.getResult().getToken();
                    Log.e(TAG, token);
                });
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

            String validationResult = validateRegistrationData(firstName, lastName, email, password1, password2);

            if (!validationResult.equals("Success")) {
                Toast.makeText(RegistrationActivity.this, validationResult, Toast.LENGTH_LONG).show();
            } else {
                final Customer customer = new Customer(email, token, firstName, lastName, password1);
                Gson gson = new Gson();
                final String jsonInString = gson.toJson(customer);
//                String url = "https://myprojectstore.000webhostapp.com/customer/";
                String url = "http://100.25.155.48/customer/";

                VolleyService request = new VolleyService(this);
                request.executePostRequest(url, new VolleyService.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            if (response.equals("true")) {
                                Toast.makeText(RegistrationActivity.this, "You have successfully registered \n You will now be redirected to LogIn Page.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Your account already exits.", Toast.LENGTH_LONG).show();
                            }

                            final Intent loginIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            Thread thread = new Thread() {
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

                            Log.d(TAG, response);
                        } catch (Exception ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                    }
                }, "customer", jsonInString);
            }
        }
    }

    public String validateRegistrationData(String firstName, String lastName, String email, String password1, String password2) {
        String result = "";
        if (!email.isEmpty()) {
            if (!firstName.isEmpty() && !lastName.isEmpty()) {
                int length = password1.length();
                if (length >= 6) { //for testing but should be longer
                    if (isValidPassword(password1) == true) {
                        if (password1.equals(password2)) {
                            if (!isValidEmail(email)) {
                                result = "Invalid email";
                            } else {
                                result = "Success";
                            }
                        } else {
                            result = "Passwords don't match";
                        }
                    } else {
                        result = "Password should contain one uppercase, one number and one special character";
                    }
                } else {
                    result = "Password is too short";
                }
            } else {
                result = "Names can't be empty";
            }
        } else {
            result = "Email can't be empty";
        }
        return result;
    }

    public boolean isValidPassword(String password) {
        Pattern pattern;
        Matcher matcher;
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}
