package com.example.podlesnayaa.mapsapplication.net;


import com.example.podlesnayaa.mapsapplication.net.responce.PointResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {

    @GET("api/json/get/bPvorlFkwO?indent=2")
    Observable<List<PointResponse>> getPoints();

}
