package com.app.jobfizzer.View.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jobfizzer.Model.AutoCompleteAddressResponseModel;
import com.app.jobfizzer.Model.GetLatLongFromIdResponseModel;
import com.app.jobfizzer.Model.Prediction;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onClickListener;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.adapters.AutoCompleteAdapter;
import com.app.jobfizzer.ViewModel.MapViewModel;
import com.app.jobfizzer.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChooseLocationActivity extends AppCompatActivity {

    EditText searchBox;
    RecyclerView autoCompleteList;
    AutoCompleteAdapter autoCompleteAdapter;
    ProgressBar progressBarOTP;
    ImageView backButton;

    MapViewModel mapViewModel;
    RelativeLayout search_map;

    String selectedLat, selectedLong;
    String selectedAddressName, selectedAddressText;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        initView();
        initListeners();

    }

    private void initView() {
        searchBox = findViewById(R.id.searchBox);
        autoCompleteList = findViewById(R.id.autoCompleteList);
        progressBarOTP = findViewById(R.id.progressBarOTP);
        backButton = findViewById(R.id.backButton);
        search_map = findViewById(R.id.search_map);

    }

    private void searchPlaces() {
        String url = getPlaceAutoCompleteUrl(searchBox.getText().toString());
        InputForAPI inputForAPI = new InputForAPI(ChooseLocationActivity.this);
        inputForAPI.setUrl(url);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(new HashMap<String, String>());
        getSearchDetails(inputForAPI);
    }

    private void getSearchDetails(InputForAPI inputForAPI) {
        mapViewModel.autoCompleteAddress(inputForAPI).observe(
                this, new Observer<AutoCompleteAddressResponseModel>() {
                    @Override
                    public void onChanged(@Nullable AutoCompleteAddressResponseModel autoCompleteAddressResponseModel) {
                        if (autoCompleteAddressResponseModel != null) {

                            setAdapters(autoCompleteAddressResponseModel.getPredictions());
                        }
                    }

                }
        );
    }


    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlString.append("&radius=500&language=en");
        urlString.append("&key=").append(getApplication().getBaseContext().getString(R.string.GOOGLE_MAPS_KEY));
        Log.d("FINAL URL:::   ", urlString.toString());
        return urlString.toString();
    }

    public String getLatLngByPlaceID(String placeID) {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key=" + getApplication().getBaseContext().getString(R.string.GOOGLE_MAPS_KEY);

    }

    private void setAdapters(final List<Prediction> predictions) {
        autoCompleteList.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChooseLocationActivity.this, LinearLayoutManager.VERTICAL, false);
        autoCompleteList.setLayoutManager(linearLayoutManager);
        autoCompleteAdapter = new AutoCompleteAdapter(ChooseLocationActivity.this, predictions);
        autoCompleteList.setAdapter(autoCompleteAdapter);
        autoCompleteAdapter.notifyDataSetChanged();
        autoCompleteAdapter.setOnClickListner(new onClickListener() {
            @Override
            public void onClicked(final int position) {
                String url = getLatLngByPlaceID(predictions.get(position).getPlaceId());

                InputForAPI inputForAPI = new InputForAPI(ChooseLocationActivity.this);
                inputForAPI.setUrl(url);
                inputForAPI.setFile(null);
                inputForAPI.setHeaders(new HashMap<String, String>());
                getLatLagDetails(inputForAPI, position, predictions);
            }
        });
    }

    private void getLatLagDetails(InputForAPI inputForAPI, final int position, final List<Prediction> predictions) {
        mapViewModel.getLatLagAddress(inputForAPI).observe(
                this, new Observer<GetLatLongFromIdResponseModel>() {
                    @Override
                    public void onChanged(@Nullable GetLatLongFromIdResponseModel getLatLongFromIdResponseModel) {
                        if (getLatLongFromIdResponseModel != null) {

                            searchBox.setText("");
                            String areaname = predictions.get(position).getDescription();
                            String desc_detail = predictions.get(position).getTerms().get(0).getValue();
                            selectedLat = String.valueOf(getLatLongFromIdResponseModel.getResult().getGeometry().getLocation().getLat());
                            selectedLong = String.valueOf(getLatLongFromIdResponseModel.getResult().getGeometry().getLocation().getLng());
                            selectedAddressName = areaname;
                            selectedAddressText = desc_detail;
                            finishIntent(selectedLat, selectedLong, selectedAddressName);
                        }

                    }

                }
        );

    }


    private void finishIntent(String selectedLat, String selectedLong, String selectedAddressName) {

        Intent intent = new Intent();
        intent.putExtra("latitude", selectedLat);
        intent.putExtra("longitude", selectedLong);
        intent.putExtra("areaname", selectedAddressName);
        setResult(100, intent);
        finish();
    }

    private void initListeners() {
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (ConnectionUtils.isNetworkConnected(ChooseLocationActivity.this)) {
                    searchPlaces();
                } else {
                    Utils.toast(findViewById(android.R.id.content), getResources().getString(R.string.no_internet_connection));
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (searchBox.getText().toString().length() > 3) {

                }


            }
        });
        search_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionUtils.isNetworkConnected(ChooseLocationActivity.this)) {
                    searchPlaces();
                } else {
                    Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
                }
            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchPlaces();
                    if (searchBox.getText().toString().isEmpty()) {
                        Utils.toast(findViewById(android.R.id.content), getResources().getString(R.string.address_not_found));
                    }
                    return true;
                }
                return false;
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    public void onStart() {
        super.onStart();

    }
}
