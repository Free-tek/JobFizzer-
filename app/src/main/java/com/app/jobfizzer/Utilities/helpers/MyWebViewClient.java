package com.app.jobfizzer.Utilities.helpers;

import android.app.ProgressDialog;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class MyWebViewClient extends WebViewClient {
    ProgressDialog pd;
    Button save;
    WebView webView;
    public MyWebViewClient(ProgressDialog progressDialog,Button save,WebView webView) {
        pd=progressDialog;
        this.save=save;
        this.webView=webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        if (!pd.isShowing()) {
            pd.show();
        }
        save.setVisibility(View.GONE);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (pd.isShowing()) {
            pd.dismiss();
        }
        save.setVisibility(View.VISIBLE);
        webView.loadUrl("javascript:(function() { " +
                "document.getElementsByClassName('ndfHFb-c4YZDc-Wrql6b')[0].style.visibility='hidden'; })()");

    }
}
