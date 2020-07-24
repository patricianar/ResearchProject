package com.example.researchproject.Customer;

import android.content.Context;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.researchproject.Classes.Address;
import com.example.researchproject.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapsFragment";
    GoogleMap map;
    Double latitude, longitude;
    private static final float DEFAULT_ZOOM = 15f;
    private Address shippingAddress;
    private OnMapsListener mListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
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
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnContinue = view.findViewById(R.id.btnContinue);
        shippingAddress = new Address();
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
        }

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getContext());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setHint("Enter your address");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d(TAG, "Place: " + place.getName() + ", " + place.getId());
                geoLocate(place.getName());
                initMap();
            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "An error occurred: " + status);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onVerifyAddress(shippingAddress);
            }
        });
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void geoLocate(String search) {
        Log.d(TAG, "geolocate: geolocating");

        Geocoder geocoder = new Geocoder(getContext());
        List<android.location.Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(search, 1);
        } catch (IOException e) {
            Log.e(TAG, "-------------IOException: --------------" + e.getMessage());
        }

        if (list.size() > 0) {
            android.location.Address address = list.get(0);
            Log.d(TAG, "!!!!!!!!!!!! Find location: !!!!!!!!!!" + address.toString());
            latitude = address.getLatitude();
            longitude = address.getLongitude();
            shippingAddress.setStreet(search);
            shippingAddress.setCity(address.getLocality());
            shippingAddress.setProvince(address.getAdminArea());
            shippingAddress.setPostal_code(address.getPostalCode());
            shippingAddress.setCountry(address.getCountryName());

            initMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Add a marker in Location and move the camera
        LatLng place = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(place).title("Shipping Address").snippet(shippingAddress.getCity() + " " + shippingAddress.getProvince())).showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(place, DEFAULT_ZOOM));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnMapsListener){
            mListener = (OnMapsListener) context;
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

    public interface OnMapsListener {
        void onVerifyAddress(Address shippingAddress); // show fragment with address
    }
}