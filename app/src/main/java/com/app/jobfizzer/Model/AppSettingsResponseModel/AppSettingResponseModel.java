
package com.app.jobfizzer.Model.AppSettingsResponseModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppSettingResponseModel {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("delete_status")
    @Expose
    private String deleteStatus;
    @SerializedName("location")
    @Expose
    private List<Location> location = null;
    @SerializedName("timeslots")
    @Expose
    private List<Timeslot> timeslots = null;
    @SerializedName("status")
    @Expose
    private List<StatusAppSettings> status = null;
    @SerializedName("wallet")
    @Expose
    private List<Object> wallet = null;

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

    public String getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(String deleteStatus) {
        this.deleteStatus = deleteStatus;
    }
    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }

    public List<StatusAppSettings> getStatus() {
        return status;
    }

    public void setStatus(List<StatusAppSettings> status) {
        this.status = status;
    }

    public List<Object> getWallet() {
        return wallet;
    }

    public void setWallet(List<Object> wallet) {
        this.wallet = wallet;
    }

}
