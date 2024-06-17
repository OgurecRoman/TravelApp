package com.example.myapplication;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.example.myapplication.adapters.WeatherAdapter;
import com.example.myapplication.data_base.DBHelper;
import com.example.myapplication.meteo.MeteoService;
import com.yandex.mapkit.MapKitFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class App extends Application {
    private MeteoService meteoService;
    private DBHelper dbHelper;

    public static SQLiteDatabase database;
    private ContentValues contentValues_name;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MeteoService.LocalBinder binder = (MeteoService.LocalBinder) service;
            meteoService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    BroadcastReceiver receivar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            contentValues_name = new ContentValues();
            Log.d("RESULT", intent.getStringExtra("INFO"));
            String str = intent.getStringExtra("INFO");
            int id = intent.getIntExtra("ID", 0);
            if (Objects.equals(str, "ERROR")) {
                return;
            }
            try {
                JSONObject start = new JSONObject(str);
                JSONObject current = start.getJSONObject("current");
                JSONArray forecast_array = start.getJSONObject("forecast")
                        .getJSONArray("forecastday");
                if (forecast_array.length() == 0){
                    return;
                }
                JSONObject forecast = forecast_array.getJSONObject(0);
                JSONObject day = forecast.getJSONObject("day");
                String url_img = "https:" + current.getJSONObject("condition").getString("icon");
                double temp_max = day.getDouble("maxtemp_c");
                double temp_min = day.getDouble("mintemp_c");

                contentValues_name.put(DBHelper.KEY_MAX_TEMP, temp_max);
                contentValues_name.put(DBHelper.KEY_MIN_TEMP, temp_min);
                contentValues_name.put(DBHelper.KEY_URL_IMG, url_img);
                database.update(DBHelper.TABLE_CITIES, contentValues_name,
                        "id = ?", new String[]{"" + id});
                Log.d("RRR", "записали в бд " + id +
                        " " + temp_max + " " + temp_min);
                meteoService.sendData();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };
    @Override
    public void onCreate() {
        super.onCreate();

        MapKitFactory.setApiKey("9f3ae9dc-9347-4d2b-8422-11b9ac3efad5");
        MapKitFactory.initialize(this);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        registerReceiver(receivar, new IntentFilter("MeteoService"), RECEIVER_EXPORTED);

        Intent intent = new Intent(this, MeteoService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

    }
}
