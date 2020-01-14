package com.app.jobfizzer.View.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.jobfizzer.Model.ForgotPasswordResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {
    Button sendOtp;
    EditText emailEditText;
    ImageView backButton;
    CommonViewModel commonViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        initViews();
        initListners();
    }

    private void initListners() {
        backButton.setOnClickListener(this);
        sendOtp.setOnClickListener(this);

    }

    private void initViews() {
        backButton = findViewById(R.id.backButton);
        sendOtp = findViewById(R.id.sendOtp);
        emailEditText = findViewById(R.id.emailEditText);
    }


    @Override
    public void onClick(View view) {

        if (view == backButton) {
            moveToSignInScreen();
        } else if (view == sendOtp) {
            Utils.hideKeyboard(view);
            if (isValidInputs().equalsIgnoreCase("true")) {
                try {
                    if (ConnectionUtils.isNetworkConnected(ForgotPasswordActivity.this)) {
                        buildOtpinputs();
                    } else {
                        Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.toast(findViewById(android.R.id.content), isValidInputs());
            }
        }
    }

    private void moveToSignInScreen() {
        Intent intent = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private void buildOtpinputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("email", emailEditText.getText().toString().trim());
        InputForAPI inputForAPI = new InputForAPI(ForgotPasswordActivity.this);
        inputForAPI.setUrl(UrlHelper.FORGOT_PASSWORD);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        requestOtp(inputForAPI);
    }

    private String isValidInputs() {

        String val;
        if (emailEditText.getText().toString().trim().length() == 0 || !Utils.isValidEmail(emailEditText.getText().toString().trim())) {
            val = getResources().getString(R.string.please_enter_valid_email);
        } else {
            val = "true";
        }
        return val;
    }

    private void requestOtp(InputForAPI inputForAPI) {

        commonViewModel.requestOtp(inputForAPI).observe(this, new Observer<ForgotPasswordResponseModel>() {
            @Override
            public void onChanged(@Nullable ForgotPasswordResponseModel forgotPasswordResponseModel) {
                if (forgotPasswordResponseModel != null) {
                    if (forgotPasswordResponseModel.getError().equalsIgnoreCase("false")) {
                        handleForgotPasswordResponse(forgotPasswordResponseModel);
                    } else {
                        Utils.toast(findViewById(android.R.id.content), forgotPasswordResponseModel.getErrorMessage());
                    }
                }
            }
        });
    }

    private void handleForgotPasswordResponse(ForgotPasswordResponseModel response) {
        String otp = response.getOtp().toString();
        moveVerfication(otp);
    }

    private void moveVerfication(String otp) {
        Intent intent = new Intent(ForgotPasswordActivity.this, EnterVerificationCodeActivity.class);
        intent.putExtra("otp", otp);
        intent.putExtra("emailValue", emailEditText.getText().toString().trim());
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

}
