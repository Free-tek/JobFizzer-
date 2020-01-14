package com.app.jobfizzer.View.activities;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.MapRipple;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.adapters.ProvidersAdapter;
import com.app.jobfizzer.View.adapters.ProvidersListAdapter;
import com.app.jobfizzer.ViewModel.ShowProvidersViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.app.jobfizzer.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.jobfizzer.Utilities.Constants.Constants.COARSE_LOCATION_PERMISSIONS;

public class ShowProvidersActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    DiscreteScrollView providersRecyclerView;
    GoogleMap map;
    AppSettings appSettings = new AppSettings(ShowProvidersActivity.this);
    ImageView sortProvider;
    ImageView arrowIcon;
    FrameLayout bottomSheet;
    Boolean isSheetExpanded = false;
    RecyclerView providerList;
    ShowProvidersViewModel showProvidersViewModel;
    private List<ShowProvidersResponseModel.ProviderService.AllProvider> providers = new ArrayList<>();
    private int val = 0;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ImageView backButton;
    private MapRipple mapRipple;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_providers);
        showProvidersViewModel = ViewModelProviders.of(this).get(ShowProvidersViewModel.class);
        initViews();

        try {
            getListProviderInputs();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initlocation();
        initListners();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSIONS);
        } else {
            settingsrequest();
            initlocationRequest();
        }
    }

    private void getListProviderInputs() throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service_sub_category_id", appSettings.getSelectedSubCategory());
        jsonObject.put("time_slot_id", appSettings.getSelectedTimeSlot());
        jsonObject.put("date", appSettings.getSelectedDate());
        jsonObject.put("city", appSettings.getSelectedCity());
        jsonObject.put("lat", appSettings.getSelectedlat());
        jsonObject.put("lon", appSettings.getSelectedLong());

        InputForAPI inputForAPI = new InputForAPI(ShowProvidersActivity.this);
        inputForAPI.setUrl(UrlHelper.LIST_PROVIDERS);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        getListProviderData(inputForAPI);
    }

    private void getListProviderData(InputForAPI inputForAPI) {
        showProvidersViewModel.getShowProviders(inputForAPI).observe(this, new Observer<ShowProvidersResponseModel>() {
            @Override
            public void onChanged(@Nullable ShowProvidersResponseModel showProvidersResponseModel) {


                if (showProvidersResponseModel != null) {
                    if (!showProvidersResponseModel.getError().equalsIgnoreCase("true")) {
                        handleSuccessRespone(showProvidersResponseModel);
                    } else {
                        Utils.toast(findViewById(android.R.id.content), showProvidersResponseModel.getErrorMessage());
                    }
                }
            }
        });
    }

    public void handleSuccessRespone(ShowProvidersResponseModel response) {
        providers = response.getAllProviders();
        if (providers != null) {
            double lat = Double.parseDouble(String.valueOf(providers.get(0).getLatitude()));
            double lng = Double.parseDouble(String.valueOf(providers.get(0).getLongitude()));
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            map.moveCamera(center);
            map.animateCamera(zoom);
            val++;
            mapRipple = new MapRipple(map, new LatLng(lat,
                    lng), ShowProvidersActivity.this);

            for (int i = 0; i < providers.size(); i++) {
                createMarker(i);
            }
            setProviderListAdapter();
        }

    }

    private void setProviderListAdapter() {
        final ProvidersAdapter adapter = new ProvidersAdapter(ShowProvidersActivity.this, providers);
        providersRecyclerView.setAdapter(adapter);
        final ProvidersListAdapter adapters = new ProvidersListAdapter(ShowProvidersActivity.this, providers);
        LinearLayoutManager verticalmanager = new LinearLayoutManager(ShowProvidersActivity.this, LinearLayoutManager.VERTICAL, false);
        providerList.setLayoutManager(verticalmanager);
        providerList.setAdapter(adapters);
    }

    private void initlocation() {

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
                .setFastestInterval(1000);

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
                            status.startResolutionForResult(ShowProvidersActivity.this, 5);
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


    public void createRippleMarker(final LatLng latLng) {

        try {
            mapRipple.stopRippleMapAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapRipple.withLatLng(latLng);
        mapRipple.withNumberOfRipples(3);
        mapRipple.withFillColor(Utils.getPrimaryCOlor(ShowProvidersActivity.this));
        mapRipple.withStrokeColor(Utils.getPrimaryCOlor(ShowProvidersActivity.this));
        mapRipple.withStrokewidth(10);   // 10dp
        mapRipple.withDistance(10);   // 2000 metres radius
        mapRipple.withRippleDuration(3000);   //12000ms
        mapRipple.withTransparency(0.5f);
        mapRipple.withDurationBetweenTwoRipples(0);
        mapRipple.startRippleMapAnimation();      //in onMapReadyCallBack

    }

    public void createMarker(int position) {
        Bitmap smallMarker = showProvidersViewModel.getBitmap(ShowProvidersActivity.this);
        MarkerOptions markerOptions = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        double lat = Double.parseDouble(String.valueOf(providers.get(position).getLatitude()));
        double lng = Double.parseDouble(String.valueOf(providers.get(position).getLongitude()));
        LatLng latLng = new LatLng(lat, lng);
        markerOptions.position(latLng);
        map.addMarker(markerOptions);
    }

    private void initListners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSheetExpanded) {
                    onBackPressed();
                }
            }
        });
        sortProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPoup();
            }
        });


        providersRecyclerView.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener
                <RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                double lat = providers.get(adapterPosition).getLatitude();
                double lng = providers.get(adapterPosition).getLongitude();
                LatLng latLng = new LatLng(lat, lng);
                createRippleMarker(latLng);
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(15).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

    }

    private void initViews() {
        providersRecyclerView = findViewById(R.id.providersRecyclerView);
        providerList = findViewById(R.id.providerList);
        sortProvider = findViewById(R.id.sortProvider);
        arrowIcon = findViewById(R.id.arrowIcon);
        backButton = findViewById(R.id.backButton);
        bottomSheet = findViewById(R.id.bottomSheet);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    isSheetExpanded = true;
                    bottomSheet.setBackgroundColor(getResources().getColor(R.color.white));
                    arrowIcon.setRotation(180);
                }

                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_DRAGGING) {
                    isSheetExpanded = false;
                    bottomSheet.setBackgroundColor(getResources().getColor(R.color.white));
                }
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    isSheetExpanded = false;
                    bottomSheet.setBackgroundColor(getResources().getColor(R.color.transparent));
                    arrowIcon.setRotation(0);

                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    public void showFilterPoup() {
        Dialog dialog = new Dialog(ShowProvidersActivity.this);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.filter_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.clear();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(false);
        map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map));
        map.getUiSettings().setMyLocationButtonEnabled(false);
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
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
