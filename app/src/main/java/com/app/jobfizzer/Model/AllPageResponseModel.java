package com.app.jobfizzer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AllPageResponseModel implements Serializable{


    @Expose
    @SerializedName("page")
    private List<Page> page;
    @Expose
    @SerializedName("error_message")
    private String error_message;
    @Expose
    @SerializedName("error")
    private String error;

    public AllPageResponseModel() {
    }

    public List<Page> getPage() {
        return page;
    }

    public void setPage(List<Page> page) {
        this.page = page;
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
