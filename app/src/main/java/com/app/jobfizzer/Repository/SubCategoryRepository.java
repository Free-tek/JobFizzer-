package com.app.jobfizzer.Repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.SubCategoryResponseModel.SubCategoryResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SubCategoryRepository {


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
}
