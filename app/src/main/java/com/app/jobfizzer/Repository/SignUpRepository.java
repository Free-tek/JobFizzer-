package com.app.jobfizzer.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.SignUpResponseModel;
import com.app.jobfizzer.Model.SocialSignInSuccessResponse;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SignUpRepository {



    public LiveData<SignUpResponseModel> signUp(InputForAPI inputForAPI) {
        final MutableLiveData<SignUpResponseModel> liveData = new MutableLiveData<>();


        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                SignUpResponseModel signInResponseModel= gson.fromJson(response.toString(), SignUpResponseModel.class);
                liveData.setValue(signInResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                SignUpResponseModel signInResponseModel=new SignUpResponseModel();
                signInResponseModel.setError("true");
                signInResponseModel.setErrorMessage(error);
                liveData.setValue(signInResponseModel);

            }
        });
        return liveData;
    }


    public LiveData<SocialSignInSuccessResponse> socialSignIn(InputForAPI inputForAPI) {
        final MutableLiveData<SocialSignInSuccessResponse> liveData = new MutableLiveData<>();


        ApiCall.PostMethod( inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                SocialSignInSuccessResponse signInResponseModel= gson.fromJson(response.toString(), SocialSignInSuccessResponse.class);
                liveData.setValue(signInResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                SocialSignInSuccessResponse signInResponseModel=new SocialSignInSuccessResponse();
                signInResponseModel.setError("true");
                signInResponseModel.setErrorMessage(error);
                liveData.setValue(signInResponseModel);

            }
        });
        return liveData;
    }


}
