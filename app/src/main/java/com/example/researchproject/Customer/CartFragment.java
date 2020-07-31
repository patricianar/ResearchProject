package com.example.researchproject.Customer;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.researchproject.Admin.AddProductFragment;
import com.example.researchproject.Classes.Catalogue;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";
    RecyclerView recyclerView;

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProducts();
        ImageView imgBack = view.findViewById(R.id.imgBack);
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(CartFragment.this).commit();
                //mListener.onClose();
            }
        });
    }

    private void getProducts() {
        String url = "https://myprojectstore.000webhostapp.com/product/";
        String productsIds = "";
        SharedPreferences sharedPref = getContext().getSharedPreferences("Cart", MODE_PRIVATE);

        //gets the contents of shared preferences
        Map<String, ?> keys = sharedPref.getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            productsIds += entry.getKey() + ",";
        }
        productsIds = productsIds.substring(0, productsIds.length() - 1);
        Log.e("test", productsIds);

        VolleyService request = new VolleyService(getContext());
        request.executePostRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    if (response.contains("products")) {
                        Gson gson = new Gson();
                        Catalogue catalogue = gson.fromJson(response, Catalogue.class);
                        ProductCartAdapter myAdapter = new ProductCartAdapter(catalogue.getProducts());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(myAdapter);
                        //myAdapter.setListener(ProductCustomerActivity.this);
                    } else {
                        Toast.makeText(getContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, response);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }, "productsCart", productsIds);
    }
}