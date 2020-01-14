package com.app.jobfizzer.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.app.jobfizzer.Model.AllPageResponseModel;
import com.app.jobfizzer.Model.CommonResponseModel;
import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Model.ForgotPasswordResponseModel;
import com.app.jobfizzer.Model.ImageUploadSuccessResponse;
import com.app.jobfizzer.Model.PayStachAccessURLResponse;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class CommonRepository {


    public LiveData<ImageUploadSuccessResponse> uploadImage(InputForAPI inputForAPI) {

        final MutableLiveData<ImageUploadSuccessResponse> liveData = new MutableLiveData<>();

        ApiCall.uploadImage(inputForAPI.getContext(), inputForAPI.getFile(), inputForAPI.getUrl(), new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ImageUploadSuccessResponse imageUploadSuccessResponse = gson.fromJson(response.toString(), ImageUploadSuccessResponse.class);
                Log.d("image_opt", response.optString("image"));
                liveData.postValue(imageUploadSuccessResponse);
            }

            @Override
            public void setResponseError(String error) {

                ImageUploadSuccessResponse imageUploadSuccessResponse = new ImageUploadSuccessResponse();
                imageUploadSuccessResponse.setError("true");
                imageUploadSuccessResponse.setErrorMessage(error);
                liveData.postValue(imageUploadSuccessResponse);


            }
        });
        return liveData;

    }


    public LiveData<AllPageResponseModel> getAllPageResponse(InputForAPI inputForAPI) {
        final MutableLiveData<AllPageResponseModel> allPageResponseModelMutableLiveData = new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                AllPageResponseModel allPageResponseModel = gson.fromJson(response.toString(), AllPageResponseModel.class);
                allPageResponseModelMutableLiveData.setValue(allPageResponseModel);

            }

            @Override
            public void setResponseError(String error) {
                AllPageResponseModel allPageResponseModel = new AllPageResponseModel();
                allPageResponseModel.setError("true");
                allPageResponseModel.setError_message(error);
                allPageResponseModelMutableLiveData.setValue(allPageResponseModel);
                allPageResponseModelMutableLiveData.setValue(allPageResponseModel);

            }
        });
        return allPageResponseModelMutableLiveData;
    }

    public LiveData<FlagResponseModel> flagUpdate(InputForAPI inputForAPI) {

        final MutableLiveData<FlagResponseModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                FlagResponseModel flagResponseModel = gson.fromJson(response.toString(), FlagResponseModel.class);
//                FlagResponseModel flagResponseModel = new FlagResponseModel();
//                flagResponseModel.setError("true");
                flagResponseModelMutableLiveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                FlagResponseModel flagResponseModel = new FlagResponseModel();
                flagResponseModel.setError("true");
                flagResponseModel.setError_message(error);
                flagResponseModelMutableLiveData.setValue(flagResponseModel);
            }
        });
        return flagResponseModelMutableLiveData;
    }

    public LiveData<ForgotPasswordResponseModel> requestOtp(InputForAPI inputForAPI) {

        final MutableLiveData<ForgotPasswordResponseModel> flagResponseModelMutableLiveData = new MutableLiveData<>();

        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                ForgotPasswordResponseModel forgotPasswordResponseModel = gson.fromJson(response.toString(), ForgotPasswordResponseModel.class);
                flagResponseModelMutableLiveData.setValue(forgotPasswordResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                ForgotPasswordResponseModel forgotPasswordResponseModel = new ForgotPasswordResponseModel();
                forgotPasswordResponseModel.setError("true");
                forgotPasswordResponseModel.setErrorMessage(error);
                flagResponseModelMutableLiveData.setValue(forgotPasswordResponseModel);
            }
        });
        return flagResponseModelMutableLiveData;
    }

    public MutableLiveData<PayStachAccessURLResponse> makePayment(InputForAPI inputForAPI) {

        final MutableLiveData<PayStachAccessURLResponse> flagResponseModelMutableLiveData = new MutableLiveData<>();


        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                PayStachAccessURLResponse forgotPasswordResponseModel = gson.fromJson(response.toString(), PayStachAccessURLResponse.class);
                flagResponseModelMutableLiveData.setValue(forgotPasswordResponseModel);

            }

            @Override
            public void setResponseError(String error) {

                PayStachAccessURLResponse forgotPasswordResponseModel = new PayStachAccessURLResponse();
                forgotPasswordResponseModel.setError("true");
                forgotPasswordResponseModel.setError_data(error);
                flagResponseModelMutableLiveData.setValue(forgotPasswordResponseModel);

            }
        });

        return flagResponseModelMutableLiveData;
    }

    public MutableLiveData<CommonResponseModel> getPaymentStatus(InputForAPI book_id) {

        final MutableLiveData<CommonResponseModel> flagResponseModelMutableLiveData = new MutableLiveData<>();


        ApiCall.PostMethod(book_id, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                CommonResponseModel forgotPasswordResponseModel = gson.fromJson(response.toString(), CommonResponseModel.class);
                flagResponseModelMutableLiveData.setValue(forgotPasswordResponseModel);
            }

            @Override
            public void setResponseError(String error) {
                CommonResponseModel forgotPasswordResponseModel = new CommonResponseModel();
                forgotPasswordResponseModel.setError("true");
                forgotPasswordResponseModel.setError_message(error);
                flagResponseModelMutableLiveData.setValue(forgotPasswordResponseModel);
            }
        });

        return flagResponseModelMutableLiveData;
    }
}
