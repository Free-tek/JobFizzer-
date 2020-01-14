package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

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
