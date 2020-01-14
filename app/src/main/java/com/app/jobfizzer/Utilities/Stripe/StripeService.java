
package com.app.jobfizzer.Utilities.Stripe;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * A Retrofit service used to communicate with a server.
 */
public interface StripeService {

    @FormUrlEncoded
    @POST("ephemeral_keys")
    Observable<ResponseBody> createEphemeralKey(@Header("Accept") String accept, @Header("Authorization") String auth,
                                                @FieldMap Map<String, String> apiVersionMap);

//    Call<ResponseBody> create(@Header("")String aut, @Field("")String name);

}
