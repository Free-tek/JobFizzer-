package com.app.jobfizzer.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.app.jobfizzer.Model.CategoryResponseModel.Banner_images;
import com.app.jobfizzer.Model.CategoryResponseModel.CategoryResponse;
import com.app.jobfizzer.Model.CategoryResponseModel.List_category;
import com.app.jobfizzer.Model.FavouriteBannerImages;
import com.app.jobfizzer.Model.SubCategoryResponseModel.List_subcategory;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.DatabaseHandler;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class HomePageRepository {


    public LiveData<CategoryResponse> getListCategories(InputForAPI inputs) {
        final MutableLiveData<CategoryResponse> categoryResponseMutableLiveData = new MutableLiveData<>();

        ApiCall.GetMethod(inputs, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {

                Gson gson = new Gson();
                CategoryResponse addressResponseModel = gson.fromJson(response.toString(), CategoryResponse.class);
                categoryResponseMutableLiveData.setValue(addressResponseModel);

            }

            @Override
            public void setResponseError(String error) {
                CategoryResponse addressResponseModel = new CategoryResponse();
                addressResponseModel.setError("true");
                addressResponseModel.setError_message(error);
                categoryResponseMutableLiveData.setValue(addressResponseModel);

            }
        });

        return categoryResponseMutableLiveData;

    }


    public LiveData<Banner_images> getBannerImages(InputForAPI inputs) {
        final MutableLiveData<Banner_images> categoryResponseMutableLiveData = new MutableLiveData<>();

        ApiCall.PostMethod(inputs, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {

                Gson gson = new Gson();
                Banner_images addressResponseModel = gson.fromJson(response.toString(), Banner_images.class);
                categoryResponseMutableLiveData.setValue(addressResponseModel);

            }

            @Override
            public void setResponseError(String error) {
                Banner_images addressResponseModel = new Banner_images();
                categoryResponseMutableLiveData.setValue(addressResponseModel);

            }
        });

        return categoryResponseMutableLiveData;

    }


    public MutableLiveData<List<FavouriteBannerImages>> getBanner(Context baseContext) {
        final MutableLiveData<List<FavouriteBannerImages>> faveList = new MutableLiveData<>();

        DatabaseHandler db = new DatabaseHandler(baseContext);
        faveList.setValue(db.getFavList());
        db.close();

        return faveList;
    }


    public boolean getLikedStatus(Context baseContext, String id, boolean categoryStatus) {
        DatabaseHandler db = new DatabaseHandler(baseContext);
        return db.isLikedCategory(id, categoryStatus);
    }

    public LiveData<Boolean> setLiked(Context baseContext, List_category listCategory) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        DatabaseHandler db = new DatabaseHandler(baseContext);

        long result = db.insertData(true, "" + listCategory.getId(), listCategory.getCategory_name(), listCategory.getIcon());
        if (result == -1)
            liveData.setValue(false);
        else
            liveData.setValue(true);

        db.close();
        return liveData;
    }

    public LiveData<Boolean> setSubLiked(Context baseContext, List_subcategory listCategory) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        DatabaseHandler db = new DatabaseHandler(baseContext);

        long result = db.insertData(false, "" + listCategory.getId(), listCategory.getSub_category_name(), listCategory.getIcon());
        if (result == -1)
            liveData.setValue(false);
        else
            liveData.setValue(true);

        db.close();
        return liveData;
    }

    public LiveData<Boolean> deleteFav(Context baseContext, String id, boolean categoryStatus) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        DatabaseHandler db = new DatabaseHandler(baseContext);
        int delete = db.deleteCategory(id, categoryStatus);
        if (delete == 0)
            liveData.setValue(false);
        else
            liveData.setValue(true);

        return liveData;
    }
}