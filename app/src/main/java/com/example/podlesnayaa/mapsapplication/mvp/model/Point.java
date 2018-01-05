package com.example.podlesnayaa.mapsapplication.mvp.model;


import com.example.podlesnayaa.mapsapplication.net.responce.PointResponse;

public class Point {

    private int id;
    private double lat;
    private double lng;
    private String description;
    private String title;

    public Point(PointResponse response) {
        id = response.getId();
        lat = response.getLat();
        lng = response.getLng();
        description = response.getDescription();
        title = response.getTitle();
    }

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
