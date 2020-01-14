package com.app.jobfizzer.Utilities.FCM;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.app.jobfizzer.Utilities.Events.InternetEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * no internet toast
 */

public class ConnectivityReceiver extends BroadcastReceiver {
    public ConnectivityListener connectivityListener;
    private static String TAG = ConnectivityReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        onReceive(context);
    }

    public void onReceive(Context context) {
        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (cn != null) {
            networkInfo = cn.getActiveNetworkInfo();
        }

        if (networkInfo != null) {
            //Log.e(TAG, "onReceive: " + networkInfo);
            if (networkInfo.isConnected()) {
                Log.e(TAG, "Connected");
                EventBus.getDefault().post(new InternetEvent(true));

            } else {
                EventBus.getDefault().post(new InternetEvent(false));
            }
        } else {
            //Log.e(TAG, "ConnectivityReceiver: ");
            EventBus.getDefault().post(new InternetEvent(false));

        }
    }

    public interface ConnectivityListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}