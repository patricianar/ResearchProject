package com.example.researchproject;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.researchproject.Admin.AddProductFragment;
import com.example.researchproject.Admin.UpdateProductFragment;

import java.util.Objects;

public class SearchFragment extends Fragment {
    private OnSearchListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText etWordToSearch = view.findViewById(R.id.etWordToSearch);
        ImageView imgClose = view.findViewById(R.id.imgBack);
        ImageView imgCamera = view.findViewById(R.id.imgCamera);

        etWordToSearch.requestFocus();
        etWordToSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    mListener.onEnter(etWordToSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });

        imgCamera.setOnClickListener(v -> {
            mListener.onClick();
        });

        imgClose.setOnClickListener(view1 -> {
            requireActivity().getSupportFragmentManager().beginTransaction().remove(SearchFragment.this).commit();
            mListener.onClose();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SearchFragment.OnSearchListener){
            mListener = (OnSearchListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onClose();
        mListener = null;
    }

    public interface OnSearchListener{
        void onClose(); // show toolbar when cardView is clicked
        void onEnter(String word);
        void onClick();
    }
}