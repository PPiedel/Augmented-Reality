package com.example.pawel_piedel.thesis.api;

import android.util.Log;

import com.example.pawel_piedel.thesis.model.AccessToken;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Pawel_Piedel on 04.07.2017.
 * Based on https://github.com/jpelgrom/retrofit2-oauthrefresh
 * and https://futurestud.io/tutorials/oauth-2-on-android-with-retrofit
 */


public class ServiceGenerator {
    public static final String LOG_TAG = ServiceGenerator.class.getCanonicalName();
    public static final String API_BASE_URL = "https://api.yelp.com";
    public static final String CLIENT_ID = "VokcbDNJly63jzOhJqJ0JA";
    public static final String CLIENT_SECRET = "gaFo3VLh1cNWS5L7nHJ6nRxVq97iRJCqvBAWnvmoiAWCf2xriOKhp6h5U0LNuj8F";
    public static final String GRANT_TYPE = "client_credentials";

    private static OkHttpClient.Builder httpClient;
    private static OkHttpClient authClient;

    private static Retrofit.Builder builder;

    private static AccessToken accessToken;

    public static <S> S createService(Class<S> serviceClass) {
        try {
            accessToken = getAccessToken(CLIENT_ID,CLIENT_SECRET);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpClient = new OkHttpClient.Builder()
                        .addInterceptor(new AuthenticationInterceptor(accessToken));
        OkHttpClient client = httpClient.build();

        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static AccessToken getAccessToken(String clientId, String clientSecret) throws IOException {
        authClient = new OkHttpClient.Builder()
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(authClient)
                .build();

        ApiService client = retrofit.create(ApiService.class);
        Call<AccessToken> call = client.getAccessToken(CLIENT_ID,CLIENT_SECRET,"client_credentials");
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                Log.v(LOG_TAG,response.body().getAccessToken());
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.v(LOG_TAG,"Failure");
            }
        });
        return accessToken;
    }







}
