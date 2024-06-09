package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.adapters.ItemAdapter;
import com.example.myapplication.adapters.WeatherAdapter;
import com.example.myapplication.data_base.DBHelper;
import com.example.myapplication.meteo.MeteoService;
import com.example.myapplication.meteo.WeatherService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherFragment extends Fragment {
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    RecyclerView recyclerView;
    private int id;
    private ArrayList<Intent> intents = new ArrayList<>();

    public static Boolean update = false;
    private Integer count = 0;

    public static WeatherFragment newInstance() {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        Intent intent = new Intent(getActivity(), MeteoService.class);
        getActivity().stopService(intent);
    }

    public void show(){
        Cursor cursor = database.query(DBHelper.TABLE_CITIES, null, null,
                null, null, null, null);
        Log.d("RRR", "------НАЧАЛО------");
        if (cursor.moveToFirst()){
            Log.d("RRR", "У нас есть " + cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID))
                    + " " + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CITY))
                    + " " + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DATE))
                    + " " + cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MAX_TEMP))
                    + " " + cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MIN_TEMP)));
            while (cursor.moveToNext()){
                Log.d("RRR", "У нас есть " + cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID))
                        + " " + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CITY))
                        + " " + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DATE))
                        + " " + cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MAX_TEMP))
                        + " " + cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MIN_TEMP)));
            }
        }
        cursor.close();
        Log.d("RRR", "------КОНЕЦ------");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        String city, date;

        dbHelper = new DBHelper(getActivity());
        database = dbHelper.getWritableDatabase();

        show();

        Cursor cursor = database.query(DBHelper.TABLE_CITIES, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID));
                city = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_CITY));
                date = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DATE));
                Log.d("RRR", "вызываю update для " + id + " " + city + " " + date);

                Intent intent = new Intent(getActivity(), MeteoService.class);
                intent.putExtra("city", city);
                intent.putExtra("date", date);
                intent.putExtra("id", id);
                getActivity().startService(intent);
            } while (cursor.moveToNext());
        }

        cursor.close();

        recyclerView = view.findViewById(R.id.list_weather);
        WeatherAdapter adapter = new WeatherAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        Button btn_next = view.findViewById(R.id.next);
        btn_next.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_weatherFragment_to_LaguageFragment));

        return view;
    }
}