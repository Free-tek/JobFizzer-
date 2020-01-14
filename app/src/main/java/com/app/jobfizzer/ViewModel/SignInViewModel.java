package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzer.Model.SignInResponseModel;
import com.app.jobfizzer.Model.SocialSignInSuccessResponse;
import com.app.jobfizzer.Repository.SignInRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

public class SignInViewModel extends AndroidViewModel {

    SignInRepository signInRepository;
    public SignInViewModel(@NonNull Application application) {
        super(application);
        signInRepository=new SignInRepository();
    }


    public LiveData<SignInResponseModel> signIn(InputForAPI inputs) {
        return  signInRepository.signIn(inputs);
    }
    public LiveData<SocialSignInSuccessResponse> socialSignIn(InputForAPI inputs) {
        return  signInRepository.socialSignIn(inputs);
    }

}
