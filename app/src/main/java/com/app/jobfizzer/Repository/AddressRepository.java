package com.app.jobfizzer.Repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.AddResponseModel.AddressResponseModel;
import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class AddressRepository {

    public LiveData<AddressResponseModel> getAddressList(InputForAPI inputForAPI) {
        final MutableLiveData<AddressResponseModel> addressResponseModelLiveData = new MutableLiveData<>();
        ApiCall.GetMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                AddressResponseModel addressResponseModel = gson.fromJson(response.toString(), AddressResponseModel.class);
                addressResponseModelLiveData.setValue(addressResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                AddressResponseModel addressResponseModel = new AddressResponseModel();
                addressResponseModel.setError("true");
                addressResponseModel.setErrorMessage(error);
                addressResponseModelLiveData.setValue(addressResponseModel);
            }
        });

        return addressResponseModelLiveData;
    }

    public LiveData<FlagResponseModel> deleteAddress(InputForAPI inputForAPI) {
        final MutableLiveData<FlagResponseModel> addressResponseModelLiveData = new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {

                Gson gson = new Gson();
                FlagResponseModel addressResponseModel = gson.fromJson(response.toString(), FlagResponseModel.class);
                addressResponseModelLiveData.setValue(addressResponseModel);

            }

            @Override
            public void setResponseError(String error) {
                FlagResponseModel addressResponseModel = new FlagResponseModel();
                addressResponseModel.setError("true");
                addressResponseModel.setError_message(error);
                addressResponseModelLiveData.setValue(addressResponseModel);

            }
        });

        return addressResponseModelLiveData;
    }

}
