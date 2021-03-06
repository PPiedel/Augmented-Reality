
package com.example.pawel_piedel.thesis.data.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Hour implements Serializable {

    @SerializedName("hours_type")
    @Expose
    private String hoursType;
    @SerializedName("open")
    @Expose
    private List<Open> open = null;
    @SerializedName("is_open_now")
    @Expose
    private boolean isOpenNow;

    public String getHoursType() {
        return hoursType;
    }

    public void setHoursType(String hoursType) {
        this.hoursType = hoursType;
    }

    public List<Open> getOpen() {
        return open;
    }

    public void setOpen(List<Open> open) {
        this.open = open;
    }

    public boolean isIsOpenNow() {
        return isOpenNow;
    }

    public void setIsOpenNow(boolean isOpenNow) {
        this.isOpenNow = isOpenNow;
    }

}
