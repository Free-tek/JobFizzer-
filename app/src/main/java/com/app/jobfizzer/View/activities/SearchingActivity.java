package com.app.jobfizzer.View.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.CustomLibraries.CircleImageView;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.Utilities.helpers.ripple_effect.RippleBackGround;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SearchingActivity extends BaseActivity implements View.OnClickListener {
    CircleImageView staticImage, centerImage;
    AppSettings appSettings;
    TextView searchingTextView, header;
    RippleBackGround rippleLayout;
    LinearLayout beforeSearch, afterSearch;
    String radiusValue;
    EditText radiusEditText;
    ImageView backButton;
    Button confirmBooking, cancelBooking;
    CommonViewModel commonViewModel;
    ImageLoader imageLoader = new ImageLoader(this);

    private String TAG = SearchingActivity.class.getSimpleName();
    private Socket socket;
    private TextView dateValue, serviceName, timeValue, addressText;
    private String booking_id;


    private void moveToMainActivity() {
        Intent intent = new Intent(SearchingActivity.this, MainActivity.class);

        intent.putExtra(Constants.LOGIN_TYPE, Constants.BOOKINGS_TAB);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_searching_activity);
        appSettings = new AppSettings(SearchingActivity.this);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);

        initSocket();
        initViews();
        initListeners();
        getStaticMap();

        try {
            postRequest("50000");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        confirmBooking.setOnClickListener(this);
        cancelBooking.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void initSocket() {
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true;
        try {
            socket = IO.socket(UrlHelper.SOCKET_URL, opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e("SOCKET.IO ", e.getMessage());
        }
        socket.connect();
        Log.e(TAG, "initSocket: connected");
        startListening();
    }

    private void initViews() {


        staticImage = findViewById(R.id.staticImage);
        centerImage = findViewById(R.id.centerImage);
        rippleLayout = (RippleBackGround) findViewById(R.id.rippleLayout);

        searchingTextView = findViewById(R.id.searchingTextView);
        header = findViewById(R.id.header);
        confirmBooking = findViewById(R.id.confirmBooking);
        cancelBooking = findViewById(R.id.cancelBooking);


        rippleLayout.setBackgroundColor(Utils.getPrimaryCOlor(SearchingActivity.this));

        beforeSearch = findViewById(R.id.beforeSearch);
        afterSearch = findViewById(R.id.afterSearch);
        radiusEditText = findViewById(R.id.radiusEditText);
        backButton = findViewById(R.id.backButton);
        dateValue = findViewById(R.id.dateValue);
        timeValue = findViewById(R.id.timeValue);
        addressText = findViewById(R.id.addresstText);
        serviceName = findViewById(R.id.serviceName);


        setBeforeLayout();
    }

    private void setAfterLayout() {
        rippleLayout.startRippleAnimation();
        searchingTextView.setText(getResources().getString(R.string.looking_for) + " " + appSettings.getSelectedSubCategoryName() + " ...");

        backButton.setVisibility(View.GONE);
        beforeSearch.setVisibility(View.GONE);
        afterSearch.setVisibility(View.VISIBLE);
        addressText.setText(appSettings.getSelectedAddress());
        dateValue.setText(appSettings.getSelectedDate());
        timeValue.setText(appSettings.getSelectedTimeText());
        serviceName.setText(appSettings.getSelectedSubCategoryName());
    }

    private void setBeforeLayout() {

        searchingTextView.setText("");
        backButton.setVisibility(View.VISIBLE);
        beforeSearch.setVisibility(View.GONE);
        afterSearch.setVisibility(View.GONE);
        rippleLayout.stopRippleAnimation();
    }

    private void postRequest(String radius) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subcategory_id", appSettings.getSelectedSubCategory());
        jsonObject.put("category_id", appSettings.getCategoryId());
        jsonObject.put("time_slot_id", appSettings.getSelectedTimeSlot());
        jsonObject.put("date", appSettings.getSelectedDate());
        jsonObject.put("user_id", appSettings.getUserId());
        jsonObject.put("radius", radius);
        jsonObject.put("latitude", appSettings.getSelectedlat());
        jsonObject.put("longitude", appSettings.getSelectedLong());
        jsonObject.put("address_id", appSettings.getSelectedAddressId());

        Utils.log(TAG, "postRequest: " + jsonObject);

        socket.emit("GetRandomRequest", jsonObject);

        setAfterLayout();


    }

    private void startListening() {
        socket.on("random_request_accepted-" + appSettings.getUserId(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Utils.log(TAG, "random_request_accepted: " + args[0]);
                showAcceptedDialog();
            }
        });

        socket.on("request_completed-" + appSettings.getUserId(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Utils.log(TAG, "request_completed: " + args[0]);
                JSONObject response = (JSONObject) args[0];
                showAlertDialog();
            }
        });


        socket.on("user_booking-" + appSettings.getUserId(), new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                Utils.log(TAG, "user_booking: " + args[0]);
                JSONObject response = (JSONObject) args[0];
                booking_id = response.optString("booking_id");

            }
        });
    }


    private void showAcceptedDialog() {
        SearchingActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final Dialog dialog = new Dialog(SearchingActivity.this);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.accepted_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                dialog.show();
                TextView yesButton;
                yesButton = dialog.findViewById(R.id.yesButton);
                Button noButton = dialog.findViewById(R.id.noButton);
                ImageView okay = dialog.findViewById(R.id.okay);


                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        moveToMainActivity();
                        setBeforeLayout();
                    }
                });


            }
        });

    }


    private void showAlertDialog() {
        SearchingActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setBeforeLayout();

                final Dialog dialog = new Dialog(SearchingActivity.this);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.no_providers_found);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                dialog.show();
                TextView yesButton, noButton;
                yesButton = dialog.findViewById(R.id.yesButton);
                noButton = dialog.findViewById(R.id.noButton);

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        moveToMainActivity();
                    }
                });


            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        socket.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        socket.connect();
    }

    private JSONObject getInputs() {
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }

    private void getStaticMap() {
        String urls = "https://maps.googleapis.com/maps/api/staticmap?key=" + getResources().getString(R.string.GOOGLE_MAPS_KEY) + "&center=" + appSettings.getSelectedlat() + "," + appSettings.getSelectedLong() + "&zoom=16&format=jpeg&maptype=roadmap&size=512x512&sensor=false&style=feature:administrative|element:geometry|color:0x1d1d1d|weight:1&style=feature:administrative|element:labels.text.fill|color:0x93a6b5&style=feature:landscape|color:0xeff0f5&style=feature:landscape|element:geometry|color:0xdde3e3|visibility:simplified|weight:0.5&style=feature:landscape|element:labels|color:0x1d1d1d|visibility:simplified|weight:0.5&style=feature:landscape.natural.landcover|element:geometry|color:0xfceff9&style=feature:poi|element:geometry|color:0xeeeeee&style=feature:poi|element:labels|visibility:off|weight:0.5&style=feature:poi|element:labels.text|color:0x505050|visibility:off&style=feature:poi.attraction|element:labels|visibility:off&style=feature:poi.attraction|element:labels.text|color:0xa6a6a6|visibility:off&style=feature:poi.business|element:labels|visibility:off&style=feature:poi.business|element:labels.text|color:0xa6a6a6|visibility:off&style=feature:poi.government|element:labels|visibility:off&style=feature:poi.government|element:labels.text|color:0xa6a6a6|visibility:off&style=feature:poi.medical|element:labels.text|color:0xa6a6a6|visibility:simplified&style=feature:poi.park|element:geometry|color:0xa9de82&style=feature:poi.park|element:labels.text|color:0xa6a6a6|visibility:simplified&style=feature:poi.place_of_worship|element:labels.text|color:0xa6a6a6|visibility:simplified&style=feature:poi.school|element:labels.text|color:0xa6a6a6|visibility:simplified&style=feature:poi.sports_complex|element:labels.text|color:0xa6a6a6|visibility:simplified&style=feature:road|element:geometry|color:0xffffff&style=feature:road|element:labels.text|color:0xc0c0c0|visibility:simplified|weight:0.5&style=feature:road|element:labels.text.fill|color:0x000000&style=feature:road.highway|element:geometry|color:0xf4f4f4|visibility:simplified&style=feature:road.highway|element:labels.text|color:0x1d1d1d|visibility:simplified&style=feature:road.highway.controlled_access|element:geometry|color:0xf4f4f4&style=feature:transit|element:geometry|color:0xc0c0c0&style=feature:water|element:geometry|color:0xa5c9e1";
        Utils.log(TAG, "url: " + urls);
        imageLoader.load(urls, staticImage, Utils.getMapRequest(SearchingActivity.this));
    }


    @Override
    public void onClick(View view) {
        if (view == confirmBooking) {
            radiusValue = radiusEditText.getText().toString().trim();

//            if (!radiusValue.isEmpty()) {
//                try {
//                    postRequest(radiusValue);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                Utils.toast(findViewById(android.R.id.content), getString(R.string.enter_km_to_search));
//
//            }

        } else if (view == cancelBooking) {
            try {

                buildCancelBookingInputs();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (view == backButton) {
            if (!(afterSearch.getVisibility() == View.VISIBLE)) {
                onBackPressed();
            }
        }
    }

    private void buildCancelBookingInputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", booking_id);
        InputForAPI inputForAPI = new InputForAPI(SearchingActivity.this);
        inputForAPI.setUrl(UrlHelper.CANCEL_REQUEST);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        cancelBookings(inputForAPI);
    }

    private void cancelBookings(InputForAPI inputForAPI) throws JSONException {

        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                handleCancelResponse();
            }
        });

    }

    private void handleCancelResponse() {
        setAfterLayout();
        finish();
    }
}
