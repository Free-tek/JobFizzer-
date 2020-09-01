package com.app.jobfizzer.Repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.jobfizzer.Model.ViewBookingsResponseModel.BookingResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.ApiCall;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.google.gson.Gson;

import org.json.JSONObject;

public class BookingsRepository {
    public LiveData<BookingResponseModel> getBookingList(InputForAPI inputForAPI) {

        final MutableLiveData<BookingResponseModel> bookingList=new MutableLiveData<>();
        ApiCall.GetMethod(inputForAPI, new ApiCall.ApiResponseHandler() {
            @Override
            public void setResponseSuccess(JSONObject response) {

                Gson gson=new Gson();
                BookingResponseModel bookingResponseModel=gson.fromJson(response.toString(),BookingResponseModel.class);
                bookingList.setValue(bookingResponseModel);
            }

            @Override
            public void setResponseError(String error) {


                BookingResponseModel bookingResponseModel= new BookingResponseModel();
                bookingResponseModel.setError("true");
                bookingResponseModel.setErrorMessage(error);
                bookingList.setValue(bookingResponseModel);
            }
        });


        return bookingList;

    }
}
