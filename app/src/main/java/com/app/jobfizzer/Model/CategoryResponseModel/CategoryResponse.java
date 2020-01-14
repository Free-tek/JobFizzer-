package com.app.jobfizzer.Model.CategoryResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    @Expose
    @SerializedName("location")
    private List<Location> location;
    @Expose
    @SerializedName("banner_images")
    private List<Banner_images> banner_images;
    @Expose
    @SerializedName("error_message")
    private String error_message;
    @Expose
    @SerializedName("error")
    private String error;
    @Expose
    @SerializedName("list_category")
    private List<List_category> list_category;

    public List<Location> getLocation() {
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    public List<Banner_images> getBanner_images() {
        return banner_images;
    }

    public void setBanner_images(List<Banner_images> banner_images) {
        this.banner_images = banner_images;
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

    public List<List_category> getList_category() {
        return list_category;
    }

    public void setList_category(List<List_category> list_category) {
        this.list_category = list_category;
    }
}
