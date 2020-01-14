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
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.jobfizzer.Utilities.helpers.Utils.whiteSpacefilter;


public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
    EditText confirmPassword, newPassword, oldPassword;
    ImageView backButton;
    String email;
    Button saveButton;
    CommonViewModel commonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getIntentValues();
        initViews();
        initListners();

    }


    private void getIntentValues() {
        Intent intent = getIntent();
        email = intent.getStringExtra("emailValue");
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
    }

    private void initListners() {
        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    private void initViews() {
        confirmPassword = findViewById(R.id.confirmPassword);
        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        backButton = findViewById(R.id.backButton);
        saveButton = findViewById(R.id.saveButton);

        confirmPassword.setTransformationMethod(new Utils.AsteriskPasswordTransformationMethod());
        oldPassword.setTransformationMethod(new Utils.AsteriskPasswordTransformationMethod());
        newPassword.setTransformationMethod(new Utils.AsteriskPasswordTransformationMethod());

        confirmPassword.setFilters(new InputFilter[]{whiteSpacefilter});
        oldPassword.setFilters(new InputFilter[]{whiteSpacefilter});
        newPassword.setFilters(new InputFilter[]{whiteSpacefilter});


    }

    @Override
    public void onClick(View view) {
        if (view == backButton) {
            onBackPressed();
        } else {
            Utils.hideKeyboard(view);
            if (validateInputs().equalsIgnoreCase("true")) {
                buildInputs();
            } else {
                Utils.toast(findViewById(android.R.id.content), validateInputs());
            }

        }

        if (view == saveButton) {
            if (ConnectionUtils.isNetworkConnected(ChangePasswordActivity.this)) {

            } else {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
            }
        }
    }


    private String validateInputs() {
        String message = "true";

        if (oldPassword.getText().length() == 0) {
            return getResources().getString(R.string.enter_password);

        } else if (newPassword.getText().length() == 0) {
            return getResources().getString(R.string.enter_confirmpassword);

        } else if (confirmPassword.getText().length() == 0) {
            return getResources().getString(R.string.enter_confirmpassword);

        } else if (oldPassword.getText().toString().trim().length() < 6) {
            return getResources().getString(R.string.password_must_be_six);

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


    public void buildInputs() {
        JSONObject jsonObject = getInputs();

        InputForAPI inputForAPI = new InputForAPI(ChangePasswordActivity.this);
        inputForAPI.setUrl(UrlHelper.CHANGE_PASSWORD);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        resetPassword(inputForAPI);
    }

    private void resetPassword(InputForAPI inputForAPI) {

        commonViewModel.flagUpdate(inputForAPI).observe(this,
                new Observer<FlagResponseModel>() {
                    @Override
                    public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                        if (flagResponseModel != null) {
                            if (!flagResponseModel.getError().equalsIgnoreCase("true")) {
                                moveMainActivity();
                            } else {
                                Utils.toast(findViewById(android.R.id.content), flagResponseModel.getError_message());
                            }
                        }
                    }
                });

    }


    private void moveMainActivity() {
        Utils.toast(findViewById(android.R.id.content), getResources().getString(R.string.password_changed_success));
        Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
        intent.putExtra(Constants.LOGIN_TYPE, Constants.HOME_TAB);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private JSONObject getInputs() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("oldpassword", oldPassword.getText().toString());
            jsonObject.put("newpassword", newPassword.getText().toString());
            jsonObject.put("cnfpassword", confirmPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
