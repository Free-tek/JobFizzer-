package com.app.jobfizzer.View.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.jobfizzer.Model.ImageUploadSuccessResponse;
import com.app.jobfizzer.Model.SignUpResponseModel;
import com.app.jobfizzer.Model.SocialSignInSuccessResponse;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.CustomLibraries.CircleImageView;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.ViewModel.SignInViewModel;
import com.app.jobfizzer.ViewModel.SignUpViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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

import java.io.File;
import java.util.HashMap;

import static com.app.jobfizzer.Utilities.Constants.Constants.CAMERA_INTENT;
import static com.app.jobfizzer.Utilities.Constants.Constants.CAMERA_STORAGE_PERMISSIONS;
import static com.app.jobfizzer.Utilities.Constants.Constants.GALLERY_INTENT;
import static com.app.jobfizzer.Utilities.Constants.Constants.READ_STORAGE_PERMISSIONS;
import static com.app.jobfizzer.Utilities.helpers.Utils.emojiSpecialFilter;
import static com.app.jobfizzer.Utilities.helpers.Utils.whiteSpacefilter;


/**
 * Created by karthik on 01/10/17.
 */
public class SignUpActivity extends BaseActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, passwordEditText;
    Button signUpButton, alreadyHaveAnAccount;
    CircleImageView profilePic;
    File uploadFile;
    ImageLoader imageLoader;
    AppSettings appSettings;
    SignUpViewModel signUpViewModel;
    CommonViewModel commonViewModel;
    private Uri uri;
    private String TAG = SignUpActivity.class.getSimpleName();
    private String uploadImagepath = "";
    ImageButton facebookLogin, googleLogin;
    LoginButton loginFbButton;
    CallbackManager callbackManager;
    public GoogleApiClient mGoogleApiClient;
    private final int RC_SIGN_IN = 420;
    SignInViewModel signInViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        imageLoader = new ImageLoader(this);
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        signInViewModel = ViewModelProviders.of(this).get(SignInViewModel.class);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        initViews();
        initListners();
        initFacebookLogin();
        initGoogleLogin();

    }

    private void initViews() {
        appSettings = new AppSettings(SignUpActivity.this);
        alreadyHaveAnAccount = findViewById(R.id.alreadyHaveAnAccount);
        signUpButton = findViewById(R.id.signUpButton);
        facebookLogin = findViewById(R.id.facebookLogin);
        googleLogin = findViewById(R.id.googleLogin);

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        profilePic = findViewById(R.id.profilePic);
        ColorFilter colorFilter = new PorterDuffColorFilter(Utils.getPrimaryCOlor(SignUpActivity.this),
                PorterDuff.Mode.SRC_IN);
        profilePic.setColorFilter(colorFilter);

        passwordEditText.setTransformationMethod(new Utils.AsteriskPasswordTransformationMethod());
        passwordEditText.setFilters(new InputFilter[]{whiteSpacefilter});
        firstNameEditText.setFilters(new InputFilter[]{emojiSpecialFilter, new InputFilter.LengthFilter(15)});
        lastNameEditText.setFilters(new InputFilter[]{emojiSpecialFilter, new InputFilter.LengthFilter(15)});

    }

    private void initListners() {
        alreadyHaveAnAccount.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        profilePic.setOnClickListener(this);
        facebookLogin.setOnClickListener(this);
        googleLogin.setOnClickListener(this);
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
                Log.d("facebookonSuccess", "onSuccess");
            }

            @Override
            public void onCancel() {
                Log.d("facebookonCancel", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("facebookonError", "onError");
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
        AlertDialog.Builder alert = new AlertDialog.Builder(SignUpActivity.this);
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
                jsonObject.put("image", " ");
            } else {
                jsonObject.put("image", imageUrl);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        InputForAPI inputForAPI = new InputForAPI(SignUpActivity.this);
        inputForAPI.setUrl(UrlHelper.SOCIAL_LOGIN);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        socialLogin(inputForAPI, imageUrl);
    }

    private void socialLogin(InputForAPI inputForAPI, final String imageUrl) {

        signInViewModel.socialSignIn(inputForAPI).observe(this, new Observer<SocialSignInSuccessResponse>() {
            @Override
            public void onChanged(@Nullable SocialSignInSuccessResponse socialSignInSuccessResponse) {
                if (socialSignInSuccessResponse != null) {
                    if (!socialSignInSuccessResponse.getError().equalsIgnoreCase("true")) {
                        handleSuccessResponeSocialLogin(socialSignInSuccessResponse, imageUrl);
                    } else {
                        Utils.toast(findViewById(android.R.id.content), socialSignInSuccessResponse.getErrorMessage());
                    }
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

    private void moveMainActivity() {
        appSettings.setUserType("user");
        Intent main = new Intent(SignUpActivity.this, MainActivity.class);
        main.putExtra(Constants.LOGIN_TYPE, Constants.HOME_TAB);
        main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(main);
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_left);

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


    @Override
    public void onClick(View view) {

        if (view == alreadyHaveAnAccount) {
            moveToSignIn();
        } else if (view == signUpButton) {

            if (ConnectionUtils.isNetworkConnected(SignUpActivity.this)) {
                if (validInputs().equalsIgnoreCase("true")) {
                    Utils.show(SignUpActivity.this);
                    proceedSignup();
                } else {
                    Utils.toast(findViewById(android.R.id.content), validInputs());
                }
            } else {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
            }

        } else if (view == profilePic) {
            showPictureDialog();
        } else if (view == facebookLogin) {
            if (ConnectionUtils.isNetworkConnected(SignUpActivity.this)) {
                loginFbButton.performClick();
            } else {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
            }
        } else if (view == googleLogin) {
            if (ConnectionUtils.isNetworkConnected(SignUpActivity.this)) {
                googlesignIn();
            } else {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
            }
        }
    }

    private void proceedSignup() {
        if (uploadFile != null) {
            uploadImage();
        } else {
            try {
                signUpApiInputs();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void showPictureDialog() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.choose_your_option));
        String[] items = {getResources().getString(R.string.gallery), getResources().getString(R.string.camera)};

        dialog.setItems(items, new DialogInterface.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch (which) {
                    case 0:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSIONS);
                        } else {
                            choosePhotoFromGallary();
                        }
                        break;
                    case 1:

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_STORAGE_PERMISSIONS);
                        } else {
                            takePhotoFromCamera();
                        }


                        break;

                }
            }
        });
        dialog.show();
    }

    private void choosePhotoFromGallary() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY_INTENT);

    }

    private void takePhotoFromCamera() {
        File file = Utils.getUploadFileName(SignUpActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(SignUpActivity.this, getPackageName() + ".provider", file);
            appSettings.setImageUploadPath(file.getAbsolutePath());
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, CAMERA_INTENT);
        } else {
            uri = Uri.fromFile(file);
            appSettings.setImageUploadPath(file.getAbsolutePath());
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, CAMERA_INTENT);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("requestCode", "" + requestCode);
        switch (requestCode) {
            case CAMERA_STORAGE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoFromCamera();
                } else {
                    Utils.toast(findViewById(android.R.id.content), getString(R.string.camera_permission_error));
                }
                break;

            case READ_STORAGE_PERMISSIONS:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromGallary();
                } else {
                    Utils.toast(findViewById(android.R.id.content), getString(R.string.storage_permission_error));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void loadImage(String url, ImageView imageView, Drawable drawable) {
        imageLoader.load(url, imageView, drawable);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GALLERY_INTENT:
                if (data != null) {
                    uri = data.getData();
                    if (uri != null) {
                        handleimage(uri);
                    } else {
                        Utils.toast(findViewById(android.R.id.content), getString(R.string.unable_to_select_image));
                    }
                }
                break;
            case CAMERA_INTENT://camera
                uploadFile = new File(String.valueOf(appSettings.getImageUploadPath()));
                if (uploadFile.exists()) {
                    profilePic.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.transparent));
                    loadImage(appSettings.getImageUploadPath(), profilePic, Utils.getProfilePicture(SignUpActivity.this));
                }
                break;

            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
            default:
                callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void handleimage(Uri uri) {
        profilePic.setColorFilter(ContextCompat.getColor(SignUpActivity.this, R.color.transparent));
        loadImage(Utils.getRealPathFromUriNew(SignUpActivity.this, uri), profilePic, Utils.getProfilePicture(SignUpActivity.this));
        uploadFile = new File(Utils.getRealPathFromURI(SignUpActivity.this, uri));
    }


    private void uploadImage() {
        new UploadImage().execute();
    }

    private void uploadImageInputs(File imageFile) {


        InputForAPI inputForAPI = new InputForAPI(SignUpActivity.this);
        inputForAPI.setUrl(UrlHelper.UPLOAD_IMAGE);
        inputForAPI.setFile(imageFile);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        uploadImageFile(inputForAPI);

    }

    private void uploadImageFile(InputForAPI inputForAPI) {
        commonViewModel.uploadImage(inputForAPI).observe(this, new Observer<ImageUploadSuccessResponse>() {
            @Override
            public void onChanged(@Nullable ImageUploadSuccessResponse imageUploadSuccessResponse) {
                uploadImagepath = imageUploadSuccessResponse.getImage();
                try {
                    signUpApiInputs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String validInputs() {
        String returnVal;
        if (firstNameEditText.getText().toString().trim().length() == 0) {
            returnVal = getResources().getString(R.string.please_enter_valid_firstname);
        } else if (lastNameEditText.getText().toString().trim().length() == 0) {
            returnVal = getResources().getString(R.string.please_enter_valid_lastname);
        } else if (emailEditText.getText().toString().trim().length() == 0 || !Utils.isValidEmail(emailEditText.getText().toString().trim())) {
            returnVal = getResources().getString(R.string.please_enter_valid_email);
        } else if (passwordEditText.getText().length() == 0) {
            returnVal = getResources().getString(R.string.please_enter_valid_password);
        } else if (passwordEditText.getText().toString().trim().length() < 6) {
            return getResources().getString(R.string.password_must_be_six);

        } else if (phoneEditText.getText().length() == 0 && phoneEditText.getText().length() < 7) {
            returnVal = getResources().getString(R.string.please_enter_valid_phoneNumber);
        } else {
            returnVal = "true";
        }
        return returnVal;
    }

    private void signUpApiInputs() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first_name", firstNameEditText.getText().toString().trim());
        jsonObject.put("last_name", lastNameEditText.getText().toString().trim());
        jsonObject.put("email", emailEditText.getText().toString().trim());
        jsonObject.put("password", passwordEditText.getText().toString());
        jsonObject.put("mobile", phoneEditText.getText().toString());
        jsonObject.put("image", "" + uploadImagepath);
        InputForAPI inputForAPI = new InputForAPI(SignUpActivity.this);
        inputForAPI.setUrl(UrlHelper.SIGN_UP);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(new HashMap<String, String>());
        signUp(inputForAPI);


    }

    private void signUp(InputForAPI inputForAPI) {

        signUpViewModel.signUp(inputForAPI).observe(this, new Observer<SignUpResponseModel>() {
            @Override
            public void onChanged(@Nullable SignUpResponseModel signUpResponseModel) {

                if (signUpResponseModel != null) {
                    if (signUpResponseModel.getError().equalsIgnoreCase("false")) {
                        Utils.toast(findViewById(android.R.id.content), getString(R.string.account_created_successfully));
                        moveToSignIn();
                    } else {
                        Utils.toast(findViewById(android.R.id.content), signUpResponseModel.getErrorMessage());
                    }
                }
            }
        });


    }


    private void moveToSignIn() {
        Intent signin = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(signin);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class UploadImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Utils.show(SignUpActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File imageFile = Utils.getCompressedFile(SignUpActivity.this, uploadFile);
            uploadImageInputs(imageFile);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //  Utils.dismiss();
        }
    }
}
