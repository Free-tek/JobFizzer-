package com.app.jobfizzer.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

public class ProviderProfileViewModel extends AndroidViewModel {
    public ProviderProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public String getCombinedString(List<String> stringsToCombined) {

        String returnString="";
        for (int i=0;i<stringsToCombined.size();i++)
        {
            returnString=returnString+stringsToCombined.get(i);
        }

        return returnString;
    }
}
