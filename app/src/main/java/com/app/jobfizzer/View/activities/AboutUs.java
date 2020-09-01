package com.app.jobfizzer.View.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.app.jobfizzer.Model.AllPageResponseModel;
import com.app.jobfizzer.Utilities.ApiCall.InputForAPI;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.ViewModel.CommonViewModel;
import com.app.jobfizzer.Utilities.Constants.UrlHelper;
import com.app.jobfizzer.R;

import org.json.JSONObject;

import java.util.HashMap;

public class AboutUs extends BaseActivity {
    WebView webView;
    ImageView backButton;
    CommonViewModel aboutUsViewModel;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        initViewsAndListners();
        getValues();

    }

    private void getValues() {
        InputForAPI inputForAPI = new InputForAPI(AboutUs.this);
        inputForAPI.setUrl(UrlHelper.ABOUT_US);
        inputForAPI.setFile(null);
        inputForAPI.setJsonObject(new JSONObject());
        inputForAPI.setHeaders(new HashMap<String, String>());
        getUrlFromAPI(inputForAPI);

    }

    private void initViewsAndListners() {

        aboutUsViewModel = ViewModelProviders.of(this).get(CommonViewModel.class);
        webView = findViewById(R.id.webView);
        textView = findViewById(R.id.textView);
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_out_right);
    }

    private void getUrlFromAPI(InputForAPI inputForAPI) {

        aboutUsViewModel.getAllPageResponse(inputForAPI).observe(this, new Observer<AllPageResponseModel>() {
            @Override
            public void onChanged(@Nullable AllPageResponseModel allPageResponseModel) {
                if (allPageResponseModel != null) {
                    if (!allPageResponseModel.getError().equalsIgnoreCase("true")) {
                        handleAllpageResponse(allPageResponseModel);
                    }else {
                        Utils.toast(findViewById(android.R.id.content), allPageResponseModel.getError_message());
                    }
                }
            }
        });
    }

    private void handleAllpageResponse(AllPageResponseModel allPageResponseModel) {

        textView.setText(allPageResponseModel.getPage().get(0).getPrivacyPolicy());
    }
}