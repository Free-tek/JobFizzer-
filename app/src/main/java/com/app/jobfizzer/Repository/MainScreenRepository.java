package com.app.jobfizzer.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.AppSettingsResponseModel.AppSettingResponseModel;
import com.app.jobfizzer.Model.PaymentMethodModel;
import com.app.jobfizzer.Model.StartJobEndJobDetailsResponse.StartJobEndJobResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MainScreenRepository {

    public LiveData<AppSettingResponseModel> getAppSettings(InputForAPI inputForAPI) {
        final MutableLiveData<AppSettingResponseModel> mutableLiveData = new MutableLiveData<>();
        ApiCall.GetMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson=new Gson();
                AppSettingResponseModel appSettingResponseModel=gson.fromJson(response.toString(),
                        AppSettingResponseModel.class);

                mutableLiveData.setValue(appSettingResponseModel);

            }

            @Override
            public void setResponseError(String error) {
                AppSettingResponseModel appSettingResponseModel= new AppSettingResponseModel();
                appSettingResponseModel.setError("true");
                appSettingResponseModel.setErrorMessage(error);
                mutableLiveData.setValue(appSettingResponseModel);

            }
        });
        return mutableLiveData;
    }

    public LiveData<PaymentMethodModel> getPaymentMethods(InputForAPI inputForAPI) {
        final MutableLiveData<PaymentMethodModel> mutableLiveData = new MutableLiveData<>();
        ApiCall.GetMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                PaymentMethodModel paymentMethodModel = gson.fromJson(response.toString(), PaymentMethodModel.class);
                mutableLiveData.setValue(paymentMethodModel);
            }

            @Override
            public void setResponseError(String error) {

            }
        });

        return mutableLiveData;
    }

    public LiveData<StartJobEndJobResponseModel> getRatingDetails(InputForAPI inputForAPI) {
        final MutableLiveData<StartJobEndJobResponseModel> mutableLiveData = new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                StartJobEndJobResponseModel paymentMethodModel = gson.fromJson(response.toString(), StartJobEndJobResponseModel.class);
                mutableLiveData.setValue(paymentMethodModel);
            }

            @Override
            public void setResponseError(String error) {

            }
        });

        return mutableLiveData;
    }

}