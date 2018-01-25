package com.example.pawel_piedel.thesis.data.auth.remote;

import com.example.pawel_piedel.thesis.data.model.AccessToken;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Pawel_Piedel on 17.07.2017.
 */

public class AuthenticationInterceptor implements Interceptor {
    private AccessToken accessToken;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .method(original.method(), original.body());

        if (accessToken != null) {
            requestBuilder.header("Authorization",
                    accessToken.getTokenType() + " " + accessToken.getAccessToken());
        }


        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}

