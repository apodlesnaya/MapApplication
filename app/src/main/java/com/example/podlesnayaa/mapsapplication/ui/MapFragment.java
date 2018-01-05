package com.example.podlesnayaa.mapsapplication.ui;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.podlesnayaa.mapsapplication.R;
import com.example.podlesnayaa.mapsapplication.mvp.model.Point;
import com.example.podlesnayaa.mapsapplication.mvp.presenter.PointsPresenter;
import com.example.podlesnayaa.mapsapplication.mvp.view.PointsView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends Fragment implements PointsView,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener {

    private static final String KEY_IS_SHOWN = "isShown";
    private static final String KEY_TITLE_TEXT = "titleText";
    private static final String KEY_DESCRIPTION_TEXT = "descriptionText";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int MAP_ZOOM_LEVEL = 3;

    private GoogleMap map;

    private boolean isDescriptionShown;
    private int mainViewHeight;

    private PointsPresenter presenter;

    @BindView(R.id.map_view)
    MapView mapView;

    @BindView(R.id.description_layout)
    RelativeLayout descriptionLayout;

    @BindView(R.id.title_text)
    TextView titleText;

    @BindView(R.id.description_text)
    TextView descriptionText;


    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new PointsPresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        ButterKnife.bind(this, view);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mainViewHeight = metrics.heightPixels;

        if (isNetworkAvailable()) {
            if (savedInstanceState != null) {
                isDescriptionShown = savedInstanceState.getInt(KEY_IS_SHOWN) == 1;
                if (isDescriptionShown) {
                    showDescription();
                    titleText.setText(savedInstanceState.getString(KEY_TITLE_TEXT));
                    descriptionText.setText(savedInstanceState.getString(KEY_DESCRIPTION_TEXT));
                }
            }

            loadMap();
        } else {
            presenter.setLocation(false);
            showSnackbar();
        }
    }

    public void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    private void loadMap() {
        mapView.getMapAsync(this);
    }

    public void onStop() {
        super.onStop();
        presenter.detachView();

        if (isDescriptionShown) {
            hideDescription();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);

        if (!presenter.isLocationSet()) {
            getCurrentLocation();
        } else {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(presenter.getCurrentLat(), presenter.getCurrentLng()), MAP_ZOOM_LEVEL));
        }

        presenter.getPointsOnMap();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_IS_SHOWN, isDescriptionShown ? 1 : 0);
        outState.putString(KEY_TITLE_TEXT, titleText.getText().toString());
        outState.putString(KEY_DESCRIPTION_TEXT, descriptionText.getText().toString());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        showDescription();
        titleText.setText(marker.getTitle());
        descriptionText.setText(marker.getSnippet());

        presenter.setCurrentLat(marker.getPosition().latitude);
        presenter.setCurrentLng(marker.getPosition().longitude);
        presenter.setLocation(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), MAP_ZOOM_LEVEL));

        return true;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (isDescriptionShown) {
            hideDescription();
        }
    }

    @Override
    public void onPointsReceived(List<Point> points) {
        for (Point point : points) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(point.getLat(), point.getLng()))
                    .title(point.getTitle())
                    .snippet(point.getDescription());
            map.addMarker(markerOptions);
        }
    }

    @Override
    public void onPointsReceivedError(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCurrentLocationReceived(Location location) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), MAP_ZOOM_LEVEL));

        Toast.makeText(getContext(), R.string.map_hint, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCurrentLocationError(Throwable throwable) {
        Toast.makeText(getContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    presenter.getCurrentLocation();
                }
            }
        }
    }

    private void showDescription() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(descriptionLayout, "Y",
                mainViewHeight, mainViewHeight - getResources().getDimensionPixelSize(R.dimen.description_height));
        animator.start();
        isDescriptionShown = true;
    }

    private void hideDescription() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(descriptionLayout, "Y",
                mainViewHeight - getResources().getDimensionPixelSize(R.dimen.description_height), mainViewHeight);
        animator.start();
        isDescriptionShown = false;
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                presenter.getCurrentLocation();
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void showSnackbar() {
        Snackbar.make(mapView, R.string.no_internet_error, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.try_again_action), v -> {
                    if (isNetworkAvailable()) {
                        loadMap();
                    } else {
                        showSnackbar();
                    }
                }).show();
    }


    public boolean isNetworkAvailable() {
        if (getView() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getView().getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }
        return false;
    }
}
