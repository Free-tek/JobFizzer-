package com.app.jobfizzer.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Page {
    @Expose
    @SerializedName("termsAndConditionContent")
    private String termsAndConditionContent;
    @Expose
    @SerializedName("privacyPolicyContent")
    private String privacyPolicyContent;
    @Expose
    @SerializedName("termsAndCondition")
    private String termsAndCondition;
    @Expose
    @SerializedName("privacyPolicy")
    private String privacyPolicy;

    public String getTermsAndConditionContent() {
        return termsAndConditionContent;
    }

    public void setTermsAndConditionContent(String termsAndConditionContent) {
        this.termsAndConditionContent = termsAndConditionContent;
    }

    public String getPrivacyPolicyContent() {
        return privacyPolicyContent;
    }

    public void setPrivacyPolicyContent(String privacyPolicyContent) {
        this.privacyPolicyContent = privacyPolicyContent;
    }

    public String getTermsAndCondition() {
        return termsAndCondition;
    }

    public void setTermsAndCondition(String termsAndCondition) {
        this.termsAndCondition = termsAndCondition;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }
}
