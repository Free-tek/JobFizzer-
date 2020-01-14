package com.app.jobfizzer.Model.ChatMessagesResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChatMessageApiModel implements Serializable {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("msglist")
    @Expose
    private List<Msglist> msglist = null;
    private final static long serialVersionUID = -874018293739882771L;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Msglist> getMsglist() {
        return msglist;
    }

    public void setMsglist(List<Msglist> msglist) {
        this.msglist = msglist;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}