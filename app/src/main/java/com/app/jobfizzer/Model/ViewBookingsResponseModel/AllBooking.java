
package com.app.jobfizzer.Model.ViewBookingsResponseModel;

import java.io.Serializable;
import java.util.List;

import com.app.jobfizzer.Utilities.helpers.Utils;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllBooking implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("booking_order_id")
    @Expose
    private String bookingOrderId;
    @SerializedName("user_mobile")
    @Expose
    private long userMobile;
    @SerializedName("provider_mobile")
    @Expose
    private long providerMobile;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("providername")
    @Expose
    private String providername;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("provider_id")
    @Expose
    private Integer providerId;
    @SerializedName("provider_latitude")
    @Expose
    private Double providerLatitude;
    @SerializedName("provider_longitude")
    @Expose
    private Double providerLongitude;
    @SerializedName("provider_bearing")
    @Expose
    private String providerBearing;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("timing")
    @Expose
    private String timing;
    @SerializedName("booking_date")
    @Expose
    private String bookingDate;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("Pending_time")
    @Expose
    private String pendingTime;
    @SerializedName("Accepted_time")
    @Expose
    private String acceptedTime;
    @SerializedName("Rejected_time")
    @Expose
    private String rejectedTime;
    @SerializedName("Finished_time")
    @Expose
    private String finishedTime;
    @SerializedName("startjob_timestamp")
    @Expose
    private String startjobTimestamp;
    @SerializedName("endjob_timestamp")
    @Expose
    private String endjobTimestamp;
    @SerializedName("CancelledbyUser_time")
    @Expose
    private String cancelledbyUserTime;
    @SerializedName("CancelledbyProvider_time")
    @Expose
    private String cancelledbyProviderTime;
    @SerializedName("StarttoCustomerPlace_time")
    @Expose
    private String starttoCustomerPlaceTime;
    @SerializedName("job_start_time")
    @Expose
    private String jobStartTime;
    @SerializedName("job_end_time")
    @Expose
    private String jobEndTime;
    @SerializedName("cost")
    @Expose
    private String cost;
    @SerializedName("tax_name")
    @Expose
    private String taxName;
    @SerializedName("gst_percent")
    @Expose
    private Integer gstPercent;
    @SerializedName("gst_cost")
    @Expose
    private float gstCost;
    @SerializedName("total_cost")
    @Expose
    private String totalCost;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("doorno")
    @Expose
    private String doorno;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("address_line_1")
    @Expose
    private String addressLine1;
    @SerializedName("boooking_latitude")
    @Expose
    private Double boookingLatitude;
    @SerializedName("booking_longitude")
    @Expose
    private Double bookingLongitude;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("feedback")
    @Expose
    private String feedback;
    @SerializedName("show_bill_flag")
    @Expose
    private String showBillFlag;
    @SerializedName("worked_mins")
    @Expose
    private String workedMins;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("invoicedetails")
    @Expose
    private List<Invoicedetail> invoicedetails = null;

    private final static long serialVersionUID = -2001927104804618923L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookingOrderId() {
        return bookingOrderId;
    }

    public void setBookingOrderId(String bookingOrderId) {
        this.bookingOrderId = bookingOrderId;
    }

    public long getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(Integer userMobile) {
        this.userMobile = userMobile;
    }

    public long getProviderMobile() {
        return providerMobile;
    }

    public void setProviderMobile(Integer providerMobile) {
        this.providerMobile = providerMobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getProviderId() {
        return providerId;
    }

    public void setProviderId(Integer providerId) {
        this.providerId = providerId;
    }

    public Double getProviderLatitude() {
        return providerLatitude;
    }

    public void setProviderLatitude(Double providerLatitude) {
        this.providerLatitude = providerLatitude;
    }

    public Double getProviderLongitude() {
        return providerLongitude;
    }

    public void setProviderLongitude(Double providerLongitude) {
        this.providerLongitude = providerLongitude;
    }

    public String getProviderBearing() {
        return providerBearing;
    }

    public void setProviderBearing(String providerBearing) {
        this.providerBearing = providerBearing;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPendingTime() {
        return pendingTime;
    }

    public void setPendingTime(String pendingTime) {
        this.pendingTime = pendingTime;
    }

    public String getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(String acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public String getStartjobTimestamp() {
        return startjobTimestamp;
    }

    public void setStartjobTimestamp(String startjobTimestamp) {
        this.startjobTimestamp = startjobTimestamp;
    }

    public String getEndjobTimestamp() {
        return endjobTimestamp;
    }

    public void setEndjobTimestamp(String endjobTimestamp) {
        this.endjobTimestamp = endjobTimestamp;
    }


    public String getStarttoCustomerPlaceTime() {
        return starttoCustomerPlaceTime;
    }

    public void setStarttoCustomerPlaceTime(String starttoCustomerPlaceTime) {
        this.starttoCustomerPlaceTime = starttoCustomerPlaceTime;
    }

    public String getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(String jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public String getJobEndTime() {
        return jobEndTime;
    }

    public void setJobEndTime(String jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    public String getCost() {
        return Utils.getRoundUpValue(cost);
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public Integer getGstPercent() {
        return gstPercent;
    }

    public void setGstPercent(Integer gstPercent) {
        this.gstPercent = gstPercent;
    }

    public float getGstCost() {
        return gstCost;
    }

    public void setGstCost(Integer gstCost) {
        this.gstCost = gstCost;
    }

    public String getTotalCost() {
        return Utils.getRoundUpValue(totalCost);
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDoorno() {
        return doorno;
    }

    public void setDoorno(String doorno) {
        this.doorno = doorno;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public Double getBoookingLatitude() {
        return boookingLatitude;
    }

    public void setBoookingLatitude(Double boookingLatitude) {
        this.boookingLatitude = boookingLatitude;
    }

    public Double getBookingLongitude() {
        return bookingLongitude;
    }

    public void setBookingLongitude(Double bookingLongitude) {
        this.bookingLongitude = bookingLongitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRejectedTime() {
        return rejectedTime;
    }

    public void setRejectedTime(String rejectedTime) {
        this.rejectedTime = rejectedTime;
    }

    public String getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(String finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getCancelledbyUserTime() {
        return cancelledbyUserTime;
    }

    public void setCancelledbyUserTime(String cancelledbyUserTime) {
        this.cancelledbyUserTime = cancelledbyUserTime;
    }

    public String getCancelledbyProviderTime() {
        return cancelledbyProviderTime;
    }

    public void setCancelledbyProviderTime(String cancelledbyProviderTime) {
        this.cancelledbyProviderTime = cancelledbyProviderTime;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getShowBillFlag() {
        return showBillFlag;
    }

    public void setShowBillFlag(String showBillFlag) {
        this.showBillFlag = showBillFlag;
    }

    public String getWorkedMins() {
        return workedMins;
    }

    public void setWorkedMins(String workedMins) {
        this.workedMins = workedMins;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Invoicedetail> getInvoicedetails() {
        return invoicedetails;
    }

    public void setInvoicedetails(List<Invoicedetail> invoicedetails) {
        this.invoicedetails = invoicedetails;
    }

}