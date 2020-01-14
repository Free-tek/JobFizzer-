
package com.app.jobfizzer.Model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordResponseModel implements Serializable {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    private final static long serialVersionUID = -882105622010566289L;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
