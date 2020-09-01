package com.app.jobfizzer.Utilities.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.View.activities.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.app.jobfizzer.R;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/*
 * Created by user on 25-10-2017.
 */

public class FcmMessageService extends FirebaseMessagingService {

    private static final String TAG = FcmMessageService.class.getSimpleName();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived: " + remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            JSONObject jsonObject = null;
            jsonObject = new JSONObject(remoteMessage.getData());
            Log.e(TAG, "getData: " + jsonObject);
            sendNotification(jsonObject);
        }
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "getNotification: " + remoteMessage.getNotification());
        }

        EventBus.getDefault().post(new com.app.jobfizzer.Utilities.Events.Status());

    }

    private void sendNotification(JSONObject jsonObject) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.LOGIN_TYPE, Constants.HOME_TAB);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(jsonObject.optString("title"))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

        if (jsonObject.has("body")) {
            notificationBuilder.setContentText(jsonObject.optString("body"));
        } else {
            notificationBuilder.setContentText(jsonObject.optString("message"));
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                String NOTIFICATION_CHANNEL_ID = "22";
                String NOTIFICATION_NAME = "uberdoox";
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        NOTIFICATION_NAME, importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.WHITE);
                notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }
}