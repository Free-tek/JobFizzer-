package com.app.jobfizzer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public  class DateModel implements Serializable {

    @Expose
    @SerializedName("selected")
    private String selected;
    @Expose
    @SerializedName("day")
    private String day;
    @Expose
    @SerializedName("fullDate")
    private String fullDate;
    @Expose
    @SerializedName("date")
    private String date;

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFullDate() {
        return fullDate;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
