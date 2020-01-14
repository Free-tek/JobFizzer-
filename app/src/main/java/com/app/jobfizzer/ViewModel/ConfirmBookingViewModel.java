package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.app.jobfizzer.Repository.ConfirmBookingRepository;
import com.app.jobfizzer.R;

public class ConfirmBookingViewModel extends AndroidViewModel {
    ConfirmBookingRepository confirmBookingRepository;
    Application application;
    public ConfirmBookingViewModel(@NonNull Application application) {
        super(application);
        confirmBookingRepository=new ConfirmBookingRepository();
        this.application=application;
    }

    public String getMapUrl(String lat, String longg) {

        return "https://maps.googleapis.com/maps/api/staticmap?key=" + application.getBaseContext().getResources().getString(R.string.GOOGLE_MAPS_KEY) + "&center=" + lat + "," + longg + "&zoom=15&format=jpeg&maptype=roadmap&size=512x512&sensor=false";

    }
}
