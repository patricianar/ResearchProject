package com.example.researchproject.Admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.researchproject.Classes.Product;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProductFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "product";
    private static final String TAG = "UpdateProductFragment";
    private static final int PICK_IMAGE_REQUEST = 11;
    private ImageView imgProd;
    private OnCardViewClickedListener mListener;
    private String encodedImage;
    private Product mParam1;

    public UpdateProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ProductDetaiilFragment.
     */

    public static UpdateProductFragment newInstance(Product param1) {
        UpdateProductFragment fragment = new UpdateProductFragment();
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
        return inflater.inflate(R.layout.fragment_product_update, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListener.onOpen();
        TextView tvProdName = view.findViewById(R.id.tvProdNameEdit);
        final EditText etProdDesc = view.findViewById(R.id.etProdDescEdit);
        final EditText etCostPrice = view.findViewById(R.id.etCostPrice);
        final EditText etPrice = view.findViewById(R.id.etPrice);
        final EditText etInvLevel = view.findViewById(R.id.etInvLevel);
        final EditText etInvWarnLevel = view.findViewById(R.id.etInvWarnLevel);
        imgProd = view.findViewById(R.id.imgProdEdit);
        ImageView imgClose = view.findViewById(R.id.imgClose);
        Button btnEditProd = view.findViewById(R.id.btnEditProduct);

        encodedImage = mParam1.getUrl_image();
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
                getActivity().getSupportFragmentManager().beginTransaction().remove(UpdateProductFragment.this).commit();
                mListener.onClose();
            }
        });

        imgProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btnEditProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = "https://myprojectstore.000webhostapp.com/product/";
                String url = "http://100.25.155.48/product/";

                mParam1.setDescription(etProdDesc.getText().toString());
                mParam1.setCost_price(Double.parseDouble(etCostPrice.getText().toString()));
                mParam1.setPrice(Double.parseDouble(etPrice.getText().toString()));
                mParam1.setInventory_level(Integer.parseInt(etInvLevel.getText().toString()));
                mParam1.setInventory_warning_level(Integer.parseInt(etInvWarnLevel.getText().toString()));
                mParam1.setUrl_image(encodedImage);

                Gson gson = new Gson();
                final String jsonInString = gson.toJson(mParam1);
                VolleyService request = new VolleyService(getContext());
                request.executePostRequest(url, new VolleyService.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            if (response.equals("true")) {
                                Toast.makeText(getContext(), "Product information has been successfully changed!", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), "Something went wrong, please try again!", Toast.LENGTH_SHORT).show();
                            }
                            Log.d(TAG, response);
                        } catch (Exception ex) {
                            Log.e(TAG, ex.getMessage());
                        }
                    }
                }, "editProduct", jsonInString);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnCardViewClickedListener) {
            mListener = (OnCardViewClickedListener) context;
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

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and resultCode is RESULT_OK
        // then set image in the image view
        if (resultCode == RESULT_OK) {
            Uri filePath = null;
            if (data != null) {
                if (requestCode == PICK_IMAGE_REQUEST) {
                    filePath = data.getData();
                    try {
                        // Setting image on image view using Bitmap
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                        imgProd.setImageBitmap(bitmap);

                        //Converting Bitmap to string
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();

                        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    } catch (IOException e) {
                        // Log the exception
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public interface OnCardViewClickedListener {
        void onOpen(); // hide bottom bar when cardView is clicked

        void onClose(); // show bottom bar when cardView is clicked
    }

}
