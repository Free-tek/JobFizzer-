
package com.app.jobfizzer.Model.ChatMessagesResponseModel;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatListResponseModel implements Serializable
{

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sender_profilePic")
    @Expose
    private String senderProfilePic;
    @SerializedName("sender_name")
    @Expose
    private String senderName;
    @SerializedName("receiver_profilePic")
    @Expose
    private String receiverProfilePic;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("currentpage")
    @Expose
    private String currentpage;
    @SerializedName("pageCount")
    @Expose
    private Integer pageCount;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    private final static long serialVersionUID = -9020630788898107020L;

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

    public String getSenderProfilePic() {
        return senderProfilePic;
    }

    public void setSenderProfilePic(String senderProfilePic) {
        this.senderProfilePic = senderProfilePic;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverProfilePic() {
        return receiverProfilePic;
    }

    public void setReceiverProfilePic(String receiverProfilePic) {
        this.receiverProfilePic = receiverProfilePic;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(String currentpage) {
        this.currentpage = currentpage;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
