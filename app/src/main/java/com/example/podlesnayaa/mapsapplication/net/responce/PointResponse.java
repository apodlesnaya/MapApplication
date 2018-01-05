package com.example.podlesnayaa.mapsapplication.net.responce;


import com.google.gson.annotations.SerializedName;

public class PointResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("description")
    private String description;

    @SerializedName("title")
    private String title;

    public int getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

}
