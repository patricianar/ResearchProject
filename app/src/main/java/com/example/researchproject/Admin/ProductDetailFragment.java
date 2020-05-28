package com.example.researchproject.Admin;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.researchproject.Classes.Customer;
import com.example.researchproject.Classes.Product;
import com.example.researchproject.Customer.LoginActivity;
import com.example.researchproject.Customer.ProductCustomerActivity;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "product";
    private static final String TAG = "ProductDetailFragment";

    private Product mParam1;

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ProductDetaiilFragment.
     */

    public static ProductDetailFragment newInstance(Product param1) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (Product) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_detaiil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvProdName = view.findViewById(R.id.tvProdNameEdit);
        EditText etProdDesc = view.findViewById(R.id.etProdDescEdit);
        EditText etCostPrice = view.findViewById(R.id.etCostPrice);
        EditText etPrice = view.findViewById(R.id.etPrice);
        EditText etInvLevel = view.findViewById(R.id.etInvLevel);
        EditText etInvWarnLevel = view.findViewById(R.id.etInvWarnLevel);
        ImageView imgProd = view.findViewById(R.id.imgProdEdit);
        ImageView imgClose = view.findViewById(R.id.imgClose);
        Button btnEditProd = view.findViewById(R.id.btnEditProduct);

        tvProdName.setText(mParam1.getName());
        etProdDesc.setText(mParam1.getDescription());
        etCostPrice.setText(String.valueOf(mParam1.getCost_price()));
        etPrice.setText(String.valueOf(mParam1.getPrice()));
        etInvLevel.setText(String.valueOf(mParam1.getInventory_level()));
        etInvWarnLevel.setText(String.valueOf(mParam1.getInventory_warning_level()));
        Picasso.get().load(mParam1.getUrl_image()).into(imgProd);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(ProductDetailFragment.this).commit();
            }
        });

        btnEditProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://myprojectstore.000webhostapp.com/product/";

                Gson gson = new Gson();
                final String jsonInString = gson.toJson(mParam1);
                VolleyService request = new VolleyService(getContext());
                request.executePostRequest(url, new VolleyService.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            if(response.equals("true")){
                                Toast.makeText(getContext(), "Product information has been successfully changed!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Something went wrong, please try again!" + response, Toast.LENGTH_SHORT).show();
                            }
                            Log.e(TAG, response);
                        } catch (Exception ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                    }
                }, "editProduct", jsonInString);
            }
        });
    }
}
