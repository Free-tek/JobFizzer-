package com.app.jobfizzer.View.activities;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jobfizzer.Model.DistanceResponseModel.Distance;
import com.app.jobfizzer.Model.DistanceResponseModel.DistanceResponseModel;
import com.app.jobfizzer.Model.DistanceResponseModel.Duration;
import com.app.jobfizzer.Model.DistanceResponseModel.Leg;
import com.app.jobfizzer.Model.GetProviderLocationResponseModel;
import com.app.jobfizzer.Model.ViewBookingsResponseModel.AllBooking;
import com.app.jobfizzer.Utilities.TrackingHelpers.AbstractRouting;
import com.app.jobfizzer.Utilities.TrackingHelpers.Route;
import com.app.jobfizzer.Utilities.TrackingHelpers.RouteException;
import com.app.jobfizzer.Utilities.TrackingHelpers.Routing;
import com.app.jobfizzer.Utilities.TrackingHelpers.RoutingListener;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.ViewModel.TrackingViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.app.jobfizzer.Utilities.Constants.Constants.FINE_LOCATION_PERMISSIONS;

public class TrackingActivity extends BaseActivity implements OnMapReadyCallback, LocationListener, RoutingListener {

    public double src_lat, src_lng, des_lat, des_lng;
    GoogleMap map;
    TextView distanceView, etaView;
    String mobileNumber;
    ImageLoader imageLoader;
    ImageView callButton;
    String placeHolderUrl = "http://104.131.74.144/uber_test/profile_pic.png";
    AppSettings appSettings = new AppSettings(TrackingActivity.this);
    String img;
    ImageView dummyIcon;
    BitmapDescriptor srcBitmap, desBitmap;
    TrackingViewModel trackingViewModel;
    private List<Polyline> polylines;
    private android.location.LocationManager locationManager;
    private String TAG = TrackingActivity.class.getSimpleName();
    private String dist, dura, provider_id;
    private LatLng origin, dest;
    private Socket socket;
    private MarkerOptions srcmarkerOptions;
    AllBooking bookingValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        imageLoader = new ImageLoader(this);
        trackingViewModel = ViewModelProviders.of(this).get(TrackingViewModel.class);
        getIntentValues();
        initSockets();
        createBitmaps();
        initViews();
        initLocation();
        buildDistanceInputs(src_lat, src_lng, des_lat, des_lng);
        try {
            buildLocationForInputs();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void buildLocationForInputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("provider_id", provider_id);
        InputForAPI inputForAPI = new InputForAPI(TrackingActivity.this);
        inputForAPI.setUrl(UrlHelper.GET_PROVIDER_LOCATION);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(TrackingActivity.this));
        getLocationFromServer(inputForAPI);
    }

    private void initLocation() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSIONS);
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
    }

    private void getIntentValues() {

        bookingValues = (AllBooking) getIntent().getSerializableExtra("bookingValues");
        mobileNumber = String.valueOf(bookingValues.getProviderMobile());
        img = bookingValues.getImage();
        provider_id = String.valueOf(bookingValues.getProviderId());
        src_lat = bookingValues.getProviderLatitude();
        src_lng = bookingValues.getProviderLongitude();
        des_lat = bookingValues.getBoookingLatitude();
        des_lng = bookingValues.getBookingLongitude();

//        mobileNumber = getIntent().getStringExtra("mobileNumber");
//        provider_id = getIntent().getStringExtra("provider_id");
//        img = getIntent().getStringExtra("provider_image");
//        src_lat = Double.parseDouble(getIntent().getStringExtra("src_lat"));
//        src_lng = Double.parseDouble(getIntent().getStringExtra("src_lng"));
//        des_lat = Double.parseDouble(getIntent().getStringExtra("des_lat"));
//        des_lng = Double.parseDouble(getIntent().getStringExtra("des_lng"));

        origin = new LatLng(src_lat, src_lng);
        dest = new LatLng(des_lat, des_lng);

    }

    private void initViews() {

        distanceView = findViewById(R.id.distance);
        etaView = findViewById(R.id.eta);
        callButton = findViewById(R.id.callButton);
        dummyIcon = findViewById(R.id.dummyIcon);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPhone();
            }
        });

    }

    private void callPhone() {
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                "tel", mobileNumber, null));
        startActivity(phoneIntent);
    }

    private void initSockets() {

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;
        try {
            socket = IO.socket(UrlHelper.SOCKET_URL, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();


        socket.on("GetLocation-" + provider_id, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                double src_lat = Double.parseDouble(response.optString("latitude"));
                double src_lng = Double.parseDouble(response.optString("longitude"));
                double des_lat = bookingValues.getBoookingLatitude();
                double des_lng = bookingValues.getBookingLongitude();
//                double des_lat = Double.parseDouble(getIntent().getStringExtra("des_lat"));
//                double des_lng = Double.parseDouble(getIntent().getStringExtra("des_lng"));
                origin = new LatLng(src_lat, src_lng);
                dest = new LatLng(des_lat, des_lng);
                tracking();
            }
        });
    }

    private void createBitmaps() {
        desBitmap = trackingViewModel.getBitmap((BitmapDrawable) getResources().getDrawable(R.drawable.tracking_src_marker));
        if (img.trim().length() == 0 && img != null) {
            imageLoader.loadWithBitmap(placeHolderUrl, new ImageLoader.ImageLoadedBitmap() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    srcBitmap = BitmapDescriptorFactory.fromBitmap(bitmap);
                }
            });
        } else {
            imageLoader.loadWithBitmap(img, new ImageLoader.ImageLoadedBitmap() {
                @Override
                public void onImageLoaded(Bitmap bitmap) {
                    srcBitmap = BitmapDescriptorFactory.fromBitmap(bitmap);
                }
            });
        }
    }


    private void getLocationFromServer(InputForAPI inputForAPI) {

        trackingViewModel.getLocationFromServer(inputForAPI).observe(this, new Observer<GetProviderLocationResponseModel>() {
            @Override
            public void onChanged(@Nullable GetProviderLocationResponseModel getProviderLocationResponseModel) {
                if (getProviderLocationResponseModel != null) {
                    handleSuccessResponse(getProviderLocationResponseModel);
                }
            }
        });
    }

    private void handleSuccessResponse(GetProviderLocationResponseModel response) {

        double src_lat = response.getLatitude();
        double src_lng = response.getLongitude();
        double des_lat = bookingValues.getBoookingLatitude();
        double des_lng = bookingValues.getBookingLongitude();

//        double des_lat = Double.parseDouble(getIntent().getStringExtra("des_lat"));
//        double des_lng = Double.parseDouble(getIntent().getStringExtra("des_lng"));
        origin = new LatLng(src_lat, src_lng);
        dest = new LatLng(des_lat, des_lng);
        tracking();
    }

    private void tracking() {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(origin, dest)
                .key(getResources().getString(R.string.GOOGLE_MAPS_KEY))
                .build();
        routing.execute();
    }


    private void buildDistanceInputs(double src_lat, double src_lng, double des_lat, double des_lng) {
        String urls = trackingViewModel.getCombinedUrl(src_lat, src_lng, des_lat, des_lng);
        InputForAPI inputForAPI = new InputForAPI(TrackingActivity.this);
        inputForAPI.setUrl(urls);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(new HashMap<String, String>());
        getDistanceValues(inputForAPI);

    }


    private void getDistanceValues(InputForAPI inputForAPI) {

        trackingViewModel.getDistance(inputForAPI).observe(this, new Observer<DistanceResponseModel>() {
            @Override
            public void onChanged(@Nullable DistanceResponseModel distanceResponseModel) {
                if (distanceResponseModel != null) {
                    handleDistanceInfoResponse(distanceResponseModel);
                }
            }
        });

    }

    private void handleDistanceInfoResponse(DistanceResponseModel response) {

        List<com.app.jobfizzer.Model.DistanceResponseModel.Route> array = response.getRoutes();
        com.app.jobfizzer.Model.DistanceResponseModel.Route routes = array.get(0);
        List<Leg> legs = routes.getLegs();
        Leg steps = legs.get(0);
        Distance distance = steps.getDistance();
        Duration duration = steps.getDuration();
        dist = distance.getText();
        dura = duration.getText();
        distanceView.setText(trackingViewModel.getCombinedString(dist, " ", getResources().getString(R.string.away_from)));
        etaView.setText(trackingViewModel.getCombinedString(dura, " ", getResources().getString(R.string.minutes_to_reach)));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map));
    }


    @Override
    public void onLocationChanged(Location location) {

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
        map.setMyLocationEnabled(false);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_LOCATION_PERMISSIONS) {
            if (grantResults.length > 0) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            }
        }
    }

    public void setMarker() {


        srcmarkerOptions = new MarkerOptions()
                .icon(srcBitmap)
                .anchor(0.5f, 0.5f);
        srcmarkerOptions.position(origin);

        map.addMarker(srcmarkerOptions);

        MarkerOptions desmarkerOptions = new MarkerOptions()
                .icon(desBitmap)
                .anchor(0.5f, 0.5f);

        desmarkerOptions.position(dest);
        map.addMarker(desmarkerOptions);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(desmarkerOptions.getPosition());
        builder.include(srcmarkerOptions.getPosition());
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        map.animateCamera(cu);

    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex) {
        if (polylines != null) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
        polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.black));
            polyOptions.width(10);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);
        }
        setMarker();

    }

    @Override
    public void onRoutingCancelled() {

    }

}
