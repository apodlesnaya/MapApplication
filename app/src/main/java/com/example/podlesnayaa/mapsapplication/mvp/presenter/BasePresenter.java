package com.example.podlesnayaa.mapsapplication.mvp.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.podlesnayaa.mapsapplication.mvp.view.BaseView;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends BaseView> {

    @Nullable
    private WeakReference<V> viewWeakReference;

    public void attachView(V view) {
        viewWeakReference = new WeakReference<>(view);
        onAttachView(view);
    }

    public void detachView() {
        if (viewWeakReference != null && viewWeakReference.get() != null) {
            onDetachView(viewWeakReference.get());
        }
        viewWeakReference = null;
    }

    V getView() {
        if (viewWeakReference != null && viewWeakReference.get() != null) {
            return viewWeakReference.get();
        }
        return null;
    }

    protected void onAttachView(@NonNull V view) {

    }

    protected void onDetachView(@NonNull V view) {

    }
}
