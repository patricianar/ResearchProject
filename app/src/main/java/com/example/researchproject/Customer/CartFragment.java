package com.example.researchproject.Customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.researchproject.Admin.AddProductFragment;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";

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
    }

    private void getProducts(){
        String url = "https://myprojectstore.000webhostapp.com/product/";

        final String jsonInString = "40, 50";
        VolleyService request = new VolleyService(getContext());
        request.executePostRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    if(response.equals("true")){
                        Toast.makeText(getContext(), "New Product has been added!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                    }
                    Log.d(TAG, response);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }
        }, "productsCart", jsonInString);
    }
}
