package com.app.jobfizzer.Model.CategoryResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class List_category implements Serializable {
    @Expose
    @SerializedName("baseamount_status")
    private String baseamount_status;
    @Expose
    @SerializedName("baseamount")
    private String baseamount;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("long_description")
    private String long_description;
    @Expose
    @SerializedName("short_description")
    private String short_description;
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("icon")
    private String icon;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("category_name")
    private String category_name;
    @Expose
    @SerializedName("id")
    private int id;

    Boolean isLiked;

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public String getBaseamount_status() {
        return baseamount_status;
    }

    public void setBaseamount_status(String baseamount_status) {
        this.baseamount_status = baseamount_status;
    }

    public String getBaseamount() {
        return baseamount;
    }

    public void setBaseamount(String baseamount) {
        this.baseamount = baseamount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getLong_description() {
        return long_description;
    }

    public void setLong_description(String long_description) {
        this.long_description = long_description;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
