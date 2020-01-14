
package com.app.jobfizzer.Model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeDashBoardResponseModel implements Serializable
{

    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("Pending")
    @Expose
    private List<Object> pending = null;
    @SerializedName("Accepted")
    @Expose
    private List<Accepted> accepted = null;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("random_request_pending")
    @Expose
    private List<Object> randomRequestPending = null;
    @SerializedName("RecurralPending")
    @Expose
    private List<Object> recurralPending = null;
    private final static long serialVersionUID = -2338417870885654177L;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Object> getPending() {
        return pending;
    }

    public void setPending(List<Object> pending) {
        this.pending = pending;
    }

    public List<Accepted> getAccepted() {
        return accepted;
    }

    public void setAccepted(List<Accepted> accepted) {
        this.accepted = accepted;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Object> getRandomRequestPending() {
        return randomRequestPending;
    }

    public void setRandomRequestPending(List<Object> randomRequestPending) {
        this.randomRequestPending = randomRequestPending;
    }

    public List<Object> getRecurralPending() {
        return recurralPending;
    }

    public void setRecurralPending(List<Object> recurralPending) {
        this.recurralPending = recurralPending;
    }
	public class Accepted implements Serializable
{

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("user_mobile")
    @Expose
    private long userMobile;
    @SerializedName("userimage")
    @Expose
    private String userimage;
    @SerializedName("ProviderName")
    @Expose
    private String providerName;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("sub_category_name")
    @Expose
    private String subCategoryName;
    @SerializedName("long_description")
    @Expose
    private String longDescription;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("timing")
    @Expose
    private String timing;
    @SerializedName("Pending_time")
    @Expose
    private String pendingTime;
    @SerializedName("Accepted_time")
    @Expose
    private Object acceptedTime;
    @SerializedName("Rejected_time")
    @Expose
    private String rejectedTime;
    @SerializedName("Finished_time")
    @Expose
    private Object finishedTime;
    @SerializedName("CancelledbyUser_time")
    @Expose
    private Object cancelledbyUserTime;
    @SerializedName("CancelledbyProvider_time")
    @Expose
    private Object cancelledbyProviderTime;
    @SerializedName("StarttoCustomerPlace_time")
    @Expose
    private Object starttoCustomerPlaceTime;
    @SerializedName("startjob_timestamp")
    @Expose
    private Object startjobTimestamp;
    @SerializedName("endjob_timestamp")
    @Expose
    private Object endjobTimestamp;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("booking_order_id")
    @Expose
    private String bookingOrderId;
    @SerializedName("booking_date")
    @Expose
    private String bookingDate;
    @SerializedName("job_start_time")
    @Expose
    private Object jobStartTime;
    @SerializedName("job_end_time")
    @Expose
    private Object jobEndTime;
    @SerializedName("cost")
    @Expose
    private Object cost;
    @SerializedName("worked_mins")
    @Expose
    private Object workedMins;
    @SerializedName("tax_name")
    @Expose
    private Object taxName;
    @SerializedName("gst_percent")
    @Expose
    private Object gstPercent;
    @SerializedName("gst_cost")
    @Expose
    private Object gstCost;
    @SerializedName("total_cost")
    @Expose
    private Object totalCost;
    @SerializedName("address_line_1")
    @Expose
    private String addressLine1;
    @SerializedName("doorno")
    @Expose
    private String doorno;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    @SerializedName("user_latitude")
    @Expose
    private Double userLatitude;
    @SerializedName("user_longitude")
    @Expose
    private Double userLongitude;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("isProviderReviewed")
    @Expose
    private Integer isProviderReviewed;
    @SerializedName("admin_share")
    @Expose
    private Object adminShare;
    @SerializedName("provider_share")
    @Expose
    private Object providerShare;
    @SerializedName("show_bill_flag")
    @Expose
    private String showBillFlag;
    private final static long serialVersionUID = 3188906362845762260L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(long userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getPendingTime() {
        return pendingTime;
    }

    public void setPendingTime(String pendingTime) {
        this.pendingTime = pendingTime;
    }

    public Object getAcceptedTime() {
        return acceptedTime;
    }

    public void setAcceptedTime(Object acceptedTime) {
        this.acceptedTime = acceptedTime;
    }

    public String getRejectedTime() {
        return rejectedTime;
    }

    public void setRejectedTime(String rejectedTime) {
        this.rejectedTime = rejectedTime;
    }

    public Object getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Object finishedTime) {
        this.finishedTime = finishedTime;
    }

    public Object getCancelledbyUserTime() {
        return cancelledbyUserTime;
    }

    public void setCancelledbyUserTime(Object cancelledbyUserTime) {
        this.cancelledbyUserTime = cancelledbyUserTime;
    }

    public Object getCancelledbyProviderTime() {
        return cancelledbyProviderTime;
    }

    public void setCancelledbyProviderTime(Object cancelledbyProviderTime) {
        this.cancelledbyProviderTime = cancelledbyProviderTime;
    }

    public Object getStarttoCustomerPlaceTime() {
        return starttoCustomerPlaceTime;
    }

    public void setStarttoCustomerPlaceTime(Object starttoCustomerPlaceTime) {
        this.starttoCustomerPlaceTime = starttoCustomerPlaceTime;
    }

    public Object getStartjobTimestamp() {
        return startjobTimestamp;
    }

    public void setStartjobTimestamp(Object startjobTimestamp) {
        this.startjobTimestamp = startjobTimestamp;
    }

    public Object getEndjobTimestamp() {
        return endjobTimestamp;
    }

    public void setEndjobTimestamp(Object endjobTimestamp) {
        this.endjobTimestamp = endjobTimestamp;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Object getJobStartTime() {
        return jobStartTime;
    }

    public void setJobStartTime(Object jobStartTime) {
        this.jobStartTime = jobStartTime;
    }

    public Object getJobEndTime() {
        return jobEndTime;
    }

    public void setJobEndTime(Object jobEndTime) {
        this.jobEndTime = jobEndTime;
    }

    public Object getCost() {
        return cost;
    }

    public void setCost(Object cost) {
        this.cost = cost;
    }

    public Object getWorkedMins() {
        return workedMins;
    }

    public void setWorkedMins(Object workedMins) {
        this.workedMins = workedMins;
    }

    public Object getTaxName() {
        return taxName;
    }

    public void setTaxName(Object taxName) {
        this.taxName = taxName;
    }

    public Object getGstPercent() {
        return gstPercent;
    }

    public void setGstPercent(Object gstPercent) {
        this.gstPercent = gstPercent;
    }

    public Object getGstCost() {
        return gstCost;
    }

    public void setGstCost(Object gstCost) {
        this.gstCost = gstCost;
    }

    public Object getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Object totalCost) {
        this.totalCost = totalCost;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
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

    public Double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(Double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public Double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(Double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsProviderReviewed() {
        return isProviderReviewed;
    }

    public void setIsProviderReviewed(Integer isProviderReviewed) {
        this.isProviderReviewed = isProviderReviewed;
    }

    public Object getAdminShare() {
        return adminShare;
    }

    public void setAdminShare(Object adminShare) {
        this.adminShare = adminShare;
    }

    public Object getProviderShare() {
        return providerShare;
    }

    public void setProviderShare(Object providerShare) {
        this.providerShare = providerShare;
    }

    public String getShowBillFlag() {
        return showBillFlag;
    }

    public void setShowBillFlag(String showBillFlag) {
        this.showBillFlag = showBillFlag;
    }

}

}
