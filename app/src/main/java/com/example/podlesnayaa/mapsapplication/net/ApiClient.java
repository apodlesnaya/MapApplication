package com.example.podlesnayaa.mapsapplication.net;


import com.example.podlesnayaa.mapsapplication.mvp.model.Point;

import java.util.List;

import io.reactivex.Observable;

public interface ApiClient {

    Observable<List<Point>> getPoints();

}
