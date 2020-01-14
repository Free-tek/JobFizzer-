package com.app.jobfizzer.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.ListAddressResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SelectTimeAndAddressRepository {
    public LiveData<ListAddressResponseModel> getAddressList(InputForAPI inputForAPI) {
        final MutableLiveData<ListAddressResponseModel> mutableLiveData = new MutableLiveData<>();
        ApiCall.GetMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ListAddressResponseModel listAddressResponseModel = gson.fromJson(response.toString(), ListAddressResponseModel.class);
                mutableLiveData.setValue(listAddressResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                ListAddressResponseModel listAddressResponseModel = new ListAddressResponseModel();
                listAddressResponseModel.setError("true");
                listAddressResponseModel.setError_message(error);
                mutableLiveData.setValue(listAddressResponseModel);
            }
        });
        return mutableLiveData;
    }
}
