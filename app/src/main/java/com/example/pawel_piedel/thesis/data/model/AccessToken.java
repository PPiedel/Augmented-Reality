package com.example.pawel_piedel.thesis.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

/**
 * Created by Pawel_Piedel on 17.07.2017.
 */

@Generated("org.jsonschema2pojo")
public class AccessToken {
    @SerializedName("access_token")
    @Expose
    private
    String accessToken;

    @SerializedName("token_type")
    @Expose
    private
    String tokenType;

    @SerializedName("expires_in")
    private
    int expiresIn;

    public AccessToken() {
    }

    public AccessToken(String accessToken, String tokenType, int expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        if(!Character.isUpperCase(tokenType.charAt(0))) {
            tokenType = Character.toString(tokenType.charAt(0)).toUpperCase() + tokenType.substring(1);
        }
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccessToken that = (AccessToken) o;

        if (expiresIn != that.expiresIn) return false;
        if (!accessToken.equals(that.accessToken)) return false;
        if (!tokenType.equals(that.tokenType)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = accessToken.hashCode();
        result = 31 * result + tokenType.hashCode();
        result = 31 * result + expiresIn;
        return result;
    }
}
