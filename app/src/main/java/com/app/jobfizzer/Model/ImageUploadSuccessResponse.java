package com.app.jobfizzer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUploadSuccessResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("image")
    @Expose
    private String image;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
