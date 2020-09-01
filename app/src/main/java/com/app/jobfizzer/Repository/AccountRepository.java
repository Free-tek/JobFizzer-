package com.app.jobfizzer.Repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.ViewProfileResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class AccountRepository {

    public LiveData<ViewProfileResponseModel> getViewProfile(InputForAPI inputForAPI) {
        final MutableLiveData<ViewProfileResponseModel> mutableLiveData = new MutableLiveData<>();
        ApiCall.GetMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson=new Gson();
                ViewProfileResponseModel viewProfileResponseModel=gson.fromJson(response.toString(),ViewProfileResponseModel.class);
                mutableLiveData.setValue(viewProfileResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                ViewProfileResponseModel viewProfileResponseModel= new ViewProfileResponseModel();
                viewProfileResponseModel.setError("true");
                viewProfileResponseModel.setErrorMessage(error);
                mutableLiveData.setValue(viewProfileResponseModel);

            }
        });
        return mutableLiveData;
    }

    public void hitLogout(InputForAPI inputForAPI) {

        ApiCall.GetMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {

            }

            @Override
            public void setResponseError(String error) {


            }
        });

    }
}
