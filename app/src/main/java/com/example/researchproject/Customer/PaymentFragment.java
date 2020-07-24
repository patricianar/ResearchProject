package com.example.researchproject.Customer;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.researchproject.Classes.Address;
import com.example.researchproject.Classes.Customer;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class PaymentFragment extends Fragment {
    private static final String CUSTOMER = "Customer";
    private static final String ACTIVITY = "Activity";
    private static final String TAG = "PaymentFragment";
    private PaymentFragment.OnPaymentClickedListener mListener;

    private Customer mCustomer;
    private String mActivity;
    EditText etCardNumber, etCVV;
    Spinner spinnerMonth, spinnerYear;

    public PaymentFragment() {
        // Required empty public constructor
    }

    public static PaymentFragment newInstance(Customer customer, String activity) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putSerializable(CUSTOMER, customer);
        args.putString(ACTIVITY, activity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCustomer = (Customer) getArguments().getSerializable(CUSTOMER);
            mActivity = getArguments().getString(ACTIVITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etCardNumber = view.findViewById(R.id.etCardNumber);
        spinnerMonth = view.findViewById(R.id.spinnerMonth);
        etCVV = view.findViewById(R.id.etCVV);
        spinnerYear = view.findViewById(R.id.spinnerYear);
        Button btnContinue = view.findViewById(R.id.btnContinue);

        if (mActivity.equals("Account")) {
            btnContinue.setText("SAVE");
        }

        List<String> list = new ArrayList<>();
        for (int i = 2020; i <= 2035; i++) {
            list.add(String.valueOf(i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(dataAdapter);

        try {
            etCardNumber.setText(mCustomer.getCard().getCardNumber());
            spinnerMonth.setSelection(mCustomer.getCard().getExpiryMonth());
            int yearPosition = dataAdapter.getPosition(String.valueOf(mCustomer.getCard().getExpiryYear()));
            spinnerYear.setSelection(yearPosition);
            etCVV.setText(String.valueOf(mCustomer.getCard().getCVV()));

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }

        btnContinue.setOnClickListener(v -> {
                    updateCustomerDetails();
                    mListener.onPaymentClicked(mCustomer);
                }
        );
    }

    private void updateCustomerDetails() {
        mCustomer.getCard().setCardNumber(etCardNumber.getText().toString());
        mCustomer.getCard().setExpiryMonth(spinnerMonth.getSelectedItemPosition());
        mCustomer.getCard().setExpiryYear((Integer.parseInt(spinnerYear.getSelectedItem().toString())));
        mCustomer.getCard().setCVV(Integer.parseInt(etCVV.getText().toString()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PaymentFragment.OnPaymentClickedListener) {
            mListener = (PaymentFragment.OnPaymentClickedListener) context;
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

    public interface OnPaymentClickedListener {
        void onPaymentClicked(Customer customer);
    }
}