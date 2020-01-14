package com.app.jobfizzer.Repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.AutoCompleteAddressResponseModel;
import com.app.jobfizzer.Model.FlagResponseModel;
import com.app.jobfizzer.Model.GetLatLongFromIdResponseModel;
import com.app.jobfizzer.Model.GoogleLocationResponseModel.GooglePlaceResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class MapRepository {

    public LiveData<FlagResponseModel> updateValues(InputForAPI inputForAPI) {
        final MutableLiveData<FlagResponseModel> liveData = new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                FlagResponseModel flagResponseModel = gson.fromJson(response.toString(), FlagResponseModel.class);
                liveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                FlagResponseModel flagResponseModel = new FlagResponseModel();
                flagResponseModel.setError("true");
                flagResponseModel.setError_message(error);
                liveData.setValue(flagResponseModel);

            }
        });
        return liveData;
    }

    public LiveData<GooglePlaceResponseModel> getAddresDetails(InputForAPI inputForAPI) {
        final MutableLiveData<GooglePlaceResponseModel> liveData = new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                GooglePlaceResponseModel flagResponseModel = gson.fromJson(response.toString(), GooglePlaceResponseModel.class);
                liveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                GooglePlaceResponseModel flagResponseModel = new GooglePlaceResponseModel();
                flagResponseModel.setStatus("false");
                liveData.setValue(flagResponseModel);

            }
        });
        return liveData;
    }

    public LiveData<AutoCompleteAddressResponseModel> autoCompleteAddress(InputForAPI inputForAPI) {
        final MutableLiveData<AutoCompleteAddressResponseModel> liveData = new MutableLiveData<>();
        ApiCall.PostMethodNoProgress(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                AutoCompleteAddressResponseModel flagResponseModel = gson.fromJson(response.toString(), AutoCompleteAddressResponseModel.class);
                liveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                AutoCompleteAddressResponseModel flagResponseModel = new AutoCompleteAddressResponseModel();
                flagResponseModel.setStatus("false");
                liveData.setValue(flagResponseModel);

            }
        });
        return liveData;
    }

    public LiveData<GetLatLongFromIdResponseModel> getLatLagAddress(InputForAPI inputForAPI) {
        final MutableLiveData<GetLatLongFromIdResponseModel> liveData = new MutableLiveData<>();
        ApiCall.PostMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {
                Gson gson = new Gson();
                GetLatLongFromIdResponseModel flagResponseModel = gson.fromJson(response.toString(), GetLatLongFromIdResponseModel.class);
                liveData.setValue(flagResponseModel);
            }

            @Override
            public void setResponseError(String error) {

                GetLatLongFromIdResponseModel flagResponseModel = new GetLatLongFromIdResponseModel();
                flagResponseModel.setStatus("false");
                liveData.setValue(flagResponseModel);

            }
        });
        return liveData;
    }



}
