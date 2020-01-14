package com.app.jobfizzer.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ChangePasswordRepository {

    public LiveData<FlagResponseModel> changePassword(InputForAPI inputForAPI)
    {
        final MutableLiveData<FlagResponseModel> flagResponseModelMutableLiveData=new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                FlagResponseModel flagResponseModel= gson.fromJson(response.toString(), FlagResponseModel.class);
                flagResponseModelMutableLiveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                FlagResponseModel flagResponseModel=new FlagResponseModel();
                flagResponseModel.setError("true");
                flagResponseModel.setError_message(error);
                flagResponseModelMutableLiveData.setValue(flagResponseModel);

            }
        });

        return flagResponseModelMutableLiveData;
    }

}
