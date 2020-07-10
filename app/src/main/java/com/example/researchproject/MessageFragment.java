package com.example.researchproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {
    private static final String ARG_PARAM1 = "IMAGE";
    private static final String ARG_PARAM2 = "TEXT";

    private int image;
    private String text;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    public static MessageFragment newInstance(int param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getInt(ARG_PARAM1);
            text = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imgMsg = view.findViewById(R.id.imgMessage);
        TextView tvMsgTitle = view.findViewById(R.id.tvMessageTitle);
        TextView tvMsg = view.findViewById(R.id.tvMessage);

        imgMsg.setImageResource(image);

        if(text.equals("nothing")){
            tvMsgTitle.setText("Nothing to see here - yet!");
            tvMsg.setText("More products are on their way, so please check back later.");
        }else{

            tvMsgTitle.setText("Your " + text + " is empty!");
            tvMsg.setText("Start adding items to your " + text);
        }
    }
}
