package com.example.podlesnayaa.mapsapplication;


import android.app.Application;

import com.example.podlesnayaa.mapsapplication.di.AppComponent;
import com.example.podlesnayaa.mapsapplication.di.DaggerAppComponent;
import com.example.podlesnayaa.mapsapplication.di.modules.RetrofitModule;

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .retrofitModule(new RetrofitModule())
                .build();
    }

    public static AppComponent appComponent() {
        return appComponent;
    }

}
