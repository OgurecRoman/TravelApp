package com.example.myapplication.adapters;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data_base.DBHelper;
import com.example.myapplication.meteo.MeteoService;
import com.squareup.picasso.Picasso;

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

    public void update_weather(WeatherAdapter.ViewHolder holder, int id){
        holder.name_text.setText(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_NAME)));
        holder.temp_max_text.setText(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_MAX_TEMP)));
        holder.temp_min_text.setText(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_MIN_TEMP)));
        holder.day_text.setText(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_DAY)));
        holder.month_text.setText(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_MONTH)));
        Picasso.get().load(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_URL_IMG)))
                .into(holder.image);
        Log.d("RRR", "утт " + get_elem(id).getColumnIndex(DBHelper.KEY_NAME)
                + " " + get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_MAX_TEMP)));
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

        holder.name_text.setText(city);
        holder.temp_max_text.setText(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_MAX_TEMP)));
        holder.temp_min_text.setText(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_MIN_TEMP)));
        holder.day_text.setText(day_imp);
        holder.month_text.setText(month);
        Picasso.get().load(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_URL_IMG)))
                .into(holder.image);
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
        final ImageView image;
        ViewHolder(View view){
            super(view);
            name_text = view.findViewById(R.id.name);
            temp_max_text = view.findViewById(R.id.temp_max);
            temp_min_text = view.findViewById(R.id.temp_min);
            day_text = view.findViewById(R.id.day);
            month_text = view.findViewById(R.id.month);
            image = view.findViewById(R.id.image);
        }
    }
}
