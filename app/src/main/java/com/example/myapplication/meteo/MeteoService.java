package com.example.myapplication.meteo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MeteoService extends Service {
    private static final String TAG = "MeteoService";

    Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String city_name = intent.getStringExtra("city");
        String date = intent.getStringExtra("date");
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

        Thread weatherThread = new Thread(new HttpsRequesr(handler, city_name, date));
        weatherThread.start();

        return START_NOT_STICKY;
    }

    public static void startServiceWithParams(Context context, String city, String date) {
        // Создание Intent для запуска сервиса
        Intent intent = new Intent(context, MeteoService.class);
        intent.putExtra("city", city);
        intent.putExtra("date", date);

        // Запуск сервиса
        context.startService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
