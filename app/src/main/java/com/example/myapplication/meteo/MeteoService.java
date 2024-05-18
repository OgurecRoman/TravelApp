package com.example.myapplication.meteo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MeteoService extends Service {

    Handler handler;

    String city;

    public MeteoService(String city) {
        this.city = city;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String response = (String) msg.obj;
                Intent intent = new Intent("MeteoService");
                intent.putExtra("INFO", response);
                sendBroadcast(intent);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread weatherThread = new Thread(new HttpsRequesr(handler));
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
