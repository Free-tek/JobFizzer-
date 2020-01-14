package com.app.jobfizzer.Utilities.ApiCall;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.jobfizzer.Utilities.ConnectionUtils;
import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.app.jobfizzer.Utilities.helpers.Utils;
import com.app.jobfizzer.application.Jobfizzer;
import com.app.jobfizzer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Created by user on 23-10-2017.
 */

public class ApiCall {


    private static final int MY_SOCKET_TIMEOUT_MS = 50000;
    private static String TAG = ApiCall.class.getSimpleName();


    public static void PostMethod(InputForAPI inputForAPI, final ApiResponseHandler volleyCallback) {
        final Context context = inputForAPI.getContext();
        final String url = inputForAPI.getUrl();
        JSONObject params = inputForAPI.getJsonObject();
        final HashMap<String, String> headers = inputForAPI.getHeaders();
        if (ConnectionUtils.isNetworkConnected(context)) {
            Utils.show(context);
            Utils.log(TAG, "url:" + url + " , input: " + params);
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.log(TAG, "url:" + url + " , response: " + response);
                            Utils.dismiss();
                            volleyCallback.setResponseSuccess(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.log(TAG, "url:" + url + " , onErrorResponse: " + error);
                    Utils.dismiss();
                    if (error instanceof TimeoutError) {
                        volleyCallback.setResponseError(context.getString(R.string.time_out_error));
                    } else if (error instanceof NoConnectionError) {
                        volleyCallback.setResponseError(context.getString(R.string.no_internet_connection));
                    } else if (error instanceof AuthFailureError) {
                        volleyCallback.setResponseError(context.getString(R.string.session_expired));
                    } else if (error instanceof ServerError) {
                        volleyCallback.setResponseError(context.getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {
                        volleyCallback.setResponseError(context.getString(R.string.network_error));
                    } else if (error instanceof ParseError) {
                        volleyCallback.setResponseError(context.getString(R.string.parsing_error));
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
//                    Log.e(TAG, "getHeaders: " + headers.toString());
                    return headers;
                }
            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Jobfizzer.getInstance().addToRequestQueue(jsonObjReq);

        } else {
            volleyCallback.setResponseError(ConnectionUtils.NO_CONNECTION);
        }

    }


    public static void PostMethodNoProgress(InputForAPI inputForAPI, final ApiResponseHandler volleyCallback) {
        final Context context = inputForAPI.getContext();
        final String url = inputForAPI.getUrl();
        JSONObject params = inputForAPI.getJsonObject();
        final HashMap<String, String> headers = inputForAPI.getHeaders();
        if (ConnectionUtils.isNetworkConnected(context)) {
            Utils.log(TAG, "url:" + url + " , input: " + params);
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.log(TAG, "url:" + url + " , response: " + response);
                            volleyCallback.setResponseSuccess(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.log(TAG, "url:" + url + " , onErrorResponse: " + error);
                    if (error instanceof TimeoutError) {
                        volleyCallback.setResponseError(context.getString(R.string.time_out_error));
                    } else if (error instanceof NoConnectionError) {
                        volleyCallback.setResponseError(context.getString(R.string.no_internet_connection));
                    } else if (error instanceof AuthFailureError) {
                        volleyCallback.setResponseError(context.getString(R.string.session_expired));
                    } else if (error instanceof ServerError) {
                        volleyCallback.setResponseError(context.getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {
                        volleyCallback.setResponseError(context.getString(R.string.network_error));
                    } else if (error instanceof ParseError) {
                        volleyCallback.setResponseError(context.getString(R.string.parsing_error));
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
//                    Log.e(TAG, "getHeaders: " + headers.toString());
                    return headers;
                }
            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Jobfizzer.getInstance().addToRequestQueue(jsonObjReq);

        } else {
            volleyCallback.setResponseError(ConnectionUtils.NO_CONNECTION);
        }

    }


    public static void GetMethod(final InputForAPI inputForAPI, final ApiResponseHandler volleyCallback) {
        if (ConnectionUtils.isNetworkConnected(inputForAPI.getContext())) {
            Utils.show(inputForAPI.getContext());
            Utils.log(TAG, "Getter, url: " + inputForAPI.getUrl());
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    inputForAPI.getUrl(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Utils.dismiss();
                            Utils.log(TAG, "Getter,url: " + inputForAPI.getUrl() + " , response: " + response);
                            volleyCallback.setResponseSuccess(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Utils.dismiss();
                    Utils.log(TAG, "Getter,url: " + inputForAPI.getUrl() + " , onErrorResponse: " + error);
                    if (error instanceof TimeoutError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.time_out_error));
                    } else if (error instanceof NoConnectionError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.no_internet_connection));
                    } else if (error instanceof AuthFailureError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.session_expired));
                    } else if (error instanceof ServerError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.server_error));
                    } else if (error instanceof NetworkError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.network_error));
                    } else if (error instanceof ParseError) {
                        volleyCallback.setResponseError(inputForAPI.getContext().getString(R.string.parsing_error));
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
//                    Utils.log(TAG, "Getter,url: " + inputForAPI.getUrl() + " , getHeaders " + inputForAPI.getHeaders());
                    return inputForAPI.getHeaders();
                }
            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Jobfizzer.getInstance().addToRequestQueue(jsonObjReq);

        } else {
            volleyCallback.setResponseError(ConnectionUtils.NO_CONNECTION);
        }

    }


    public static HashMap<String, String> getHeaders(Context context) {
        AppSettings appSettings = new AppSettings(context);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Authorization", "Bearer " + appSettings.getToken());
        return headers;
    }

    public static void uploadImage(Context context, File file, String url, final ApiResponseHandler apiResponseHandler) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/jpeg");
        AppSettings appSettings = new AppSettings(context);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, new File(file.getPath())))
                .build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url).addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + appSettings.getToken())
                .post(requestBody).build();
        Log.e(TAG, "file: " + file.getPath());

        try {
            okhttp3.Response response = client.newCall(request).execute();
            Log.d("responseURL", response.body().toString());
            JSONObject jsonObject;
            if (response.body() != null) {
                jsonObject = new JSONObject(response.body().string());
                apiResponseHandler.setResponseSuccess(jsonObject);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            apiResponseHandler.setResponseError(e.getMessage());
        } catch (JSONException e) {
            apiResponseHandler.setResponseError(e.getMessage());
            Log.e(TAG, "IOException " + e.getMessage());
        }
    }

    public interface ApiResponseHandler {

        public void setResponseSuccess(JSONObject response);

        public void setResponseError(String error);

    }

}
