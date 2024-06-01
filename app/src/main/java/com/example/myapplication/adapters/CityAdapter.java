package com.example.myapplication.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data_base.DBHelper;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{
    private final Context context;
    private final LayoutInflater inflater;
    private final SQLiteDatabase database;
    private String[] list_month = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля",
            "августа", "сентября", "октября", "ноября", "декабря"};

    public CityAdapter(Context context, SQLiteDatabase database) {
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
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.city_item, parent, false);
        return new CityAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityAdapter.ViewHolder holder, int position) {
        int id = get_id(position);
        String day = get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_DAY));
        String month = get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_MONTH));

        holder.but_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete(DBHelper.TABLE_CITIES, "id = " + id, null);
                CityAdapter.this.notifyDataSetChanged();
            }
        });

        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues_name = new ContentValues();
                if (!(get_elem(id).getInt(get_elem(id).getColumnIndex(DBHelper.KEY_SET)) == 1)){
                    contentValues_name.put(DBHelper.KEY_SET, 1);
                    holder.date.setBackgroundColor(context.getResources().getColor(R.color.set));
                }else{
                    contentValues_name.put(DBHelper.KEY_SET, 0);
                    holder.date.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
                database.update(DBHelper.TABLE_CITIES, contentValues_name,
                        "id = ?", new String[] { "" + id });
            }
        });

        holder.city_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ContentValues contentValues_name = new ContentValues();
                contentValues_name.put(DBHelper.KEY_CITY, holder.city_text.getText().toString());
                database.update(DBHelper.TABLE_CITIES, contentValues_name,
                        "id = ?", new String[] { "" + id });
            }
        });

        holder.city_text.setText(get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_CITY)));
        holder.day_text.setText(day);
        holder.month_text.setText(month);
        if (get_elem(id).getInt(get_elem(id).getColumnIndex(DBHelper.KEY_SET)) == 1)
            holder.date.setBackgroundColor(context.getResources().getColor(R.color.set));
        else
            holder.date.setBackgroundColor(context.getResources().getColor(R.color.white));
    }

    @Override
    public int getItemCount() {
        return (int) DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_CITIES);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final EditText city_text;
        final TextView day_text;
        final TextView month_text;
        final ImageButton but_remove;
        final LinearLayout date;
        ViewHolder(View view){
            super(view);
            city_text = view.findViewById(R.id.city);
            day_text = view.findViewById(R.id.day);
            month_text = view.findViewById(R.id.month);
            but_remove = view.findViewById(R.id.but_remove);
            date = view.findViewById(R.id.date);
        }
    }
}
