package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzer.Model.AutoCompleteAddressResponseModel;
import com.app.jobfizzer.Model.GetLatLongFromIdResponseModel;
import com.app.jobfizzer.Model.GoogleLocationResponseModel.GooglePlaceResponseModel;
import com.app.jobfizzer.Repository.MapRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.R;

public class MapViewModel extends AndroidViewModel {

    MapRepository mapRepository;
    public MapViewModel(@NonNull Application application) {
        super(application);
        mapRepository=new MapRepository();
    }

    public LiveData<GooglePlaceResponseModel> getAddresDetails(InputForAPI inputs) {
        return  mapRepository.getAddresDetails(inputs);
    }

    public LiveData<AutoCompleteAddressResponseModel> autoCompleteAddress(InputForAPI inputs) {
        return  mapRepository.autoCompleteAddress(inputs);
    }

    public LiveData<GetLatLongFromIdResponseModel> getLatLagAddress(InputForAPI inputs) {
        return  mapRepository.getLatLagAddress(inputs);
    }


    public String getCombinedString(double latitude, double longitude) {

        String lat = String.valueOf(latitude);
        String lngg = String.valueOf(longitude);

        return "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + ","
                + lngg + "&sensor=true&key=" + getApplication().getBaseContext().getString(R.string.GOOGLE_MAPS_KEY);
    }
}
