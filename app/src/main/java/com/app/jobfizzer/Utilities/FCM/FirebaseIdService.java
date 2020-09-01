package com.app.jobfizzer.Utilities.FCM;

import android.util.Log;

import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.google.firebase.iid.FirebaseInstanceId;


/**
 * Created by user on 25-10-2017.
 */

public class FirebaseIdService {


    public void onTokenRefresh() {
        storeToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void storeToken(String token) {
        //SharedPrefManager.getInstance(getApplicationContext()).storeToken(token);
    }



    /*private static final String TAG = FirebaseIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        AppSettings appSettings = new AppSettings(FirebaseIdService.this);
        appSettings.setFireBaseToken(token);

    }*/


}
