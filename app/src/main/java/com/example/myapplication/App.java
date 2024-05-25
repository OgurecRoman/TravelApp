package com.example.myapplication;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MapKitFactory.setApiKey("9f3ae9dc-9347-4d2b-8422-11b9ac3efad5");
        MapKitFactory.initialize(this);

    }
}
