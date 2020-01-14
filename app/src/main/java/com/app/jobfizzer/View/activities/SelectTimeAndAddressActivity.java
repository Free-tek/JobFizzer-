package com.app.jobfizzer.View.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jobfizzer.Model.AppSettingsResponseModel.Timeslot;
import com.app.jobfizzer.Model.DateModel;
import com.app.jobfizzer.Model.ListAddressResponseModel;
import com.app.jobfizzer.Model.List_address;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.Events.onSelected;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.adapters.AddressAdapter;
import com.app.jobfizzer.View.adapters.DatesAdapter;
import com.app.jobfizzer.View.adapters.TimesAdapter;
import com.app.jobfizzer.ViewModel.SelectTimeAndAddressViewModel;
import com.app.jobfizzer.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SelectTimeAndAddressActivity extends BaseActivity implements View.OnClickListener {
    public boolean isTodaySelected = true;
    AppSettings appSettings;
    SelectTimeAndAddressViewModel addressViewModel;
    private RecyclerView datesRecyclerView;
    private RecyclerView addressRecyclerView;
    private RecyclerView timesRecyclerView;
    private Button next, localProvider;
    private TextView emptyLayout;
    private ImageView backButton;
    private List<List_address> finaladdressArray = new ArrayList<>();
    private List<Timeslot> timesArray = new ArrayList<>();
    private String TAG = SelectTimeAndAddressActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time_and_address);
        appSettings = new AppSettings(SelectTimeAndAddressActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        appSettings.setAddressSelected(false);
        appSettings.setIsDateSeleceted(false);
        appSettings.setTimeSelected(false);
        isTodaySelected = true;
        try {
            initViews();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedEvent(onSelected event) throws ParseException {
        validateTimeSlot();
    }

    private void showValueLayout() {
        emptyLayout.setVisibility(View.INVISIBLE);
        timesRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
        timesRecyclerView.setVisibility(View.INVISIBLE);
    }


    private void initListners() {
        next.setOnClickListener(this);
        localProvider.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    private void validateTimeSlot() throws ParseException {
        timesArray = addressViewModel.validateTimesArray(timesArray, isTodaySelected);
        setTimeSlotAdapter();
    }

    private void setTimeSlotAdapter() {
        GridLayoutManager timeLayoutManager = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        timesRecyclerView.setLayoutManager(timeLayoutManager);
        TimesAdapter timesAdapter = new TimesAdapter(this, timesArray);
        timesRecyclerView.setAdapter(timesAdapter);
        if (timesArray.size() == 0) {
            showEmptyLayout();
        } else {
            showValueLayout();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void moveShowProviderList() {
        Intent providerProfile = new Intent(SelectTimeAndAddressActivity.this, ShowProvidersActivity.class);
        startActivity(providerProfile);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private void setDatesAdapter() {
        List<DateModel> dateModels = addressViewModel.getDateArray();
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        datesRecyclerView.setLayoutManager(manager);
        DatesAdapter datesAdapter = new DatesAdapter(SelectTimeAndAddressActivity.this, dateModels);
        datesRecyclerView.setAdapter(datesAdapter);

    }

    private void initViews() throws ParseException {

        datesRecyclerView = findViewById(R.id.datesRecyclerView);
        addressRecyclerView = findViewById(R.id.addressRecyclerView);
        backButton = findViewById(R.id.backButton);
        emptyLayout = findViewById(R.id.emptyLayout);
        next = findViewById(R.id.next);
        localProvider = findViewById(R.id.localProvider);
        timesRecyclerView = findViewById(R.id.timeRecyclerView);
        RelativeLayout guest_login = (RelativeLayout) findViewById(R.id.guest_login);
        AppSettings appSettings = new AppSettings(SelectTimeAndAddressActivity.this);
        timesArray = appSettings.getTimeSlots();
        addressViewModel = ViewModelProviders.of(this).get(SelectTimeAndAddressViewModel.class);

        initListners();

        if (appSettings.getUserType().equalsIgnoreCase("guest")) {
            guest_login.setVisibility(View.VISIBLE);
        } else {
            guest_login.setVisibility(View.GONE);
            setDatesAdapter();
            validateTimeSlot();
            addressInput();
        }
    }

    private void addressInput() {
        InputForAPI inputForAPI = new InputForAPI(SelectTimeAndAddressActivity.this);
        inputForAPI.setUrl(UrlHelper.LIST_ADDRESS);
        inputForAPI.setFile(null);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        getAddressValues(inputForAPI);
    }

    private void getAddressValues(InputForAPI inputForAPI) {
        addressViewModel.getAddressList(inputForAPI).observe(this, new Observer<ListAddressResponseModel>() {
            @Override
            public void onChanged(@Nullable ListAddressResponseModel listAddressResponseModel) {
                if (listAddressResponseModel != null) {
                    handleSuccessResponse(listAddressResponseModel);
                }
            }
        });

    }

    private void handleSuccessResponse(ListAddressResponseModel response) {
        List<List_address> addressArray = response.getList_address();
        finaladdressArray = new ArrayList<>();
        for (int i = 0; i < addressArray.size(); i++) {
            List_address jsonObject = addressArray.get(i);
            jsonObject.setSelected("false");
            jsonObject.setType("");
            finaladdressArray.add(i, jsonObject);
        }
        List_address list_address = new List_address();
        list_address.setType("addaddress");
        list_address.setSelected("false");
        finaladdressArray.add(list_address);
        setAddressAdapter();

    }

    private void setAddressAdapter() {
        GridLayoutManager addressLayoutManager = new GridLayoutManager(SelectTimeAndAddressActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        addressRecyclerView.setLayoutManager(addressLayoutManager);
        AddressAdapter addressAdapter = new AddressAdapter(SelectTimeAndAddressActivity.this, finaladdressArray);
        addressRecyclerView.setAdapter(addressAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appSettings.setAddressSelected(false);
        appSettings.setIsDateSeleceted(false);
        appSettings.setTimeSelected(false);
        finish();
    }




    @Override
    public void onClick(View view) {
        if (view == next) {
            if (appSettings.isAddressSelected()) {
                if (appSettings.getIsDateSeleceted()) {
                    if (appSettings.isTimeSelected()) {
                        moveShowProviderList();
                    } else {
                        toast(getString(R.string.please_select_time));
                    }
                } else {
                    toast(getString(R.string.please_select_date));
                }
            } else {
                toast(getString(R.string.please_select_address));
            }

        } else if (view == backButton) {
            Utils.log(TAG, "back");
            onBackPressed();
        } else if (view == localProvider) {
            if (appSettings.isAddressSelected()) {
                if (appSettings.getIsDateSeleceted()) {
                    if (appSettings.isTimeSelected()) {
                        moveToSerching();
                    } else {
                        toast(getString(R.string.please_select_time));
                    }
                } else {
                    toast(getString(R.string.please_select_date));
                }
            } else {
                toast(getString(R.string.please_select_address));

            }
        }

    }

    private void moveToSerching() {
        Intent providerProfile = new Intent(SelectTimeAndAddressActivity.this, SearchingActivity.class);
        startActivity(providerProfile);
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }


    public void toast(String message) {
        Utils.toast(findViewById(android.R.id.content), message);

    }

}
