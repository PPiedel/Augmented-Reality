
package com.example.pawel_piedel.thesis.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Business {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("is_claimed")
    @Expose
    private boolean isClaimed;
    @SerializedName("is_closed")
    @Expose
    private boolean isClosed;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("review_count")
    @Expose
    private int reviewCount;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("photos")
    @Expose
    private List<String> photos = null;
    @SerializedName("hours")
    @Expose
    private List<Hour> hours = null;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("transactions")
    @Expose
    private List<String> transactions = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isIsClaimed() {
        return isClaimed;
    }

    public void setIsClaimed(boolean isClaimed) {
        this.isClaimed = isClaimed;
    }

    public boolean isIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public List<Hour> getHours() {
        return hours;
    }

    public void setHours(List<Hour> hours) {
        this.hours = hours;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Business{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isClaimed=" + isClaimed +
                ", isClosed=" + isClosed +
                ", url='" + url + '\'' +
                ", price='" + price + '\'' +
                ", rating=" + rating +
                ", reviewCount=" + reviewCount +
                ", phone='" + phone + '\'' +
                ", photos=" + photos +
                ", hours=" + hours +
                ", categories=" + categories +
                ", coordinates=" + coordinates +
                ", location=" + location +
                ", transactions=" + transactions +
                '}';
    }
}
