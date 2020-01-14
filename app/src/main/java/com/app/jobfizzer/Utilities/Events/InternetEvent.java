package com.app.jobfizzer.Utilities.Events;

/*
 * Created by yuvaraj on 02/12/17.
 */

public class InternetEvent {

    private boolean internetStatus;

    public InternetEvent(boolean internetStatus) {
        this.internetStatus = internetStatus;
    }

    public boolean isInternetStatus() {
        return internetStatus;
    }

}