package com.example.podlesnayaa.mapsapplication.mvp.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;

import com.example.podlesnayaa.mapsapplication.App;
import com.example.podlesnayaa.mapsapplication.mvp.view.PointsView;
import com.example.podlesnayaa.mapsapplication.net.ApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PointsPresenter extends BasePresenter<PointsView> {

    @Inject
    ApiClient apiClient;

    private boolean isLocationSet;
    private double currentLat;
    private double currentLng;

    public PointsPresenter() {
        App.appComponent().inject(this);
    }

    @Override
    protected void onAttachView(@NonNull PointsView view) {
        super.onAttachView(view);
    }

    public void getPointsOnMap() {
        if (getView() != null) {
            apiClient.getPoints().subscribe(
                    points -> getView().onPointsReceived(points),
                    throwable -> getView().onPointsReceivedError(throwable)
            );
        }
    }

    @SuppressLint("MissingPermission") //need to check in caller method
    public void getCurrentLocation() {
        Single.create((SingleOnSubscribe<Location>) e ->
                LocationServices.getFusedLocationProviderClient(getView().getContext()).getLastLocation()
                .addOnSuccessListener((Activity) getView().getContext(), location -> {
                    if (location != null) {
                        isLocationSet = true;
                        currentLat = location.getLatitude();
                        currentLng = location.getLongitude();

                        e.onSuccess(location);
                    }
                })).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location ->
                                getView().onCurrentLocationReceived(location),
                        throwable -> getView().onCurrentLocationError(throwable));
    }

    public boolean isLocationSet() {
        return isLocationSet;
    }

    public void setLocation(boolean locationSet) {
        isLocationSet = locationSet;
    }

    public double getCurrentLat() {
        return currentLat;
    }

    public double getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLat(double currentLat) {
        this.currentLat = currentLat;
    }

    public void setCurrentLng(double currentLng) {
        this.currentLng = currentLng;
    }
}


