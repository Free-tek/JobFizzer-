package com.app.jobfizzer.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ConfirmBookingRepository {
    public LiveData<FlagResponseModel> confirmBooking(InputForAPI inputs) {
        final MutableLiveData<FlagResponseModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        ApiCall.PostMethod(inputs, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {

                Gson gson = new Gson();
                FlagResponseModel addressResponseModel = gson.fromJson(response.toString(), FlagResponseModel.class);
                flagResponseModelMutableLiveData.setValue(addressResponseModel);

            }

            @Override
            public void setResponseError(String error) {
                FlagResponseModel addressResponseModel = new FlagResponseModel();
                addressResponseModel.setError("true");
                addressResponseModel.setError_message(error);
                flagResponseModelMutableLiveData.setValue(addressResponseModel);

            }
        });

        return flagResponseModelMutableLiveData;

    }
}