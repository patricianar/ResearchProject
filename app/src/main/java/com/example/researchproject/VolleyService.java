package com.example.researchproject;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class VolleyService {

    private RequestQueue requestQueue;

    public VolleyService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void executeGetRequest(String url, final VolleyCallback callback) {
        try {
            StringRequest stringGetRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (callback != null)
                        callback.getResponse(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Volley: ", error.getMessage() + "hi");
                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //params.put("Content-Type", "application/json");
                    //String credentials = "";
                    //String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    //params.put("data", "data");
                    return params;
                }
            };
            requestQueue.add(stringGetRequest);

        } catch (Exception e) {
            Log.e("Volley: ", e.getMessage());
        }
    }

    public void executePostRequest(String url, final VolleyCallback callback, final String name, final String jsonInString){
        try {
        StringRequest putRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        if (callback != null)
                            callback.getResponse(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.e("Error.Response", Objects.requireNonNull(error.getMessage()));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(name, jsonInString);
                return params;
            }
        };
        requestQueue.add(putRequest);
        } catch (Exception e) {
            Log.e("Volley: ", e.getMessage());
        }
    }

    public interface VolleyCallback {
        void getResponse(String response);
    }
}
