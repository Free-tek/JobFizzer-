package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzer.Model.SubCategoryResponseModel.SubCategoryResponseModel;
import com.app.jobfizzer.Repository.SubCategoryRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

public class SubCategoryViewModel extends AndroidViewModel {
    SubCategoryRepository subCategoryRepository;
    public SubCategoryViewModel(@NonNull Application application) {
        super(application);
        subCategoryRepository=new SubCategoryRepository();
    }

    public LiveData<SubCategoryResponseModel> getListSubCategories(InputForAPI inputForAPI) {
        return subCategoryRepository.getListSubCategories(inputForAPI);
    }

}