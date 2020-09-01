package com.app.jobfizzer.Repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.DistanceResponseModel.DistanceResponseModel;
import com.app.jobfizzer.Model.GetProviderLocationResponseModel;
import com.app.jobfizzer.Model.SubCategoryResponseModel.SubCategoryResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class TrackingRepository {


    public LiveData<SubCategoryResponseModel> getListSubCategories(InputForAPI inputForAPI) {
        final MutableLiveData<SubCategoryResponseModel> subCategoryResponseModelMutableLiveData = new MutableLiveData<>();

        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                SubCategoryResponseModel subCategoryResponseModel = gson.fromJson(response.toString(), SubCategoryResponseModel.class);
                subCategoryResponseModelMutableLiveData.setValue(subCategoryResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                SubCategoryResponseModel addressResponseModel = new SubCategoryResponseModel();
                addressResponseModel.setError("true");
                addressResponseModel.setError_message(error);
                subCategoryResponseModelMutableLiveData.setValue(addressResponseModel);
            }
        });

        return subCategoryResponseModelMutableLiveData;
    }

    public LiveData<GetProviderLocationResponseModel> getLocationFromServer(InputForAPI inputForAPI) {
        final MutableLiveData<GetProviderLocationResponseModel> subCategoryResponseModelMutableLiveData = new MutableLiveData<>();

        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                GetProviderLocationResponseModel subCategoryResponseModel = gson.fromJson(response.toString(), GetProviderLocationResponseModel.class);
                subCategoryResponseModelMutableLiveData.setValue(subCategoryResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                GetProviderLocationResponseModel addressResponseModel = new GetProviderLocationResponseModel();
                addressResponseModel.setError("true");
                addressResponseModel.setErrorMessage(error);
                subCategoryResponseModelMutableLiveData.setValue(addressResponseModel);
            }
        });

        return subCategoryResponseModelMutableLiveData;
    }

    public LiveData<DistanceResponseModel> getDistance(InputForAPI inputForAPI) {

        final MutableLiveData<DistanceResponseModel> subCategoryResponseModelMutableLiveData = new MutableLiveData<>();

        ApiCall.PostMethod( inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                DistanceResponseModel subCategoryResponseModel = gson.fromJson(response.toString(), DistanceResponseModel.class);
                subCategoryResponseModelMutableLiveData.setValue(subCategoryResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                DistanceResponseModel addressResponseModel = new DistanceResponseModel();
                addressResponseModel.setStatus("false");
                subCategoryResponseModelMutableLiveData.setValue(addressResponseModel);
            }
        });

        return subCategoryResponseModelMutableLiveData;
    }
}
