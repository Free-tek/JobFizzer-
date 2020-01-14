package com.app.jobfizzer.View.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Model.ImageUploadSuccessResponse;
import com.app.jobfizzer.Model.UserDetails;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static com.app.jobfizzer.Utilities.Constants.Constants.CAMERA_INTENT;
import static com.app.jobfizzer.Utilities.Constants.Constants.CAMERA_STORAGE_PERMISSIONS;
import static com.app.jobfizzer.Utilities.Constants.Constants.GALLERY_INTENT;
import static com.app.jobfizzer.Utilities.Constants.Constants.READ_STORAGE_PERMISSIONS;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    ImageLoader imageLoader;
    UserDetails userDetails = new UserDetails();

    CommonViewModel commonViewModel;
    private EditText firstName, lastName, mobileNumber;
    private Button bottomButton;
    private ImageView profileImage;
    private ImageView backButton;
    private Uri uri;
    private File uploadFile;
    private String uploadImagepath;
    private String TAG = EditProfileActivity.class.getSimpleName();
    private AppSettings appSettings;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_edit_profile);
        getWindow().setExitTransition(new Explode());
        imageLoader = new ImageLoader(this);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        userDetails = (UserDetails) getIntent().getSerializableExtra("provider_details");
        initViews();
        initListners();
        try {
            setValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListners() {
        bottomButton.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }


    public void loadImage(String url, ImageView imageView, Drawable drawable) {
        imageLoader.load(url, imageView, drawable);
    }

    private void setValues() throws JSONException {
        firstName.setText(userDetails.getFirstName());
        lastName.setText(userDetails.getLastName());
        mobileNumber.setText(userDetails.getMobile());
        uploadImagepath = userDetails.getImage();
        loadImage(uploadImagepath, profileImage, Utils.getProfilePicture(this));

    }

    private void initViews() {
        appSettings = new AppSettings(EditProfileActivity.this);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        mobileNumber = findViewById(R.id.mobileNumber);
        profileImage = findViewById(R.id.profileImage);
        bottomButton = findViewById(R.id.bottomButton);
        backButton = findViewById(R.id.backButton);
        firstName.setFilters(new InputFilter[]{Utils.emojiSpecialFilter, new InputFilter.LengthFilter(15)});
        lastName.setFilters(new InputFilter[]{Utils.emojiSpecialFilter, new InputFilter.LengthFilter(15)});
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
        File file = commonViewModel.getFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uri = FileProvider.getUriForFile(EditProfileActivity.this, getPackageName() + ".provider", file);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GALLERY_INTENT://gallery
                if (data != null) {
                    uri = data.getData();
                    if (uri != null) {
                        handleimage(uri);
                    } else {
                        Utils.toast(findViewById(android.R.id.content), getString(R.string.unable_to_select_image));
                    }
                }
                break;
            case CAMERA_INTENT:
                uploadFile = new File(String.valueOf(appSettings.getImageUploadPath()));
                if (uploadFile.exists()) {
                    Utils.log(TAG, "onActivityResult: " + appSettings.getImageUploadPath());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        loadImage(appSettings.getImageUploadPath(), profileImage, Utils.getMapRequest(EditProfileActivity.this));
                    }
                    break;


                }
        }
    }

    private void handleimage(Uri uri) {
        loadImage(Utils.getRealPathFromUriNew(EditProfileActivity.this, uri), profileImage, Utils.getMapRequest(EditProfileActivity.this));
        uploadFile = new File(Utils.getRealPathFromURI(EditProfileActivity.this, uri));
    }


    @Override
    public void onClick(View view) {
        if (view == profileImage) {
            showPictureDialog();
        } else if (view == bottomButton) {
            if (ConnectionUtils.isNetworkConnected(EditProfileActivity.this)) {
                if (uploadFile == null) {
                    try {
                        if (validInputs().equalsIgnoreCase("true")) {
                            buildInputs();
                        } else {
                            Utils.toast(findViewById(android.R.id.content), validInputs());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    uploadImage();
                }
            } else {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));

            }
        } else if (view == backButton) {
            onBackPressed();
        }
    }

    private void buildInputs() throws JSONException {

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("first_name", firstName.getText().toString().trim());
        jsonObject.put("last_name", lastName.getText().toString().trim());
        jsonObject.put("mobile", mobileNumber.getText().toString());
        jsonObject.put("image", uploadImagepath);

        InputForAPI inputForAPI = new InputForAPI(EditProfileActivity.this);
        inputForAPI.setUrl(UrlHelper.UPDATE_PROFILE);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        updateValues(inputForAPI);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }

    private void uploadImage() {
        new UploadImage().execute();
    }

    private void updateValues(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                if (flagResponseModel != null)
                    if (flagResponseModel.getError().equalsIgnoreCase("true")) {
                        Utils.toast(findViewById(android.R.id.content), getString(R.string.mobile_number_already_registered));
                    } else {
                        handleUpdateProfileResponse();
                    }

            }
        });
    }

    private void handleUpdateProfileResponse() {
        Utils.toast(findViewById(android.R.id.content), getString(R.string.profile_updated_successfully));
        moveMainActivity();
    }

    private void moveMainActivity() {
        Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
        intent.putExtra(Constants.LOGIN_TYPE, Constants.HOME_TAB);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }


    private void uploadImageInputs(File imageFile) {
        InputForAPI inputForAPI = new InputForAPI(EditProfileActivity.this);
        inputForAPI.setUrl(UrlHelper.UPLOAD_IMAGE);
        inputForAPI.setFile(imageFile);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        uploadImageFile(inputForAPI);
    }

    private void uploadImageFile(InputForAPI inputForAPI) {

        commonViewModel.uploadImage(inputForAPI).observe(this, new Observer<ImageUploadSuccessResponse>() {
            @Override
            public void onChanged(@Nullable ImageUploadSuccessResponse imageUploadSuccessResponse) {
                if (imageUploadSuccessResponse != null) {
                    uploadImagepath = imageUploadSuccessResponse.getImage();

                    Log.d("uploadImagepath", uploadImagepath);
                }
                try {
                    if (validInputs().equalsIgnoreCase("true")) {
                        buildInputs();
                    } else {
                        Utils.toast(findViewById(android.R.id.content), validInputs());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private class UploadImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Utils.show(EditProfileActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File imageFile = Utils.getCompressedFile(EditProfileActivity.this, uploadFile);
            uploadImageInputs(imageFile);
            Log.d("image_file_asy", imageFile.getAbsolutePath());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (uploadImagepath == null) {
                Utils.toast(findViewById(android.R.id.content), getString(R.string.image_failed));
            }
        }
    }

    private String validInputs() {
        String returnVal;
        if (firstName.getText().toString().trim().length() == 0) {
            returnVal = getResources().getString(R.string.please_enter_valid_firstname);
        } else if (lastName.getText().toString().trim().length() == 0) {
            returnVal = getResources().getString(R.string.please_enter_valid_lastname);
        } else if (mobileNumber.getText().toString().trim().length() == 0) {
            returnVal = getResources().getString(R.string.please_enter_valid_mobilenumber);
        } else {
            returnVal = "true";
        }
        return returnVal;
    }
}

