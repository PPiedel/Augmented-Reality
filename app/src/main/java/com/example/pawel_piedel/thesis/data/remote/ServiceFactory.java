package com.example.pawel_piedel.thesis.data.remote;

import com.example.pawel_piedel.thesis.data.DataManager;
import com.example.pawel_piedel.thesis.data.model.AccessToken;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pawel_Piedel on 04.07.2017.
 * Based on https://github.com/jpelgrom/retrofit2-oauthrefresh
 * and https://futurestud.io/tutorials/oauth-2-on-android-with-retrofit
 */


public class ServiceFactory {
    private final String LOG_TAG = ServiceFactory.class.getCanonicalName();
    private static final String API_BASE_URL = "https://api.yelp.com";
    public static final String CLIENT_ID = "VokcbDNJly63jzOhJqJ0JA";
    public static final String CLIENT_SECRET = "gaFo3VLh1cNWS5L7nHJ6nRxVq97iRJCqvBAWnvmoiAWCf2xriOKhp6h5U0LNuj8F";
    public static final String GRANT_TYPE = "client_credentials";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    public static AccessToken accessToken;


    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (accessToken != null) {
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthenticationInterceptor(accessToken));

        }
        httpClient.addInterceptor(logging);
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }


}
