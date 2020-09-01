package com.app.jobfizzer.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.jobfizzer.Model.ViewProfileResponseModel;
import com.app.jobfizzer.Repository.AccountRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

public class AccountViewModel extends AndroidViewModel {

    AccountRepository accountRepository;
    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository=new AccountRepository();
    }

    public LiveData<ViewProfileResponseModel> getViewProfile(InputForAPI inputs) {
        return  accountRepository.getViewProfile(inputs);
    }

    public void hitLogout(InputForAPI inputForAPI) {
        accountRepository.hitLogout(inputForAPI);
    }

    public String getFormattedName(String first, String last) {
        return String.format("%s %s", first, last);
    }
}
