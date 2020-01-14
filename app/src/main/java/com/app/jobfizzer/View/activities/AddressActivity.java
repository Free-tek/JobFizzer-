package com.app.jobfizzer.View.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.jobfizzer.Model.AddResponseModel.AddressResponseModel;
import com.app.jobfizzer.Model.AddResponseModel.ListAddress;
import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.Utilities.helpers.Interfaces.onDeleteClicked;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.View.adapters.EditAddressAdapter;
import com.app.jobfizzer.ViewModel.AddressViewModel;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends BaseActivity {

    public String TAG = AddressActivity.class.getSimpleName();
    public List<ListAddress> addressArray = new ArrayList<>();
    public RecyclerView address_list;
    ImageView addButton, backButton;
    AddressViewModel addressViewModel;
    CommonViewModel commonViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        initViews();
        initListners();
    }

    private void initListners() {
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ConnectionUtils.isNetworkConnected(AddressActivity.this)) {

                    moveAddressActivity();
                } else {
                    Utils.toast(findViewById(android.R.id.content), getString(R.string.no_internet_connection));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }


    private void moveAddressActivity() {
        Intent intent = new Intent(AddressActivity.this, MapActivity.class);
        intent.putExtra("from", "save");
        startActivity(intent);

        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_out_left);
    }

    private void initViews() {

        address_list = findViewById(R.id.address_list);
        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);
        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);
    }


    @Override
    protected void onResume() {
        super.onResume();
        setAddressAdapter();
    }

    public void setAddressAdapter() {

        InputForAPI inputForAPI = new InputForAPI(AddressActivity.this);
        inputForAPI.setUrl(UrlHelper.LIST_ADDRESS);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(new JSONObject());
        inputForAPI.setHeaders(ApiCall.getHeaders(AddressActivity.this));
        getValues(inputForAPI);


    }

    private void getValues(InputForAPI inputForAPI) {
        addressViewModel.getAddressList(inputForAPI).observe(this, new Observer<AddressResponseModel>() {
            @Override
            public void onChanged(@Nullable AddressResponseModel addressResponseModel) {
                if (addressResponseModel != null) {
                    setAdapterValues(addressResponseModel);
                }
            }
        });

    }

    private void setAdapterValues(AddressResponseModel response) {
        if (response.getListAddress() != null) {
            addressArray = response.getListAddress();
            LinearLayoutManager addressLayoutManager = new LinearLayoutManager(AddressActivity.this, LinearLayoutManager.VERTICAL, false);
            address_list.setLayoutManager(addressLayoutManager);
            EditAddressAdapter addressAdapter = new EditAddressAdapter(AddressActivity.this, addressArray);
            address_list.setAdapter(addressAdapter);
            addressAdapter.setOnDeleted(new onDeleteClicked() {
                @Override
                public void deleted(String id, int adapterPosition) {
                    try {
                        deleteId(id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }


    public void deleteId(String id) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        InputForAPI inputForAPI = new InputForAPI(AddressActivity.this);
        inputForAPI.setUrl(UrlHelper.DELETE_ADDRESS);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(jsonObject);
        inputForAPI.setHeaders(ApiCall.getHeaders(this));
        delete(inputForAPI);


    }

    public void delete(InputForAPI inputForAPI) {
        commonViewModel.flagUpdate(inputForAPI).observe(this, new Observer<FlagResponseModel>() {
            @Override
            public void onChanged(@Nullable FlagResponseModel addressResponseModel) {
                setAddressAdapter();
            }
        });
    }
}
