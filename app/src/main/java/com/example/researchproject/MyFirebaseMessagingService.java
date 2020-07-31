package com.example.researchproject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.researchproject.Classes.Order;
import com.example.researchproject.Customer.OrdersCustomerActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseService";

    public MyFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            } else {
                // Handle message within 10 seconds
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
            String email = sharedPrefUser.getString("Email", "");

            String url = "http://100.25.155.48/notification/";
            VolleyService request = new VolleyService(this);
            request.executePostRequest(url, response -> {
                try {
                    Log.d(TAG, response);
                } catch (Exception ex) {
                    Log.e(TAG, ex.getMessage());
                }
            }, "newNotification", email + ","
                    + remoteMessage.getNotification().getTitle()
                    + "," + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        SharedPreferences sharedPrefUser = getSharedPreferences("User", MODE_PRIVATE);
        String email = sharedPrefUser.getString("Email", "");

        String url = "http://100.25.155.48/customer/";
        VolleyService request = new VolleyService(this);
        request.executePostRequest(url, response -> {
            try {
                Log.d(TAG, response);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
        }, "updateToken", email + "," + token);
    }
}
