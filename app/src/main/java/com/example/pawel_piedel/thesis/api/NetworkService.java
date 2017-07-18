package com.example.pawel_piedel.thesis.api;

import android.util.Log;

import com.example.pawel_piedel.thesis.model.AccessToken;

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


public class NetworkService {
    public static final String LOG_TAG = NetworkService.class.getCanonicalName();
    public static final String API_BASE_URL = "https://api.yelp.com";
    public static final String CLIENT_ID = "VokcbDNJly63jzOhJqJ0JA";
    public static final String CLIENT_SECRET = "gaFo3VLh1cNWS5L7nHJ6nRxVq97iRJCqvBAWnvmoiAWCf2xriOKhp6h5U0LNuj8F";
    public static final String GRANT_TYPE = "client_credentials";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public static OkHttpClient client = new OkHttpClient.Builder().build();
    public static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    public static AccessToken accessToken;


    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        if (accessToken != null) {
            Log.v(LOG_TAG, "Access token NIE jest NULLEM");
            httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new AuthenticationInterceptor(accessToken))
                    .addInterceptor(logging);
        }
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }


}