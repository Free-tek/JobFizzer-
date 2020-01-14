package com.app.jobfizzer.View.activities;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.jobfizzer.Model.SignInResponseModel;
import com.app.jobfizzer.Model.SocialSignInSuccessResponse;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.SignInViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by karthik on 01/10/17.
 */

public class SignInActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    public GoogleApiClient mGoogleApiClient;
    EditText usernameEditText, passwordEditText;
    Button dontHaveAnAccount, loginButton, forgotPassword;
    ImageView facebookLogin, googleLogin;
    LoginButton loginFbButton;
    FrameLayout facebookLoginButton;
    CallbackManager callbackManager;
    AppSettings appSettings = new AppSettings(SignInActivity.this);
    private String TAG = SignInActivity.class.getSimpleName();
    private int RC_SIGN_IN = 420;
    private Button ok;

    SignInViewModel signInViewModel;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        FacebookSdk.sdkInitialize(getApplicationContext());

        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);

        initViews();
        initListners();
        initFacebookLogin();
        initGoogleLogin();
    }

    private void initGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void googlesignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        loginFbButton = (LoginButton) findViewById(R.id.loginFbButton);

        loginFbButton.setReadPermissions("email");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                get_data_for_facebook(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    private void get_data_for_facebook(LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        handleFacebookSuccessResponse(object, response);

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, locale ");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void handleFacebookSuccessResponse(JSONObject object, GraphResponse response) {

        String email_json = object.optString("email");
        String first_name = object.optString("first_name");
        String last_name = object.optString("last_name");
        String id = object.optString("id");
        String type = "facebook";
        String imageUrl = "https://graph.facebook.com/" + id + "/picture?type=large";
        if (email_json.equals("")) {
            showNoEmailAttachedDialog();
        } else {
            buildSocialLoginInputs(email_json, first_name, last_name, id, type, imageUrl);
        }
    }

    private void showNoEmailAttachedDialog() {
        LoginManager.getInstance().logOut();
        AlertDialog.Builder alert = new AlertDialog.Builder(SignInActivity.this);
        alert.setMessage(getResources().getString(R.string.no_email_error_fb));
        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }

    private void buildSocialLoginInputs(String email_json, String first_name, String last_name,
                                        String id, String type, final String imageUrl) {

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("email", email_json);
            jsonObject.put("firstname", first_name);
            jsonObject.put("lastname", last_name);
            jsonObject.put("socialtoken", id);
            jsonObject.put("social_type", type);
            if (imageUrl.equalsIgnoreCase("null")) {
                jsonObject.put("file", " ");
            } else {
                jsonObject.put("file", imageUrl);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(SignInActivity.this);
        inputForAPI.setUrl(UrlHelper.SOCIAL_LOGIN);
//        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
//        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        socialLogin(inputForAPI, imageUrl);
    }

    private void socialLogin(InputForAPI inputForAPI, final String imageUrl) {

        signInViewModel.socialSignIn(inputForAPI).observe(this, new Observer<SocialSignInSuccessResponse>() {
            @Override
            public void onChanged(@Nullable SocialSignInSuccessResponse socialSignInSuccessResponse) {

                try {
                    Log.d("soial", socialSignInSuccessResponse.getError().toString());
                    if (socialSignInSuccessResponse != null) {
                        if (!socialSignInSuccessResponse.getError().equalsIgnoreCase("true")) {
                            handleSuccessResponeSocialLogin(socialSignInSuccessResponse, imageUrl);
                        } else {
                            Utils.toast(findViewById(android.R.id.content), socialSignInSuccessResponse.getErrorMessage());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleSuccessResponeSocialLogin(SocialSignInSuccessResponse socialSignInSuccessResponse, String imageUrl) {
        appSettings.setToken(socialSignInSuccessResponse.getAccessToken());
        appSettings.setUserId("" + socialSignInSuccessResponse.getId());
        appSettings.setIsLogged("true");
        appSettings.setIsSocialLogin("true");
        appSettings.setUserImage(imageUrl);
        moveMainActivity();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
            String email = googleSignInAccount.getEmail();
            String first_name, last_name;
            String type = "google";
            String id = googleSignInAccount.getId();
            String imageUrl = String.valueOf(googleSignInAccount.getPhotoUrl());
            String full_name = googleSignInAccount.getDisplayName();
            if (full_name.contains(" ")) {
                String[] names = full_name.split(" ");
                first_name = names[0];
                last_name = names[1];
            } else {
                first_name = full_name;
                last_name = "";
            }
            buildSocialLoginInputs(email, first_name, last_name, id, type, imageUrl);
        }
    }

    private void initViews() {
        dontHaveAnAccount = findViewById(R.id.dontHaveAnAccount);
        facebookLoginButton = (FrameLayout) findViewById(R.id.facebookLoginButton);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        facebookLogin = findViewById(R.id.facebookLogin);
        googleLogin = findViewById(R.id.googleLogin);

    }


    private void initListners() {
        dontHaveAnAccount.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);
        googleLogin.setOnClickListener(this);
        passwordEditText.setFilters(new InputFilter[]{Utils.whiteSpacefilter});
        passwordEditText.setTransformationMethod(new Utils.AsteriskPasswordTransformationMethod());
    }


    @Override
    public void onClick(View view) {
        if (view == dontHaveAnAccount) {
            moveSignUp();
        } else if (view == loginButton) {
            if (isValidInputs().equalsIgnoreCase("true")) {
                try {
                    buildLoginInputs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.toast(findViewById(android.R.id.content), isValidInputs());
            }

        } else if (view == forgotPassword) {
            moveToForgotPassword();
        } else if (view == facebookLogin) {
            if (ConnectionUtils.isNetworkConnected(SignInActivity.this)) {
                loginFbButton.performClick();
            } else {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
            }

        } else if (view == googleLogin) {
            if (ConnectionUtils.isNetworkConnected(SignInActivity.this)) {
                googlesignIn();
            } else {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
            }
        }
    }

    private void buildLoginInputs() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("email", usernameEditText.getText().toString().trim());
        jsonObject.put("password", passwordEditText.getText().toString());
        jsonObject.put("user_type", "User");

        InputForAPI inputForAPI = new InputForAPI(SignInActivity.this);
        inputForAPI.setUrl(UrlHelper.SIGN_IN);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        processLogin(inputForAPI);
    }

    private void moveToForgotPassword() {
        Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private String isValidInputs() {
        String value;
        if (usernameEditText.getText().toString().trim().length() == 0 || !Utils.isValidEmail(usernameEditText.getText().toString().trim())) {
            value = getResources().getString(R.string.please_enter_valid_email);
        } else if (passwordEditText.getText().length() == 0) {
            value = getResources().getString(R.string.please_enter_valid_password);
        } else {
            value = "true";
        }
        return value;
    }

    private void processLogin(InputForAPI inputForAPI) {

        signInViewModel.signIn(inputForAPI).observe(this, new Observer<SignInResponseModel>() {
            @Override
            public void onChanged(@Nullable SignInResponseModel signInResponseModel) {
                if (signInResponseModel != null) {
                    if (signInResponseModel.getError().equalsIgnoreCase("false")) {
                        handleSuccessResponeLogin(signInResponseModel);
                    } else {
                        Utils.toast(findViewById(android.R.id.content),
                                signInResponseModel.getErrorMessage());
                    }
                }

            }
        });
    }

    private void handleSuccessResponeLogin(SignInResponseModel signInResponseModel) {
        appSettings.setToken(signInResponseModel.getAccessToken());
        appSettings.setIsLogged("true");
        appSettings.setIsSocialLogin("false");
        appSettings.setUserImage(signInResponseModel.getImage());
        appSettings.setUserId("" + signInResponseModel.getId());
        appSettings.setUserName(signInResponseModel.getFirstName() + " " + signInResponseModel.getLastName());
        appSettings.setUserNumber("" + signInResponseModel.getMobile());
        if (!signInResponseModel.getDeleteStatus().equalsIgnoreCase("active")) {
            moveMainActivity();
        } else {
            showDialogDeleteStatus();
        }
    }

    private void showDialogDeleteStatus() {

        final Dialog dialog = new Dialog(SignInActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setContentView(R.layout.show_delete_status);

        Window window = dialog.getWindow();

        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            window.setGravity(Gravity.CENTER);
        }
        dialog.show();

        ok = dialog.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appSettings.setIsLogged("false");
                Intent intent = new Intent(SignInActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
            }
        });

    }

    private void moveMainActivity() {
        appSettings.setUserType("user");
        Intent main = new Intent(SignInActivity.this, MainActivity.class);
        main.putExtra(Constants.LOGIN_TYPE, Constants.HOME_TAB);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);

    }


    public void moveSignUp() {
        Intent signup = new Intent(SignInActivity.this, SignUpActivity.class);
        signup.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(signup);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
