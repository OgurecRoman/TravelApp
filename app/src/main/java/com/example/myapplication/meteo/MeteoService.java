package com.example.myapplication.meteo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.adapters.WeatherAdapter;

public class MeteoService extends Service {
    private final IBinder binder = new LocalBinder();

    Handler handler;

    public class LocalBinder extends Binder {
        public MeteoService getService() {
            return MeteoService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String city_name = intent.getStringExtra("city");
        String date = intent.getStringExtra("date");
        Integer id = intent.getIntExtra("id", 0);

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                String response = (String) msg.obj;
                Integer id = (int) msg.arg1;
                Intent intent = new Intent("MeteoService");
                intent.putExtra("INFO", response);
                intent.putExtra("ID", id);
                sendBroadcast(intent);
            }
        };

        Thread weatherThread = new Thread(new HttpsRequesr(handler, city_name, date, id));
        weatherThread.start();

        return START_NOT_STICKY;
    }

    public void sendData() {
        Intent intent = new Intent("MyServiceAction");
        intent.putExtra("key", "value");
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
