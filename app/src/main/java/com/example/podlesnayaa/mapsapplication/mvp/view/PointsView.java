package com.example.podlesnayaa.mapsapplication.mvp.view;


import android.location.Location;

import com.example.podlesnayaa.mapsapplication.mvp.model.Point;

import java.util.List;

public interface PointsView extends BaseView {

    void onPointsReceived(List<Point> points);

    void onPointsReceivedError(Throwable throwable);

    void onCurrentLocationReceived(Location location);

    void onCurrentLocationError(Throwable throwable);
}
