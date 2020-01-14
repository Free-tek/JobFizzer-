package com.app.jobfizzer.Utilities.ApiCall;

import android.content.Context;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class InputForAPI {
    private JSONObject jsonObject;
    private String url;

    private HashMap<String, String> headers=new HashMap<>();
    private File file;

    private Context context;


    public InputForAPI(Context context) {
        this.context=context;
    }

    public JSONObject getJsonObject() {

        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
