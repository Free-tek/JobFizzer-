package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;

import com.app.jobfizzer.Model.AppSettingsResponseModel.AppSettingResponseModel;
import com.app.jobfizzer.Model.AppSettingsResponseModel.Timeslot;
import com.app.jobfizzer.Model.PaymentMethodModel;
import com.app.jobfizzer.Model.StartJobEndJobDetailsResponse.StartJobEndJobResponseModel;
import com.app.jobfizzer.Repository.MainScreenRepository;
import com.app.jobfizzer.Utilities.Animationhelper;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

import java.util.List;

public class MainScreenViewModel extends AndroidViewModel {

    MainScreenRepository mainRepository;

    public MainScreenViewModel(@NonNull Application application) {
        super(application);
        mainRepository = new MainScreenRepository();
    }

    public String formatHoursAndMinutes(int totalMinutes) {
        String minutes = Integer.toString(totalMinutes % 60);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        if (minutes.equalsIgnoreCase("00")) {
            return (totalMinutes / 60) + " hrs";
        }
        return (totalMinutes / 60) + ":" + minutes + " hrs";
    }


    public LiveData<AppSettingResponseModel> getAppSettings(InputForAPI inputs) {
        return mainRepository.getAppSettings(inputs);
    }

    public LiveData<PaymentMethodModel> getPaymentMethods(InputForAPI inputs) {
        return mainRepository.getPaymentMethods(inputs);
    }

    public LiveData<StartJobEndJobResponseModel> getRatingDetails(InputForAPI inputs) {
        return mainRepository.getRatingDetails(inputs);
    }


    public void animateUp(LinearLayout commentSection, LinearLayout topLayout) {
        Animationhelper animationhelper = new Animationhelper();
        animationhelper.animateUp(commentSection, topLayout, getApplication().getBaseContext());
    }

    public List<Timeslot> getFormattedTimeslots(List<Timeslot> timeslots) {
        for (int i = 0; i < timeslots.size(); i++) {
            timeslots.get(i).setSelected("false");
        }
        return timeslots;
    }
}
