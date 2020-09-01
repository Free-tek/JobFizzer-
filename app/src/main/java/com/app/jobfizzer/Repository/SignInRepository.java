package com.app.jobfizzer.Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.SignInResponseModel;
import com.app.jobfizzer.Model.SocialSignInSuccessResponse;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class SignInRepository {

    public LiveData<SignInResponseModel> signIn(InputForAPI inputForAPI) {
        final MutableLiveData<SignInResponseModel> liveData = new MutableLiveData<>();

        ApiCall.PostMethod( inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                SignInResponseModel signInResponseModel= gson.fromJson(response.toString(), SignInResponseModel.class);
                liveData.setValue(signInResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                SignInResponseModel signInResponseModel=new SignInResponseModel();
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
