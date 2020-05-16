package com.example.researchproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.researchproject.Product.ProductCustomerActivity;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private EditText usernameField,passwordField;
    private TextView status,role,method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        usernameField = (EditText)findViewById(R.id.editText1);
        passwordField = (EditText)findViewById(R.id.editText2);

        status = (TextView)findViewById(R.id.textView6);
        role = (TextView)findViewById(R.id.textView7);
        method = (TextView)findViewById(R.id.textView9);
    }



    public void login(View view){
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        method.setText("Get Method");
        //new SigninActivity(this,status,role,0).execute(username,password);

        String url = "https://myprojectstore.000webhostapp.com/";
        Log.e("here", "here");
        VolleyService request = new VolleyService(this);
        request.executeRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    //JSONObject jsonObj = new JSONObject(response);
                    //JSONArray products = jsonObj.getJSONArray("products");
                    Gson gson = new Gson();
                    Products statusJSON = gson.fromJson(response , Products.class);
                    //Product[] vmNamesArray = gson.fromJson(response, Product[].class);
                    Log.e("response", "h" + statusJSON.getProducts().get(0).getReview_message());


                } catch (Exception ex) {
                    Log.e("Request: ", ex.getMessage());
                }
            }
        });
    }

    public void loginPost(View view){
//        String username = usernameField.getText().toString();
//        String password = passwordField.getText().toString();
//        method.setText("Post Method");
//        new SigninActivity(this,status,role,1).execute(username,password);
        startActivity(new Intent(MainActivity.this, ProductCustomerActivity.class));
    }
}
