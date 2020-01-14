
package com.app.jobfizzer.Model.AppSettingsResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Alltax implements Serializable {

    @SerializedName("taxname")
    @Expose
    private String taxname;
    @SerializedName("tax_amount")
    @Expose
    private String taxAmount;
    @SerializedName("tax_totalamount")
    @Expose
    private String taxTotalamount;

    public String getTaxname() {
        return taxname;
    }

    public void setTaxname(String taxname) {
        this.taxname = taxname;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxTotalamount() {
        return taxTotalamount;
    }

    public void setTaxTotalamount(String taxTotalamount) {
        this.taxTotalamount = taxTotalamount;
    }

}
