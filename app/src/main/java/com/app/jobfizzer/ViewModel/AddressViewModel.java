package com.app.jobfizzer.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.jobfizzer.Model.AddResponseModel.AddressResponseModel;
import com.app.jobfizzer.Repository.AddressRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

public class AddressViewModel extends AndroidViewModel {
    private AddressRepository addressRepository;
    public AddressViewModel(@NonNull Application application) {
        super(application);
        addressRepository=new AddressRepository();
    }

    public LiveData<AddressResponseModel> getAddressList(InputForAPI inputForAPI){
        return addressRepository.getAddressList(inputForAPI);
    }

}
