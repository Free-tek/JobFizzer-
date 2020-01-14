package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Build;
import android.support.annotation.NonNull;

import com.app.jobfizzer.Model.AppSettingsResponseModel.Timeslot;
import com.app.jobfizzer.Model.DateModel;
import com.app.jobfizzer.Model.ListAddressResponseModel;
import com.app.jobfizzer.Repository.SelectTimeAndAddressRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SelectTimeAndAddressViewModel extends AndroidViewModel {
    SelectTimeAndAddressRepository bookingsRepository;

    public SelectTimeAndAddressViewModel(@NonNull Application application) {
        super(application);
        bookingsRepository = new SelectTimeAndAddressRepository();
    }

    public LiveData<ListAddressResponseModel> getAddressList(InputForAPI inputForAPI) {
        return bookingsRepository.getAddressList(inputForAPI);
    }


    public List<DateModel> getDateArray() {

        SimpleDateFormat format = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat ffformat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar date = Calendar.getInstance();
        List<DateModel> dateModels = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            DateModel dateModel = new DateModel();
            dateModel.setDate(format.format(date.getTime()));
            dateModel.setFullDate(ffformat.format(date.getTime()));
            dateModel.setDay(new SimpleDateFormat("EE", Locale.getDefault()).format(date.getTime()).toUpperCase());
            if (i == 0) {
                dateModel.setSelected("true");
            } else {
                dateModel.setSelected("false");
            }
            dateModels.add(dateModel);
            date.add(Calendar.DATE, 1);
        }
        return dateModels;
    }

    public List<Timeslot> validateTimesArray(List<Timeslot> timesArray, boolean isTodaySelected) throws ParseException {
        for (int i = timesArray.size() - 1; i >= 0; i--) {
            if (isTodaySelected) {

                Timeslot timeslot = timesArray.get(i);
                String fromTime = timeslot.getToTime();
                Date date = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).parse(fromTime);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
                Date currentdate = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).parse(currentDateandTime);
                long currenttime = currentdate.getTime();
                long serviceTime = date.getTime();
                int from = Integer.parseInt(timeslot.getFromTime().substring(0, 2));
                int to = Integer.parseInt(timeslot.getToTime().substring(0, 2));
                if (from > to) {
                    serviceTime = serviceTime + 86400000;
                }
                if (serviceTime < currenttime) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        timesArray.remove(i);
                    }
                }
            } else {
                timesArray.get(i).setSelected("false");
            }
        }
        return timesArray;
    }
}
