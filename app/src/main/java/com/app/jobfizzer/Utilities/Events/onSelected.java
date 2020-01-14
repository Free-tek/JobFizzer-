package com.app.jobfizzer.Utilities.Events;

/**
 * Created by yuvaraj on 2/2/18.
 */

public class onSelected {
    String checkValue;

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    public onSelected(String checkValue) {

        this.checkValue = checkValue;
    }

    public onSelected() {
    }
}
