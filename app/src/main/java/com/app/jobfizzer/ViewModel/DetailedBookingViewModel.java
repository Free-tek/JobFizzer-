package com.app.jobfizzer.ViewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.app.jobfizzer.Repository.DetailedBookingRepository;
import com.app.jobfizzer.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailedBookingViewModel extends AndroidViewModel {
    private DetailedBookingRepository detailedBookingRepository;

    public DetailedBookingViewModel(@NonNull Application application) {
        super(application);
    }
    @SuppressLint("SimpleDateFormat")
    public String getConvertedTime(String time) {
        String ftime = "";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date outDate = null;
        try {
            outDate = dateFormatter.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat formatter = new SimpleDateFormat("d MMM, yyyy HH:mm a");
        ftime = formatter.format(outDate);

        return ftime;
    }


    public String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        if (totalMinutes > 60) {
            minutes = minutes.length() == 1 ? "0" + minutes : minutes;
            if (minutes.equalsIgnoreCase("00")) {
                return (totalMinutes / 60) + " hrs";
            }
            return (totalMinutes / 60) + ":" + minutes + " hrs";
        } else {
            return totalMinutes + " mins";
        }
    }


    public boolean isEqual(String status, String obtainedStatus) {
        return status.equalsIgnoreCase(obtainedStatus);
    }

    public String getStaticMapUrl(String lat, String longg) {
        return  "https://maps.googleapis.com/maps/api/staticmap?key=" + getApplication().getBaseContext().getResources().getString(R.string.GOOGLE_MAPS_KEY) + "&center=" + lat + "," + longg + "&zoom=15&format=jpeg&maptype=roadmap&size=512x512&sensor=false";

    }

    public String getCombinedStrings(String s, String s1) {
        return s+ "\n" + s1;
    }
}
