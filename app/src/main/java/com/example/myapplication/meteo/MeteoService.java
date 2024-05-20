package com.example.myapplication.meteo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MeteoService extends Service {

    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String city_name = intent.getStringExtra("CITY");
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String response = (String) msg.obj;
                Intent intent = new Intent("MeteoService");
                intent.putExtra("INFO", response);
                intent.putExtra("CITY", city_name);
                sendBroadcast(intent);
            }
        };

        Thread weatherThread = new Thread(new HttpsRequesr(handler, city_name));
        weatherThread.start();

        Toast.makeText(this, "Сервис создан", Toast.LENGTH_SHORT).show();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Сервис уничтожен", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
