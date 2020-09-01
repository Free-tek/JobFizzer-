package com.app.jobfizzer.View.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EnterVerificationCodeActivity extends BaseActivity implements View.OnClickListener {
    ImageView close;
    Button verifyButton;
    String email, otp;
    EditText otpEditText;
    CommonViewModel commonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_verification_code);
        getIntentValues();
        initViews();
        initListners();
    }

    private void getIntentValues() {
        Intent intent = getIntent();
        email = intent.getStringExtra("emailValue");
        otp = intent.getStringExtra("otp");
    }

    private void initListners() {
        close.setOnClickListener(this);
        verifyButton.setOnClickListener(this);
    }

    private void initViews() {
        close = findViewById(R.id.close);
        verifyButton = findViewById(R.id.verifyButton);
        otpEditText = findViewById(R.id.otpEditText);
        otpEditText.setFilters(new InputFilter[]{Utils.whiteSpacefilter});
//        otpEditText.setText(otp);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);

    }

    private String checkOtp() {
        String val;
        if (otpEditText.getText().toString().trim().length() == 0) {
            val = getResources().getString(R.string.please_enter_valid_otp);
        } else if (otpEditText.getText().toString().trim().length() < 6) {
            val = getResources().getString(R.string.please_enter_valid_otp);
        } else {
            val = "true";
        }
        return val;
    }


    @Override
    public void onClick(View view) {
        if (view == close) {
            onBackPressed();
        } else if (view == verifyButton) {
            if (ConnectionUtils.isNetworkConnected(EnterVerificationCodeActivity.this)) {
                if (checkOtp().equalsIgnoreCase("true")) {
                    requestOtp();
                } else {
                    Utils.toast(findViewById(android.R.id.content), checkOtp());
                }
            } else {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
            }
        }
    }

    private void requestOtp() {
        JSONObject jsonObject = getInputs();

        InputForAPI inputForAPI = new InputForAPI(EnterVerificationCodeActivity.this);
        inputForAPI.setUrl(UrlHelper.VERIFY_OTP);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        CheckOtp(inputForAPI);

    }

    private void CheckOtp(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this,
                new Observer<FlagResponseModel>() {
                    @Override
                    public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                        if (flagResponseModel.getError().equalsIgnoreCase("false")) {
                            moveChangePassword();
                        } else {
                            Utils.toast(findViewById(android.R.id.content), flagResponseModel.getError_message());
                        }
                    }
                });
    }

    private JSONObject getInputs() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("otp", otpEditText.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void moveChangePassword() {
        Intent intent = new Intent(EnterVerificationCodeActivity.this, ResetPasswordActivity.class);
        intent.putExtra("emailValue", email);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }
}
