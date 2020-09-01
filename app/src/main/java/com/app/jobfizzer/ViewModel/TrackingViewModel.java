package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.jobfizzer.Model.DistanceResponseModel.DistanceResponseModel;
import com.app.jobfizzer.Model.GetProviderLocationResponseModel;
import com.app.jobfizzer.Repository.TrackingRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.app.jobfizzer.R;

public class TrackingViewModel extends AndroidViewModel {
    TrackingRepository trackingRepository;
    public TrackingViewModel(@NonNull Application application) {
        super(application);
        trackingRepository=new TrackingRepository();
    }

    public String getCombinedUrl(double src_lat, double src_lng, double des_lat, double des_lng) {
        return "https://maps.googleapis.com/maps/api/directions/json?origin=" + src_lat + "," + src_lng + "&destination=" + des_lat + "," + des_lng + "&mode=driving&sensor=false&key=" + getApplication().getBaseContext().getResources().getString(R.string.GOOGLE_MAPS_KEY);
    }


    public LiveData<GetProviderLocationResponseModel> getLocationFromServer(InputForAPI inputForAPI) {
        return trackingRepository.getLocationFromServer(inputForAPI);
    }


    public LiveData<DistanceResponseModel> getDistance(InputForAPI inputForAPI) {
        return trackingRepository.getDistance(inputForAPI);
    }


    public String getCombinedString(String dist, String s, String string) {
        return dist+s+string;
    }

    public BitmapDescriptor getBitmap(BitmapDrawable drawable) {
        int height = 70;
        int width = 70;
        Bitmap ab = drawable.getBitmap();
        return  BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(ab, width, height, false));
    }
}
