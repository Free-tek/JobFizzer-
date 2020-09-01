package com.app.jobfizzer.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.ShowProvidersResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class ShowProvidersRepository {
    public LiveData<ShowProvidersResponseModel> getShowProviders(InputForAPI inputForAPI) {
        final MutableLiveData<ShowProvidersResponseModel> mutableLiveData=new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson=new Gson();
                ShowProvidersResponseModel showProvidersResponseModel=gson.fromJson(response.toString(),ShowProvidersResponseModel.class);
                mutableLiveData.setValue(showProvidersResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                ShowProvidersResponseModel showProvidersResponseModel=new ShowProvidersResponseModel();
                showProvidersResponseModel.setError("true");
                showProvidersResponseModel.setErrorMessage(error);
                mutableLiveData.setValue(showProvidersResponseModel);

            }
        });

        return mutableLiveData;

    }
}
