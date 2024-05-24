package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.meteo.MeteoService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherFragment extends Fragment {
    private static final String ARG_PARAM1 = "city";
    private static final String ARG_PARAM2 = "date";
    private String mParam_city;
    private String mParam_date;
    public String city_from_user;
    TextView name_view;
    TextView temp_max_view;
    TextView temp_min_view;
    TextView day_view;

    public String getCity_from_user() {
        return city_from_user;
    }

    public static WeatherFragment newInstance(String param_city, String param_date) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param_city);
        args.putString(ARG_PARAM2, param_date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam_city = getArguments().getString(ARG_PARAM1);
            mParam_date = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Intent intent = new Intent(getActivity(), MeteoService.class);
        getActivity().stopService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        name_view = view.findViewById(R.id.name);
        temp_max_view = view.findViewById(R.id.temp_max);
        temp_min_view = view.findViewById(R.id.temp_min);
        day_view = view.findViewById(R.id.day);

        Button btn_next = view.findViewById(R.id.next);
        btn_next.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_weatherFragment_to_LaguageFragment));

        Intent intent = new Intent(getActivity(), MeteoService.class);
        intent.putExtra("CITY", mParam_city);
        intent.putExtra("DATE", mParam_date);
        getActivity().startService(intent);

        getActivity().registerReceiver(receivar, new IntentFilter("MeteoService"),
                getActivity().RECEIVER_EXPORTED);

        return view;
    }

    BroadcastReceiver receivar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("RESULT", intent.getStringExtra("INFO"));
            String str = intent.getStringExtra("INFO");
            try {
                JSONObject start = new JSONObject(str);
                JSONObject location = start.getJSONObject("location");
                JSONArray forecast_array = start.getJSONObject("forecast")
                        .getJSONArray("forecastday");
                if (forecast_array.length() == 0){ name_view.setText("Так далеко мы не заглядываем :)"); return;}
                JSONObject forecast = forecast_array.getJSONObject(0);
                JSONObject day = forecast.getJSONObject("day");
                String name = location.getString("name");
                String date = forecast.getString("date");
                double temp_max = day.getDouble("maxtemp_c");
                double temp_min = day.getDouble("mintemp_c");
                temp_max_view.setText("" + temp_max);
                temp_min_view.setText("" + temp_min);
                name_view.setText("" + name);
                day_view.setText("" + date);
            } catch (JSONException e) {
                throw new RuntimeException(e);
                
            }
        }
    };
}