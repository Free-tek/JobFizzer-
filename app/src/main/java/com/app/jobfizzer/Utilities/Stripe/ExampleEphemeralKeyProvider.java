package com.app.jobfizzer.Utilities.Stripe;

import android.content.Context;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Size;

import com.app.jobfizzer.Utilities.Prefhandler.AppSettings;
import com.stripe.android.EphemeralKeyProvider;
import com.stripe.android.EphemeralKeyUpdateListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * An implementation of {@link EphemeralKeyProvider} that can be used to generate
 * ephemeral keys on the backend.
 */
public class ExampleEphemeralKeyProvider implements EphemeralKeyProvider {

    private @NonNull
    CompositeSubscription mCompositeSubscription;
    private @NonNull
    StripeService mStripeService;
    private @NonNull
    ProgressListener mProgressListener;
    Context context;

    public ExampleEphemeralKeyProvider(@NonNull ProgressListener progressListener, Context context) {
        Retrofit retrofit = RetrofitFactory.getInstance();
        mStripeService = retrofit.create(StripeService.class);
        mCompositeSubscription = new CompositeSubscription();
        mProgressListener = progressListener;
        this.context = context;
    }

    @Override
    public void createEphemeralKey(@NonNull @Size(min = 4) String apiVersion,
                                   @NonNull final EphemeralKeyUpdateListener keyUpdateListener) {
        Map<String, String> apiParamMap = new HashMap<>();
        apiParamMap.put("api_version", apiVersion);
        AppSettings appSettings = new AppSettings(context);
        mCompositeSubscription.add(
                mStripeService.createEphemeralKey("application/json",
                        "Bearer " + appSettings.getToken(), apiParamMap)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ResponseBody>() {
                            @Override
                            public void call(ResponseBody response) {
                                Log.e("RETROFIT TAG", "call: " + response);
                                try {
                                    String rawKey = response.string();
                                    keyUpdateListener.onKeyUpdate(rawKey);
                                    mProgressListener.onStringResponse(rawKey);
                                } catch (IOException iox) {

                                }
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mProgressListener.onStringResponse(throwable.getMessage());
                            }
                        }));
    }

    public interface ProgressListener {
        void onStringResponse(String string);
    }
}
