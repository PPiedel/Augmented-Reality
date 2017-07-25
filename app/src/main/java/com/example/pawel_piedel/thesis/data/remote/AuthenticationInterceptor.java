package com.example.pawel_piedel.thesis.data.remote;

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

    public AuthenticationInterceptor(AccessToken token) {
        this.accessToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-type", "application/json")
                .header("Authorization",
                        accessToken.getTokenType() + " " + accessToken.getAccessToken())
                .method(original.method(), original.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}

