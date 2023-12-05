package com.socatra.excutivechain.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * Created by srikanth 25/09/2023
 */

public class MyFirebaseInstanceServiceId extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.

       /* Intent intent = new Intent(this,MyFirebaseMessagingService.class);
        startActivity(intent);*/
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);
    }
}
