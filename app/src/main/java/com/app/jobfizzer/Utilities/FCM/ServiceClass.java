package com.app.jobfizzer.Utilities.FCM;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.helpers.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.app.jobfizzer.Utilities.Constants.UrlHelper.GET_ONLINE;
import static com.app.jobfizzer.Utilities.Constants.UrlHelper.IS_OFFLINE;
import static com.app.jobfizzer.Utilities.Constants.UrlHelper.IS_ONLINE;
import static com.app.jobfizzer.Utilities.Constants.UrlHelper.RECEVICE_MESSAGE;
import static com.app.jobfizzer.Utilities.Constants.UrlHelper.SEND_DELIVERED;
import static com.app.jobfizzer.Utilities.Constants.UrlHelper.SEND_MESSAGE;


/*
 * Created by yuvaraj on 02/12/17.
 */

public class ServiceClass extends Service {
    public static Socket socket;
    private static String TAG = ServiceClass.class.getSimpleName();
    AppSettings appSettings = new AppSettings(ServiceClass.this);
    Boolean UpdatedOff = false;
    Handler handler = new Handler();
    private String my_id;
    private Handler handleroffline = new Handler();
    private boolean Updatedon = false;
    private Runnable statusCheck = new Runnable() {
        @Override
        public void run() {
            Boolean isBacgkround = isAppIsInBackground(ServiceClass.this);
            if (isBacgkround) {
                if (!UpdatedOff) {
                    Emitters emitters = new Emitters(ServiceClass.this);
                    // emitters.emitoffline();
                    UpdatedOff = true;
                    Updatedon = false;
                }
            } else {
                if (!Updatedon) {
                    Emitters emitters = new Emitters(ServiceClass.this);
                    // emitters.emitnewonline();
                    UpdatedOff = false;
                    Updatedon = true;
                }
            }
            handleroffline.postDelayed(this, 1000);
        }

    };
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            handler.post(statusCheck);

            Emitters emitters = new Emitters(ServiceClass.this);
            emitters.emitonline();

        }

    };

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equals(context.getPackageName())) {
                    isInBackground = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isInBackground;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        my_id = appSettings.getUserId();

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;

        Utils.log("service: ", "MainActivity");
        try {
            socket = IO.socket(UrlHelper.CHAT_SOCKET, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("SOCKET.IO ", e.getMessage());
        }
        socket.connect();
        socket.on(Socket.EVENT_CONNECT, onConnect);

        Utils.log("service: ", "connected");
//
        socket.on(RECEVICE_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) args[0];
                Utils.log(TAG, "recivedMessage: " + jsonObject);

                EventBus.getDefault().post(new com.app.jobfizzer.Utilities.Events.MessageEvent(jsonObject));
            }
        });


        return START_STICKY;

    }


    public static class Emitters {
        private Context context;
        private AppSettings appSettings;

        public Emitters(Context context) {
            this.context = context;
            appSettings = new AppSettings(context);
        }


        public void emitonline() {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userid", appSettings.getUserId());
                socket.emit(GET_ONLINE, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        public void emitoffline() {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userid", appSettings.getUserId());
                socket.emit(IS_OFFLINE, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        public void emitnewonline() {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userid", appSettings.getUserId());
                socket.emit(IS_ONLINE, jsonObject);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        public void sendMessage(JSONObject jsonObject) {
            Utils.log(TAG, "sendMessage: " + jsonObject);
            socket.emit(SEND_MESSAGE, jsonObject);

        }

        public void sendDelievered(JSONObject jsonObject) {

            socket.emit(SEND_DELIVERED, jsonObject);
        }
    }
}