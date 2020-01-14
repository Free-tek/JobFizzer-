package com.app.jobfizzer.Model;

import java.io.Serializable;

public class PayStachAccessURLResponse implements Serializable {

    String error;
    String error_data;
    boolean status;
    String message;

    URLData data = new URLData();

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_data() {
        return error_data;
    }

    public void setError_data(String error_data) {
        this.error_data = error_data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public URLData getData() {
        return data;
    }

    public void setData(URLData data) {
        this.data = data;
    }

    public class URLData implements Serializable{
        String authorization_url;
        String access_code;
        String reference;

        public String getAuthorization_url() {
            return authorization_url;
        }

        public void setAuthorization_url(String authorization_url) {
            this.authorization_url = authorization_url;
        }

        public String getAccess_code() {
            return access_code;
        }

        public void setAccess_code(String access_code) {
            this.access_code = access_code;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }
    }
}
