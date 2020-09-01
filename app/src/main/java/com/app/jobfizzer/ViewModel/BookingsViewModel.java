package com.app.jobfizzer.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.jobfizzer.Model.ViewBookingsResponseModel.BookingResponseModel;
import com.app.jobfizzer.Repository.BookingsRepository;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;

public class BookingsViewModel extends AndroidViewModel {
    BookingsRepository bookingsRepository;
    public BookingsViewModel(@NonNull Application application) {
        super(application);
        bookingsRepository=new BookingsRepository();
    }


    public LiveData<BookingResponseModel> getBookingList(InputForAPI inputForAPI){
        return bookingsRepository.getBookingList(inputForAPI);
    }
}
