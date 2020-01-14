package com.app.jobfizzer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ListAddressResponseModel implements Serializable {

    @Expose
    @SerializedName("list_address")
    private List<List_address> list_address;
    @Expose
    @SerializedName("error_message")
    private String error_message;
    @Expose
    @SerializedName("error")
    private String error;

    public List<List_address> getList_address() {
        return list_address;
    }

    public void setList_address(List<List_address> list_address) {
        this.list_address = list_address;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
