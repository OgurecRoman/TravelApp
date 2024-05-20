package com.example.myapplication.meteo;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class HttpsRequesr implements Runnable {
    static final String KEY = "d41281142baa45dd9bc133029231612";
    static final String APIREQUEST = "https://api.weatherapi.com/v1/current.json";
    static final String CITY = "Moscow";
    URL url;
    Handler handler;

    public HttpsRequesr(android.os.Handler handler, String city) {
        this.handler = handler;
        try {
            url = new URL(APIREQUEST + "?" + "q=" + city + "&key=" + KEY);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            Scanner in = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (in.hasNext()){
                response.append(in.nextLine());
            }
            in.close();
            connection.disconnect();

            Message msg = Message.obtain();
            msg.obj = response.toString();
            handler.sendMessage(msg);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
