
package com.app.jobfizzer.Model.StartJobEndJobDetailsResponse;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartJobEndJobResponseModel {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("invoicelink")
    @Expose
    private List<Invoicelink> invoicelink = null;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<Invoicelink> getInvoicelink() {
        return invoicelink;
    }

    public void setInvoicelink(List<Invoicelink> invoicelink) {
        this.invoicelink = invoicelink;
    }

}
