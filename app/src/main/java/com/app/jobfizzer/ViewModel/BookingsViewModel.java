package com.app.jobfizzer.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

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
