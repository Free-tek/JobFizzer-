package com.app.jobfizzer.View.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends BaseActivity {
    AppSettings appSettings = new AppSettings(SplashActivity.this);
    CommonViewModel commonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        commonViewModel.generateKeyHash();
//        if (ConnectionUtils.isNetworkConnected(SplashActivity.this)) {
//            processHandler();
//        } else {
//            Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
//            processHandler();
//        }
        processHandler();
    }

    private void processHandler() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (appSettings.getIsLogged().equalsIgnoreCase("true")) {
                    try {
                        deviceTokenIputs();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    moveOnBoardActivity();
                }
            }
        }, 3000);

    }


    private void upDateToken(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                if (flagResponseModel != null) {
                    if (flagResponseModel.getError().equalsIgnoreCase("true")) {
                        Utils.toast(findViewById(android.R.id.content), flagResponseModel.getError_message());
                        moveToSignIn();
                    } else {
                        moveMainActivity();
                    }
                }
            }
        });
    }

    private void moveToSignIn() {
        Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
        finish();
    }

    public void deviceTokenIputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fcm_token", appSettings.getFireBaseToken());
        jsonObject.put("os", "android");
        InputForAPI inputForAPI = new InputForAPI(SplashActivity.this);
        inputForAPI.setUrl(UrlHelper.UPDATE_DEVICE_TOKEN);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        upDateToken(inputForAPI);

    }

    private void moveOnBoardActivity() {
        Intent intent = new Intent(SplashActivity.this, OnboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private void moveMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.putExtra(Constants.LOGIN_TYPE, Constants.HOME_TAB);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

    }


}