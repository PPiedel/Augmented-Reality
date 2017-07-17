
package com.example.pawel_piedel.thesis.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Open {

    @SerializedName("is_overnight")
    @Expose
    private boolean isOvernight;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("day")
    @Expose
    private int day;
    @SerializedName("start")
    @Expose
    private String start;

    public boolean isIsOvernight() {
        return isOvernight;
    }

    public void setIsOvernight(boolean isOvernight) {
        this.isOvernight = isOvernight;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

}
