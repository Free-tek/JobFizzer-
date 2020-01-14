package com.app.jobfizzer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavouriteBannerImages implements Serializable {


    @Expose
    @SerializedName("banner_logo")
    private String banner_logo;
    @Expose
    @SerializedName("banner_name")
    private String banner_name;
    @Expose
    @SerializedName("toSubCategory")
    private String toSubCategory;
    @Expose
    @SerializedName("subId")
    private String subId;
    @Expose
    @SerializedName("id")
    private String id;

    public FavouriteBannerImages() {
    }

    public String getBanner_logo() {
        return banner_logo;
    }

    public void setBanner_logo(String banner_logo) {
        this.banner_logo = banner_logo;
    }

    public String getBanner_name() {
        return banner_name;
    }

    public void setBanner_name(String banner_name) {
        this.banner_name = banner_name;
    }

    public String getToSubCategory() {
        return toSubCategory;
    }

    public void setToSubCategory(String toSubCategory) {
        this.toSubCategory = toSubCategory;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
