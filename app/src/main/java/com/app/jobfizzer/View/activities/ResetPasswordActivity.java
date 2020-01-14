package com.app.jobfizzer.View.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    EditText confirmPassword, newPassword;
    ImageView backButton;
    String email;
    CommonViewModel commonViewModel;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        getIntentValues();
        initViews();
        initListners();
    }

    private void getIntentValues() {
        Intent intent = getIntent();
        email = intent.getStringExtra("emailValue");
    }

    private void initListners() {
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void initViews() {
        confirmPassword = findViewById(R.id.confirmPassword);
        newPassword = findViewById(R.id.newPassword);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        confirmPassword.setTransformationMethod(new Utils.AsteriskPasswordTransformationMethod());
        newPassword.setTransformationMethod(new Utils.AsteriskPasswordTransformationMethod());

        confirmPassword.setFilters(new InputFilter[]{Utils.whiteSpacefilter});
        newPassword.setFilters(new InputFilter[]{Utils.whiteSpacefilter});

    }

    @Override
    public void onClick(View view) {
        if (view == backButton) {
//            onBackPressed();
            moveToForgotPassword();
        }
        if (view == saveButton) {
            if (ConnectionUtils.isNetworkConnected(ResetPasswordActivity.this)) {
                if (validateInputs().equalsIgnoreCase("true")) {
                    try {
                        if (ConnectionUtils.isNetworkConnected(ResetPasswordActivity.this)) {
                            getInputs();
                        } else {
                            Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.toast(findViewById(android.R.id.content), validateInputs());
                }
            } else {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
            }
        }
    }

    private void moveToForgotPassword() {
        Intent intent = new Intent(ResetPasswordActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

    }

    public void getInputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("password", newPassword.getText().toString());
        jsonObject.put("email", email);
        jsonObject.put("confirmpassword", confirmPassword.getText().toString());

        InputForAPI inputForAPI = new InputForAPI(ResetPasswordActivity.this);
        inputForAPI.setUrl(UrlHelper.RESET_PASSWORD);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(new HashMap<String, String>());

        resetPassword(inputForAPI);

    }

    private void resetPassword(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                moveSignActivity();
            }
        });
    }

    private String validateInputs() {
        String message = "true";

        if (newPassword.getText().length() == 0) {
            return getResources().getString(R.string.enter_password);

        } else if (confirmPassword.getText().length() == 0) {
            return getResources().getString(R.string.enter_confirmpassword);

        } else if (newPassword.getText().toString().trim().length() < 6) {
            return getResources().getString(R.string.password_must_be_six);

        } else if (confirmPassword.getText().toString().trim().length() < 6) {
            return getResources().getString(R.string.password_must_be_six);

        } else if (!newPassword.getText().toString().equalsIgnoreCase(confirmPassword.getText().toString())) {
            return getResources().getString(R.string.password_not_match);

        } else {
            return message;
        }
    }

    private void moveSignActivity() {

        Utils.toast(findViewById(android.R.id.content), getString(R.string.password_changed_success));
        Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveToForgotPassword();
    }
}