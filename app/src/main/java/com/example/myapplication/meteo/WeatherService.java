package com.example.myapplication.meteo;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.myapplication.adapters.WeatherAdapter;
import com.example.myapplication.data_base.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class WeatherService extends Service {
    static final String KEY = "d41281142baa45dd9bc133029231612";
    static final String APIREQUEST = "https://api.weatherapi.com/v1/forecast.json";
    private ContentValues contentValues_name;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    URL url;
    private static final String TAG = "WeatherService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("RRR", "СЕРВИС11111111");
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Получение города из Intent
        Log.d("RRR", "СЕРВИС");
        String city = intent.getStringExtra("city");
        String date = intent.getStringExtra("date");
        Log.d("RRR", "получили из intent " + city + " " + date);

//        dbHelper = new DBHelper(intent);
//        database = dbHelper.getWritableDatabase();
//
//        Thread weatherThread = new Thread(new HttpsRequesr(city, date));
//        weatherThread.start();

        // Парсинг погоды для данного города
        JSONObject weatherData = fetchWeatherData(city, date);
        Log.d("RRR", "спарсили погоду " + city + " " + date);

        // Сохранение данных погоды в базу данных
        saveWeatherData(weatherData, city, date);

        // Завершение работы сервиса
        stopSelf(startId);
        return START_NOT_STICKY;
    }

    private JSONObject fetchWeatherData(String city, String date) {

        try {
            url = new URL(APIREQUEST + "?" + "q=" + city + "&dt=" + date + "&key=" + KEY);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            Scanner in = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (in.hasNext()){
                response.append(in.nextLine());
            }
            in.close();
            connection.disconnect();

            return new JSONObject(response.toString());

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveWeatherData(JSONObject start, String city, String date) {
        JSONObject current = null;
        try {
            current = start.getJSONObject("current");
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
                    "city = ? and date = ?", new String[]{city, date});
            Log.d("RRR", "записали в бд " + temp_max + " " + temp_min);}
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
