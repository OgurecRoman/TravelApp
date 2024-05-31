package com.example.myapplication.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data_base.DBHelper;
import com.example.myapplication.meteo.MeteoService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder>{
    private final Context context;
    private final LayoutInflater inflater;
    private final SQLiteDatabase database;

    public WeatherAdapter(Context context, SQLiteDatabase database) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.database = database;
    }

    public int get_id(int pos){
        Cursor cursor = database.query(DBHelper.TABLE_CITIES, null, null,
                null, null, null, null);

        int count = 0;
        if (cursor.moveToFirst()){
            while (count != pos){
                cursor.moveToNext();
                count++;
            }
        }
        int name_id = cursor.getColumnIndex(DBHelper.KEY_ID);
        int id = cursor.getInt(name_id);
        cursor.close();
        return id;
    }

    public Cursor get_elem(int id){
        Cursor cursor = database.query(DBHelper.TABLE_CITIES, null, null,
                null, null, null, null);
        if (cursor.moveToFirst()){
            int find_id = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID));
            while (find_id != id){
                cursor.moveToNext();
                find_id = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID));
            }
        }
        return cursor;
    }

    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.weather_item, parent, false);
        return new WeatherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherAdapter.ViewHolder holder, int position) {
        int id = get_id(position);
        String city = get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_CITY));
        String date = get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_DATE));
        String day_imp = get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_DAY));
        String month = get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_MONTH));

        BroadcastReceiver receivar = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("RESULT", intent.getStringExtra("INFO"));
                String str = intent.getStringExtra("INFO");
                if (Objects.equals(str, "ERROR")) {
                    holder.name_text.setText("Такого города не знаю :(");
                    return;
                }
                try {
                    JSONObject start = new JSONObject(str);
                    JSONObject location = start.getJSONObject("location");
                    JSONArray forecast_array = start.getJSONObject("forecast")
                            .getJSONArray("forecastday");
                    if (forecast_array.length() == 0){
                        holder.name_text.setText("В прошлое и далекое будущее не заглядываем :)");
                        return;
                    }
                    JSONObject forecast = forecast_array.getJSONObject(0);
                    JSONObject day = forecast.getJSONObject("day");
                    double temp_max = day.getDouble("maxtemp_c");
                    double temp_min = day.getDouble("mintemp_c");

                    holder.temp_max_text.setText("" + temp_max);
                    holder.temp_min_text.setText("" + temp_min);
                    holder.name_text.setText(city);
                    holder.day_text.setText(day_imp);
                    holder.month_text.setText(month);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Intent intent = new Intent(context, MeteoService.class);
        if (city.isEmpty()) {
            holder.name_text.setText("Введите город");
            return;
        }
        intent.putExtra("CITY", city);
        intent.putExtra("DATE", date);
        context.startService(intent);

        context.registerReceiver(receivar, new IntentFilter("MeteoService"),
                context.RECEIVER_EXPORTED);

    }

    @Override
    public int getItemCount() {
        return (int) DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_CITIES);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name_text;
        final TextView temp_max_text;
        final TextView temp_min_text;
        final TextView day_text;
        final TextView month_text;
        ViewHolder(View view){
            super(view);
            name_text = view.findViewById(R.id.name);
            temp_max_text = view.findViewById(R.id.temp_max);
            temp_min_text = view.findViewById(R.id.temp_min);
            day_text = view.findViewById(R.id.day);
            month_text = view.findViewById(R.id.month);
        }
    }
}
