package com.example.researchproject.Customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.researchproject.Classes.Address;
import com.example.researchproject.Classes.Card;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class AccountFragment extends Fragment {
    private static final String TAG = "AccountFragment";
    private static final String ADDRESS = "Shipping Address";
    private Address mAddress;
    private OnAccountClickedListener mListener;
    VolleyService request;
    Customer customer;
    String email, url;
    EditText etFirstName, etLastName, etAddress, etCity, etProvince, etPostalCode, etCountry;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance(Address address) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAddress = (Address) getArguments().getSerializable(ADDRESS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        request = new VolleyService(getContext());
        url = "http://100.25.155.48/customer/";
        SharedPreferences sharedPrefUser = getContext().getSharedPreferences("User", MODE_PRIVATE);
        email = sharedPrefUser.getString("Email", "");
        getCustomerDetails();

        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etAddress = view.findViewById(R.id.etAddress);
        etCity = view.findViewById(R.id.spinnerMonth);
        etProvince = view.findViewById(R.id.etProvince);
        etPostalCode = view.findViewById(R.id.etPostalCode);
        etCountry = view.findViewById(R.id.etCountry);
        Button btnContinue = view.findViewById(R.id.btnContinue);

        if (mAddress != null) {
            etAddress.setText(mAddress.getStreet());
            etCity.setText(mAddress.getCity());
            etPostalCode.setText(mAddress.getPostal_code());
            etProvince.setText(mAddress.getProvince());
            etCountry.setText(mAddress.getCountry());
        }

        etAddress.setOnClickListener(v -> mListener.onAddressClicked());
        btnContinue.setOnClickListener(v -> {
            if (etFirstName.getText().toString().isEmpty() || etLastName.getText().toString().isEmpty()
                    || etAddress.getText().toString().isEmpty() || etCity.getText().toString().isEmpty()
                    || etProvince.getText().toString().isEmpty() || etPostalCode.getText().toString().isEmpty()
                    || etCountry.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_LONG).show();
            } else {
                updateCustomerDetails();
                mListener.onAccountClicked(customer);
            }
        });
    }

    private void getCustomerDetails() {
        request.executePostRequest(url, response -> {
            try {
                Gson gson = new Gson();
                customer = gson.fromJson(response, Customer.class);
                etFirstName.setText(customer.getFirst_name());
                etLastName.setText(customer.getLast_name());
                if (customer.getAddress() != null && mAddress == null) {
                    etAddress.setText(customer.getAddress().getStreet());
                    etCity.setText(customer.getAddress().getCity());
                    etProvince.setText(customer.getAddress().getProvince());
                    etPostalCode.setText(customer.getAddress().getPostal_code());
                    etCountry.setText(customer.getAddress().getCountry());
                }
                Log.d(TAG, response);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }, "customerDetails", email);
    }

    private void updateCustomerDetails() {
        customer.setFirst_name(etFirstName.getText().toString());
        customer.setLast_name(etLastName.getText().toString());
        Address shippingAddress = new Address();
        shippingAddress.setStreet(etAddress.getText().toString());
        shippingAddress.setCity(etCity.getText().toString());
        shippingAddress.setProvince(etProvince.getText().toString());
        shippingAddress.setPostal_code(etPostalCode.getText().toString());
        shippingAddress.setCountry(etCountry.getText().toString());
        customer.setAddresses(shippingAddress);
        if (customer.getCard() == null) {
            Card card = new Card();
            customer.setCard(card);
        }
        Gson gson = new Gson();
        final String jsonInString = gson.toJson(customer);
        request.executePostRequest(url, response -> {
            try {
                Log.d(TAG, response);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }, "addCustomerDetails", jsonInString);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAccountClickedListener) {
            mListener = (OnAccountClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAccountClickedListener {
        void onAddressClicked(); // show fragment with map

        void onAccountClicked(Customer customer); // show payment details
    }
}