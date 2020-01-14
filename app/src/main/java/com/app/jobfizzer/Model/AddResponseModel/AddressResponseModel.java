
package com.app.jobfizzer.Model.AddResponseModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressResponseModel {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("list_address")
    @Expose
    private List<ListAddress> listAddress = null;

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

    public List<ListAddress> getListAddress() {
        return listAddress;
    }

    public void setListAddress(List<ListAddress> listAddress) {
        this.listAddress = listAddress;
    }

}
