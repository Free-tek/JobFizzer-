package com.app.jobfizzer.View.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.ImageLoader;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Constants.Constants;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.ViewModel.ConfirmBookingViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmBookingActivity extends BaseActivity {
    TextView providerName;
    TextView addressText, addressTitle;
    ShowProvidersResponseModel.ProviderService.AllProvider providerDetails;
    Button confirmBooking;
    CommonViewModel commonViewModel;
    TextView pricing, dateValue, timeValue, pricing2;
    String selected_id;
    ImageView backButton;
    ImageView mapData;
    TextView serviceName;
    ImageView providerPic;
    TextView phoneNumber;
    ImageLoader imageLoader;
    AppSettings appSettings = new AppSettings(ConfirmBookingActivity.this);
    ConfirmBookingViewModel confirmBookingViewModel;

    private String TAG = ConfirmBookingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        confirmBookingViewModel = ViewModelProviders.of(this).get(ConfirmBookingViewModel.class);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        imageLoader = new ImageLoader(this);
        initViews();
        initListners();
    }

    private void initListners() {
        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    buildInputs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void buildInputs() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("service_sub_category_id", appSettings.getSelectedSubCategory());
        jsonObject.put("time_slot_id", appSettings.getSelectedTimeSlot());
        jsonObject.put("date", appSettings.getSelectedDate());
        jsonObject.put("provider_id", selected_id);
        jsonObject.put("address_id", appSettings.getSelectedAddressId());

        InputForAPI inputForAPI = new InputForAPI(ConfirmBookingActivity.this);
        inputForAPI.setUrl(UrlHelper.NEW_BOOKINGS);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        confirmBook(inputForAPI);

    }

    private void confirmBook(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel flagResponseModel) {
                showSuccessDialog();
            }
        });

    }


    private void showSuccessDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ConfirmBookingActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_booking_confirmed, null);
        dialogBuilder.setView(dialogView);

        Button okay = dialogView.findViewById(R.id.okay);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                moveToMainActivity();
            }
        });

    }

    private void moveToMainActivity() {
        Intent intent = new Intent(ConfirmBookingActivity.this, MainActivity.class);
        intent.putExtra(Constants.LOGIN_TYPE, Constants.BOOKINGS_TAB);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private void initViews() {

        providerDetails = (ShowProvidersResponseModel.ProviderService.AllProvider) getIntent().getSerializableExtra("providerDetails");
        providerName = findViewById(R.id.providerName);
        dateValue = findViewById(R.id.dateValue);
        timeValue = findViewById(R.id.timeValue);
        pricing = findViewById(R.id.pricing);
        pricing2 = findViewById(R.id.pricing2);
        addressText = findViewById(R.id.addresstText);
        addressTitle = findViewById(R.id.addressTitle);
        serviceName = findViewById(R.id.serviceName);
        confirmBooking = findViewById(R.id.confirmBooking);
        providerPic = findViewById(R.id.providerPic);
        mapData = findViewById(R.id.mapData);
        backButton = findViewById(R.id.backButton);
        phoneNumber = findViewById(R.id.phoneNumber);
        setValues();

    }

    private void setValues() {

        providerName.setText(providerDetails.getName());
        phoneNumber.setText("" + providerDetails.getMobile());
        pricing.setText(commonViewModel.getCombinedStrings(getResources().getString(R.string.currency_symbol) + providerDetails.getPriceperhour()));
        dateValue.setText(appSettings.getSelectedDate());
        timeValue.setText(appSettings.getSelectedTimeText());
        selected_id = "" + providerDetails.getId();
        serviceName.setText(appSettings.getSelectedSubCategoryName());
        addressTitle.setText(new StringBuilder().append(getString(R.string.address)).append(": ").append("(").append(appSettings.getSelectedAddressTitle()).append(")"));
        imageLoader.load(providerDetails.getImage(), providerPic, Utils.getProfilePicture(ConfirmBookingActivity.this));
        getStaticMap(appSettings.getSelectedlat(), appSettings.getSelectedLong());
        confirmBookingViewModel = ViewModelProviders.of(this).get(ConfirmBookingViewModel.class);

    }

    private void getStaticMap(String lat, String longg) {
        String urls = confirmBookingViewModel.getMapUrl(lat, longg);

        imageLoader.loadWithCallBack(urls, mapData, Utils.getMapRequest(ConfirmBookingActivity.this), new ImageLoader.ImageLoaded() {
            @Override
            public void onImageLoaded() {
                addressText.setText(appSettings.getSelectedAddress());
            }
        });
    }


}
