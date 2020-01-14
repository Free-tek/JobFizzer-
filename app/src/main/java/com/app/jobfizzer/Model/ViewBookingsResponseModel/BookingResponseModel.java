
package com.app.jobfizzer.Model.ViewBookingsResponseModel;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingResponseModel implements Serializable {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("all_bookings")
    @Expose
    private List<AllBooking> allBookings = null;
    private final static long serialVersionUID = 116382718841367736L;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<AllBooking> getAllBookings() {
        return allBookings;
    }

    public void setAllBookings(List<AllBooking> allBookings) {
        this.allBookings = allBookings;
    }

}
