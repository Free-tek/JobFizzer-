package com.app.jobfizzer.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.jobfizzer.Model.SignUpResponseModel;
import com.app.jobfizzer.Model.SocialSignInSuccessResponse;
import com.app.jobfizzer.Repository.CommonRepository;
import com.app.jobfizzer.Repository.SignUpRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

public class SignUpViewModel extends AndroidViewModel {

    private SignUpRepository signUpRepository;
    CommonRepository commonRepository;
    public SignUpViewModel(@NonNull Application application) {
        super(application);
        signUpRepository=new SignUpRepository();

    }

    public LiveData<SignUpResponseModel> signUp(InputForAPI inputs) {
        return  signUpRepository.signUp(inputs);
    }
    public LiveData<SocialSignInSuccessResponse> socialSignIn(InputForAPI inputs) {
        return  signUpRepository.socialSignIn(inputs);
    }
}
