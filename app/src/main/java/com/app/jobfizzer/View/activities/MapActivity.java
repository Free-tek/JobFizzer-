package com.app.jobfizzer.View.activities;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.jobfizzer.Model.AddResponseModel.ListAddress;
import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Model.GoogleLocationResponseModel.AddressComponent;
import com.app.jobfizzer.Model.GoogleLocationResponseModel.GooglePlaceResponseModel;
import com.app.jobfizzer.Model.GoogleLocationResponseModel.Result;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.ViewModel.MapViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static com.app.jobfizzer.Utilities.Constants.Constants.COARSE_LOCATION_PERMISSIONS;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    ListAddress listAddress;
    MapViewModel mapViewModel;
    CommonViewModel commonViewModel;
    private String dist = "";
    private GoogleMap map;
    private Button confirmButton;
    private TextView address_add_tv, custom_address;
    private TextView toolbarTitle;
    private ImageView myLocation;
    private ImageView backButton;
    private EditText locationName;
    private EditText title, doorNo, landMark;
    private SupportMapFragment mapFragment;
    private LinearLayout addAddressButton, editLayout;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    private String TAG = MapActivity.class.getSimpleName();
    private boolean isAddressShown = false;
    private int value = 0;
    private String cityName = "";
    private String type;
    private CheckBox checkBox;
    private String address_id;
    private LinearLayout inputText;

    String str_longitude;
    String str_latitude;
    String str_areaName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        initViews();
        initlocation();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSIONS);
        } else {
            settingsrequest();
            initlocationRequest();
        }
        initListners();
    }


    private void initlocation() {
        checkCallingOrSelfPermission("");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }


    private void initlocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);

    }

    public void settingsrequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MapActivity.this, 5);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    private void initListners() {

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isValidInputs().equalsIgnoreCase("true")) {
                    proceedClickConfirmButton();
                } else {
                    Utils.toast(findViewById(android.R.id.content), isValidInputs());
                }
            }
        });

        addAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAddressShown) {
                    Utils.hideKeyboard(MapActivity.this);
                    slideDown();
                } else {
                    slideUp();
                }
            }
        });

        inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConnectionUtils.isNetworkConnected(MapActivity.this)) {
                    Intent intent = new Intent(getApplicationContext(), ChooseLocationActivity.class);
                    startActivityForResult(intent, 100);
                } else {
                    Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                }
            }
        });

        locationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseLocationActivity.class);
                startActivityForResult(intent, 100);
            }
        });

    }

    private void proceedClickConfirmButton() {
        if (type.equalsIgnoreCase("edit")) {
            try {
                buildUpdateInputs();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            try {
                saveAddressInputs();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private void saveAddressInputs() throws JSONException {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("address", locationName.getText().toString());
        jsonObject.put("latitude", "" + currentLatitude);
        jsonObject.put("longitude", "" + currentLongitude);
        jsonObject.put("doorno", doorNo.getText().toString());
        jsonObject.put("landmark", landMark.getText().toString());
        jsonObject.put("city", cityName);
        jsonObject.put("title", title.getText().toString());

        InputForAPI inputForAPI = new InputForAPI(MapActivity.this);
        inputForAPI.setUrl(UrlHelper.ADD_ADDRESS);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        saveAddress(inputForAPI);

    }

    private void updateAddress(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {

                if (flagResponseModel != null) {
                    if (flagResponseModel.getError().equalsIgnoreCase("false")) {
                        handleUpdateSucessResponse();

                    } else {
                    }
                }
            }
        });

    }

    private void handleUpdateSucessResponse() {
        /*Intent intent = new Intent(MapActivity.this, AddressActivity.class);
         *//* intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*//*
        startActivity(intent);*/
        finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

    }


    private void slideDown() {
        isAddressShown = false;
        Animation animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.slidetobottom);
        editLayout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                editLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void slideUp() {
        isAddressShown = true;
        editLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(MapActivity.this, R.anim.slidefrombottom);
        editLayout.startAnimation(animation);


    }


    private String isValidInputs() {
        String val;
        if (locationName.getText().toString().trim().length() == 0) {
            val = getResources().getString(R.string.enter_valid_address);
        } else if (doorNo.getText().toString().length() == 0) {
            val = getResources().getString(R.string.enter_valid_door_no);
        } else if (landMark.getText().toString().length() == 0) {
            val = getResources().getString(R.string.enter_valid_landmark);
        } else if (title.getText().toString().length() == 0) {
            val = getResources().getString(R.string.enter_valid_title);
        } else {
            val = "true";
        }
        return val;
    }

    private void saveAddress(InputForAPI inputForAPI) {

        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                try {
                    handleSaveSuccessResponse();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });

    }

    private void handleSaveSuccessResponse() {

        Utils.toast(findViewById(android.R.id.content), getResources().getString(R.string.address_added_successully));
        if (type.equalsIgnoreCase("save")) {
           /* Intent intent = new Intent(MapActivity.this, AddressActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

        } else {
           /* Intent intent = new Intent(MapActivity.this, SelectTimeAndAddressActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            finish();
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
        }
    }


    private void buildUpdateInputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", listAddress.getId());
        jsonObject.put("address", locationName.getText().toString());
        jsonObject.put("latitude", "" + currentLatitude);
        jsonObject.put("longitude", "" + currentLongitude);
        jsonObject.put("doorno", doorNo.getText().toString());
        jsonObject.put("landmark", landMark.getText().toString());
        jsonObject.put("city", cityName);
        jsonObject.put("title", title.getText().toString());

        InputForAPI inputForAPI = new InputForAPI(MapActivity.this);
        inputForAPI.setUrl(UrlHelper.UPDATE_ADDRESS);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        updateAddress(inputForAPI);


    }

    private void initViews() {
        confirmButton = findViewById(R.id.confirmButton);
        address_add_tv = findViewById(R.id.address_add_tv);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        custom_address = findViewById(R.id.custom_address);
        checkBox = findViewById(R.id.checkBox);
        backButton = findViewById(R.id.backButton);
        myLocation = findViewById(R.id.myLocation);
        locationName = findViewById(R.id.locationName);
        title = findViewById(R.id.title);
        doorNo = findViewById(R.id.doorNo);
        landMark = findViewById(R.id.landMark);
        editLayout = findViewById(R.id.editLayout);
        addAddressButton = findViewById(R.id.addAddressButton);
        inputText = findViewById(R.id.inputText);

        editLayout.setVisibility(View.GONE);

        title.setFilters(new InputFilter[]{Utils.emojiChatFilter});
        doorNo.setFilters(new InputFilter[]{Utils.emojiChatFilter});
        landMark.setFilters(new InputFilter[]{Utils.emojiChatFilter});

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        addAddressButton.setVisibility(View.VISIBLE);

        editLayout.setVisibility(View.GONE);
        type = getIntent().getStringExtra("from");
        listAddress = (ListAddress) getIntent().getSerializableExtra("modelValue");

        if (type.equalsIgnoreCase("edit")) {
            slideUp();
            locationName.setEnabled(true);
            checkBox.setChecked(true);
            locationName.setText(listAddress.getAddressLine1());

            title.setText(listAddress.getTitle());
            doorNo.setText(listAddress.getDoorno());
            landMark.setText(listAddress.getLandmark());
            confirmButton.setText(getResources().getString(R.string.save));
            address_add_tv.setText(getResources().getString(R.string.change_address));
            toolbarTitle.setText(getResources().getString(R.string.change_address));
        } else {
            locationName.setEnabled(false);
            checkBox.setChecked(false);
            address_add_tv.setText(getResources().getString(R.string.add_address_caps));
            toolbarTitle.setText(getResources().getString(R.string.create_new_address));
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    locationName.setEnabled(true);
                    locationName.setText("");
                    locationName.setHint(getString(R.string.enter_address));
                } else {
                    locationName.setEnabled(false);
                }
            }
        });

        myLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.checkGpsisEnabled(MapActivity.this)) {
                    LatLng myLocation;

                    if (map != null) {
                        if (map.getMyLocation() != null) {
                            myLocation = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(18).build();
                            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }
                } else {
                    settingsrequest();
                    initlocationRequest();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setBuildingsEnabled(true);
        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                if (!locationName.isEnabled()) {
                    locationName.setText("");
                    locationName.setHint(getResources().getString(R.string.fetching));
                }

            }
        });
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                try {
                    getCompleteAddress();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if (value == 0) {
                    LatLng myLocation;

                    myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(18).build();
                    map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    map.setPadding(0, 0, 0, 0);
                    map.getUiSettings().setZoomControlsEnabled(false);
                    map.getUiSettings().setMapToolbarEnabled(false);
                    map.getUiSettings().setCompassEnabled(false);
                    value++;

                }

            }
        });

        if (type.equalsIgnoreCase("edit")) {
            LatLng myLocation;
            currentLatitude = listAddress.getLatitude();
            currentLongitude = listAddress.getLongitude();
            myLocation = new LatLng(currentLatitude, currentLongitude);

            CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(18).build();
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            map.setPadding(0, 0, 0, 0);
            map.getUiSettings().setZoomControlsEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setMapToolbarEnabled(false);
            map.getUiSettings().setCompassEnabled(false);
            value++;
        }

        map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        this, R.raw.map));
    }

    private void handleAddressResponse(GooglePlaceResponseModel result) {
        currentLatitude = map.getCameraPosition().target.latitude;
        currentLongitude = map.getCameraPosition().target.longitude;

        List<Result> Results;
        Results = result.getResults();
        Result zero = Results.get(0);
        List<AddressComponent> address_components = zero
                .getAddressComponents();
        for (int i = 0; i < address_components.size(); i++) {
            AddressComponent zero2 = address_components
                    .get(i);
            String long_name = zero2.getLongName();
            List<String> mtypes = zero2.getTypes();
            String Type = mtypes.get(0);
            if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                cityName = long_name;
            }
        }
        String strAdd = result.getResults().get(0).getFormattedAddress();
        if (!locationName.isEnabled()) {
            locationName.setText(strAdd);
        }

    }

    private void getCompleteAddress() {
        String url = buildUrl(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);

        InputForAPI inputForAPI = new InputForAPI(MapActivity.this);
        inputForAPI.setUrl(url);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(new HashMap<String, String>());
        getAddressDetails(inputForAPI);
    }

    private void getAddressDetails(InputForAPI inputForAPI) {


        mapViewModel.getAddresDetails(inputForAPI).observe(
                this, new Observer<GooglePlaceResponseModel>() {
                    @Override
                    public void onChanged(@Nullable GooglePlaceResponseModel googlePlaceResponseModel) {
                        if (googlePlaceResponseModel != null) {
                            if (googlePlaceResponseModel.getResults().size() > 0) {
                                handleAddressResponse(googlePlaceResponseModel);
                            }
                        }
                    }
                }
        );


    }

    private String buildUrl(double latitude, double longitude) {

        return mapViewModel.getCombinedString(latitude, longitude);

    }


    @Override
    public void onLocationChanged(Location location) {
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                        location.getLongitude()));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.moveCamera(center);
        map.animateCamera(zoom);

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 106) {
            if (grantResults.length > 0) {

                initlocationRequest();
            }
        }

        if (requestCode == COARSE_LOCATION_PERMISSIONS) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initlocationRequest();
                settingsrequest();

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {

            try {
                str_latitude = data.getStringExtra("latitude");
                str_longitude = data.getStringExtra("longitude");
                str_areaName = data.getStringExtra("areaname");

                locationName.setText(str_areaName);
                moveCamera(str_latitude, str_longitude);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void moveCamera(String latitude, String longitude) {
        LatLng myLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(18).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } catch (Exception e) {

            }
        } else {

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            Utils.log(TAG, "mylat:" + currentLatitude + ",mylong:" + currentLongitude);

        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }

}
