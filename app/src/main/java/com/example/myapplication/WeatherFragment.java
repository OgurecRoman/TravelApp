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
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.meteo.MeteoService;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherFragment extends Fragment {
    private static final String ARG_PARAM1 = "city";
    private String mParam1;
    public String city_from_user;
    TextView name_view;
    TextView temp_view;
    TextView desc_view;
    TextView day_view;

    public String getCity_from_user() {
        return city_from_user;
    }

    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
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
        temp_view = view.findViewById(R.id.temp);
        desc_view = view.findViewById(R.id.desc);
        day_view = view.findViewById(R.id.day);

        city_from_user = mParam1;

        Button btn_next = view.findViewById(R.id.next);
        btn_next.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_weatherFragment_to_TODOFragment));

        getActivity().registerReceiver(receivar, new IntentFilter("MeteoService"),
                getActivity().RECEIVER_EXPORTED);

        Intent intent = new Intent(getActivity(), MeteoService.class);
        getActivity().startService(intent);

        return view;
    }

    BroadcastReceiver receivar = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("RESULT", intent.getStringExtra("INFO"));
            String str = intent.getStringExtra("INFO");
            try {
                JSONObject start = new JSONObject(str);
                JSONObject current = start.getJSONObject("current");
                JSONObject location = start.getJSONObject("location");
                double temp = current.getDouble("temp_c");
                String name = location.getString("name");
                String day = location.getString("localtime");
                temp_view.setText("" + temp);
                name_view.setText("" + name);
                day_view.setText("" + day);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    };
}