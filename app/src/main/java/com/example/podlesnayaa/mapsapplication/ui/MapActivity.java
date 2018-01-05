package com.example.podlesnayaa.mapsapplication.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.podlesnayaa.mapsapplication.R;

public class MapActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (savedInstanceState == null) {
            MapFragment mapFragment = new MapFragment();
            mapFragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_fragment, mapFragment)
                    .commitAllowingStateLoss();
        }

    }


}
