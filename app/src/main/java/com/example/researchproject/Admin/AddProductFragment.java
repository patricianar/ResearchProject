package com.example.researchproject.Admin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "ProductAddFragment";
    OnCardViewClickedListener mListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.onOpen();

        final TextView tvProdName = view.findViewById(R.id.tvProdNameEdit);
        final EditText etProdBand = view.findViewById(R.id.etProdBand);
        final EditText etCategory = view.findViewById(R.id.etCategory);
        final EditText etProdDesc = view.findViewById(R.id.etProdDescEdit);
        final EditText etCostPrice = view.findViewById(R.id.etCostPrice);
        final EditText etPrice = view.findViewById(R.id.etPrice);
        final EditText etInvLevel = view.findViewById(R.id.etInvLevel);
        final EditText etInvWarnLevel = view.findViewById(R.id.etInvWarnLevel);
        ImageView imgProd = view.findViewById(R.id.imgProdEdit);
//        ImageView imgClose = view.findViewById(R.id.imgClose);
        Button btnEditProd = view.findViewById(R.id.btnEditProduct);

        ImageView imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(AddProductFragment.this).commit();
                mListener.onClose();
            }
        });

        btnEditProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://myprojectstore.000webhostapp.com/product/";

                Product newProduct = new Product("adfdf","23",etCategory.getText().toString(),Double.parseDouble(etCostPrice.getText().toString()),
                        etProdDesc.getText().toString(),300,Integer.parseInt(etInvLevel.getText().toString()),Integer.parseInt(etInvWarnLevel.getText().toString()),
                        tvProdName.getText().toString(),Double.parseDouble(etPrice.getText().toString()),"adf");

                Gson gson = new Gson();
                final String jsonInString = gson.toJson(newProduct);
                VolleyService request = new VolleyService(getContext());
                request.executePostRequest(url, new VolleyService.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            if(response.equals("true")){
                                Toast.makeText(getContext(), "New Product has been added!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                            }
                            Log.d(TAG, response);
                        } catch (Exception ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                    }
                }, "addProduct", jsonInString);
            }
        });




    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof UpdateProductFragment.OnCardViewClickedListener){
            mListener = (OnCardViewClickedListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCardViewClickedListener{
        void onOpen(); // hide bottom bar when cardView is clicked
        void onClose(); // show bottom bar when cardView is clicked
    }
}
