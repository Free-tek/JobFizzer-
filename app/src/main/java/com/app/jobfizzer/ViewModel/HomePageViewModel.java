package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.app.jobfizzer.Model.CategoryResponseModel.CategoryResponse;
import com.app.jobfizzer.Model.CategoryResponseModel.List_category;
import com.app.jobfizzer.Model.FavouriteBannerImages;
import com.app.jobfizzer.Model.SubCategoryResponseModel.List_subcategory;
import com.app.jobfizzer.Repository.HomePageRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

import java.util.List;

public class HomePageViewModel extends AndroidViewModel {
    HomePageRepository homePageRepository;

    public HomePageViewModel(@NonNull Application application) {
        super(application);
        homePageRepository = new HomePageRepository();
    }


    public LiveData<CategoryResponse> getListCategories(InputForAPI inputForAPI) {
        return homePageRepository.getListCategories(inputForAPI);
    }

    public MutableLiveData<List<FavouriteBannerImages>> getBanner() {
        return homePageRepository.getBanner(getApplication().getBaseContext());
    }


    public Boolean getLikedStatus(String id, boolean categoryStatus) {
        return homePageRepository.getLikedStatus(getApplication().getBaseContext(), id, categoryStatus);
    }


    public LiveData<Boolean> setLiked(List_category listCategory) {
        return homePageRepository.setLiked(getApplication().getBaseContext(), listCategory);
    }

    public LiveData<Boolean> setSubLiked(List_subcategory listCategory) {
        return homePageRepository.setSubLiked(getApplication().getBaseContext(), listCategory);
    }

    public LiveData<Boolean> deleteFav(String id, boolean categoryStatus) {
        return homePageRepository.deleteFav(getApplication().getBaseContext(), id,categoryStatus);
    }
}
