
package com.app.jobfizzer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Term implements Serializable
{

    @SerializedName("offset")
    @Expose
    private Integer offset;
    @SerializedName("value")
    @Expose
    private String value;
    private final static long serialVersionUID = 2936073301497354543L;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
