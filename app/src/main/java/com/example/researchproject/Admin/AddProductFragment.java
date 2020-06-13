package com.example.researchproject.Admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.researchproject.Classes.Product;
import com.example.researchproject.Customer.LoginActivity;
import com.example.researchproject.Customer.RegistrationActivity;
import com.example.researchproject.R;
import com.example.researchproject.VolleyService;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProductFragment extends Fragment {
    private static final String TAG = "ProductAddFragment";
    private static final int PICK_IMAGE_REQUEST = 11;
    private OnCardViewClickedListener mListener;
    private ImageView imgProd;
    String encodedImage;

    public AddProductFragment() {
        // Required empty public constructor
    }

    public static AddProductFragment newInstance() {
        AddProductFragment fragment = new AddProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        imgProd = view.findViewById(R.id.imgProdEdit);
        ImageView imgClose = view.findViewById(R.id.imgClose);
        Button btnEditProd = view.findViewById(R.id.btnEditProduct);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(AddProductFragment.this).commit();
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

                Product newProduct = new Product("adfdf",etProdBand.getText().toString(),etCategory.getText().toString(),Double.parseDouble(etCostPrice.getText().toString()),
                        etProdDesc.getText().toString(),300,Integer.parseInt(etInvLevel.getText().toString()),Integer.parseInt(etInvWarnLevel.getText().toString()),
                        tvProdName.getText().toString(),Double.parseDouble(etPrice.getText().toString()),encodedImage);

                Gson gson = new Gson();
                final String jsonInString = gson.toJson(newProduct);
                VolleyService request = new VolleyService(getContext());
                request.executePostRequest(url, new VolleyService.VolleyCallback() {
                    @Override
                    public void getResponse(String response) {
                        try {
                            if(response.equals("true")){
                                Toast.makeText(getContext(), "New Product has been added!", Toast.LENGTH_SHORT).show();
                                Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(3500); // wait before going to login
                                            getActivity().getSupportFragmentManager().beginTransaction().remove(AddProductFragment.this).commit();
                                            mListener.onClose();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                thread.start();
                            }
                            else {
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

    public interface OnCardViewClickedListener{
        void onOpen(); // hide bottom bar when cardView is clicked
        void onClose(); // show bottom bar when cardView is clicked
    }
}
