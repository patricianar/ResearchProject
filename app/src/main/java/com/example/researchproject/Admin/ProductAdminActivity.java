package com.example.researchproject.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProductAdminActivity extends AppCompatActivity {

    private static final int ACTIVITY_CHOOSE_FILE1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_admin);

        Button btnImport = findViewById(R.id.btnImportProducts);
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> productsList = ReadCSV();

                //final Customer customer = new Customer(email, firstName, lastName, password1);
               // Gson gson = new Gson();
                //final String jsonInString = gson.toJson(customer);
                String url = "https://myprojectstore.000webhostapp.com/product/";

                Catalogue catalogue = new Catalogue();
                catalogue.setProducts(productsList);

                Gson gson = new Gson();
                String jsonCatalogue = gson.toJson(catalogue);

                Log.e("test csv", productsList.get(0).getName());
                VolleyService request = new VolleyService(ProductAdminActivity.this);
                request.executePostRequest(url, new VolleyService.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            Log.e("aaa", response);
                        } catch (Exception ex) {
                            Log.e("Request: ", ex.getMessage());
                        }
                    }
                }, "products", jsonCatalogue);



                //selectCSVFile();
            }
        });
    }

    private void selectCSVFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Open CSV"), ACTIVITY_CHOOSE_FILE1);
        Log.e("here", "a");

//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "text/*"
//        startActivityForResult(intent, READ_REQUEST_CODE)

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVITY_CHOOSE_FILE1: {
                if (resultCode == RESULT_OK) {
                    proImportCSV(new File(data.getData().getPath()));
                    Log.e("here", "b");
                }
            }
        }
    }

    private void proImportCSV(File from) {
        try {
            // reading CSV and writing table
            //CSVReader dataRead = new CSVReader(new FileReader(from)); // <--- This line is key, and why it was reading the wrong file
            Log.e("here", "c");
            //ReadCSV(from);
        } catch (Exception e) {
            Log.e("TAG", e.toString());

        }
    }

    private ArrayList<Product> ReadCSV() {
        ArrayList<Product> productsList = new ArrayList<>();

        InputStream inputStream = getResources().openRawResource(R.raw.mock_data);//opening input stream with raw csv resource


        try{
            //String extr = Environment.getExternalStorageDirectory().toString();
            //FileInputStream fileInputStream = new FileInputStream(extr + from);
            BufferedReader reader =  new BufferedReader(new InputStreamReader(inputStream));
            String csvLine;
            if((csvLine =  reader.readLine()) != null){
                //here csv Line contains the header line and can be used if needed
            }
            while ((csvLine = reader.readLine()) != null){

                String[] row = csvLine.split(",");

                //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
               // Date dateAuction = formatter.parse(row[0]);

                String barcode = row[0];
                String brand = row[1];
                String category = row[2];
                Double costPrice = Double.parseDouble(row[3]);
                String description = row[4];
                int id = Integer.parseInt(row[5]);
                int invLevel = Integer.parseInt(row[6]);
                int invWarnLevel = Integer.parseInt(row[7]);
                String name = row[8];
                Double price = Double.parseDouble(row[9]);
                String urlImg = row[10];

                Product product = new Product(barcode, brand, category, costPrice, description, id, invLevel, invWarnLevel, name, price, urlImg);




                productsList.add(product);
            }
        }catch (IOException ex){
            throw new RuntimeException("Error reading file..." + ex.getMessage());
        }catch (NumberFormatException ex){
            throw new RuntimeException("Error in id parsing..." + ex.getMessage());
        }//catch (ParseException ex){
            //if date formatter results in an exception
            //throw new RuntimeException("Error in parsing date..." + ex.getMessage());
        //}
        return productsList;
    }
}
