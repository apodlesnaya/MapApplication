package com.example.podlesnayaa.mapsapplication.di;

import com.example.podlesnayaa.mapsapplication.di.modules.RetrofitModule;
import com.example.podlesnayaa.mapsapplication.mvp.presenter.PointsPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RetrofitModule.class})
public interface AppComponent {

    void inject(PointsPresenter presenter);

}
