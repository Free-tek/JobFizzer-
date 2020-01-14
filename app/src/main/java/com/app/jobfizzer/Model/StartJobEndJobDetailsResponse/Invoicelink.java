
package com.app.jobfizzer.Model.StartJobEndJobDetailsResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Invoicelink {

    @SerializedName("invoicelink")
    @Expose
    private String invoicelink;

    public String getInvoicelink() {
        return invoicelink;
    }

    public void setInvoicelink(String invoicelink) {
        this.invoicelink = invoicelink;
    }

}
