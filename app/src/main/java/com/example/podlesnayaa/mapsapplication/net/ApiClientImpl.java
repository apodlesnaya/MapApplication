package com.example.podlesnayaa.mapsapplication.net;


import com.example.podlesnayaa.mapsapplication.mvp.model.Point;
import com.example.podlesnayaa.mapsapplication.net.responce.PointResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApiClientImpl implements ApiClient {

    private ApiService apiService;

    public ApiClientImpl(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Observable<List<Point>> getPoints() {
        return apiService.getPoints()
                .map(pointResponses -> {
                    List<Point> points = new ArrayList<>();
                    for (PointResponse response : pointResponses) {
                        points.add(new Point(response));
                    }
                    return points;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
