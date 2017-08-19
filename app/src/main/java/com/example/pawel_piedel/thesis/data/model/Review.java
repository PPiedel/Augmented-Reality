
package com.example.pawel_piedel.thesis.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Review implements Serializable {

    @SerializedName("rating")
    @Expose
    public int rating;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("text")
    @Expose
    public String text;
    @SerializedName("time_created")
    @Expose
    public String timeCreated;
    @SerializedName("url")
    @Expose
    public String url;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Review{" +
                "rating=" + rating +
                ", user=" + user +
                ", text='" + text + '\'' +
                ", timeCreated='" + timeCreated + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
