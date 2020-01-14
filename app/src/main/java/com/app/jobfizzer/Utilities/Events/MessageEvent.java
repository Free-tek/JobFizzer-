package com.app.jobfizzer.Utilities.Events;

import org.json.JSONObject;

/*
 * Created by yuvaraj on 02/12/17.
 */

public class MessageEvent {
    private JSONObject jsonObject;

    public MessageEvent(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}