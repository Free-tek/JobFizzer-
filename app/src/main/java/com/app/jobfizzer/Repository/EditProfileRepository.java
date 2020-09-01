package com.app.jobfizzer.Repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

import org.json.JSONObject;

public class EditProfileRepository {

    public LiveData<FlagResponseModel> updateProfile(InputForAPI inputForAPI) {
        final MutableLiveData<FlagResponseModel> allPageResponseModelMutableLiveData = new MutableLiveData<>();
        ApiCall.GetMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                FlagResponseModel flagResponseModel= new FlagResponseModel();
                flagResponseModel.setError("false");
                allPageResponseModelMutableLiveData.setValue(flagResponseModel);

            }

            @Override
            public void setResponseError(String error) {
                FlagResponseModel flagResponseModel= new FlagResponseModel();
                flagResponseModel.setError("true");
                flagResponseModel.setError_message(error);
                allPageResponseModelMutableLiveData.setValue(flagResponseModel);

            }
        });
        return allPageResponseModelMutableLiveData;
    }

}
