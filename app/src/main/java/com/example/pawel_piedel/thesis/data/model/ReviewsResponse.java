
package com.example.pawel_piedel.thesis.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ReviewsResponse implements Serializable {

    @SerializedName("reviews")
    @Expose
    private ArrayList<Review> reviews = null;

    @SerializedName("total")
    @Expose
    private int total;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
