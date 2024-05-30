package com.example.myapplication.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data_base.DBHelper;

public class DoingAdapter extends RecyclerView.Adapter<DoingAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final SQLiteDatabase database;

    public DoingAdapter(Context context, SQLiteDatabase database) {
        this.inflater = LayoutInflater.from(context);
        this.database = database;
    }

    public int get_id(int pos){
        Cursor cursor = database.query(DBHelper.TABLE_DOING, null, null,
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
        Cursor cursor = database.query(DBHelper.TABLE_DOING, null, null,
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
    public DoingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new DoingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoingAdapter.ViewHolder holder, int position) {
        int id = get_id(position);
        String name = get_elem(id).getString(get_elem(id).getColumnIndex(DBHelper.KEY_NAME));

        holder.but_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete(DBHelper.TABLE_DOING, "id = " + id, null);
                DoingAdapter.this.notifyDataSetChanged();
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues_set = new ContentValues();
                if (get_elem(id).getInt(get_elem(id).getColumnIndex(DBHelper.KEY_SET)) == 1) {
                    contentValues_set.put(DBHelper.KEY_SET, 0);
                    holder.editText.setPaintFlags(holder.editText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
                else {
                    contentValues_set.put(DBHelper.KEY_SET, 1);
                    holder.editText.setPaintFlags(holder.editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                }
                database.update(DBHelper.TABLE_DOING, contentValues_set,
                        "id = ?", new String[] { "" + id });
            }
        });
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ContentValues contentValues_name = new ContentValues();
                contentValues_name.put(DBHelper.KEY_NAME, holder.editText.getText().toString());
                database.update(DBHelper.TABLE_DOING, contentValues_name,
                        "id = ?", new String[] { "" + id });
            }
        });

        holder.editText.setText(name);
        holder.checkBox.setChecked(get_elem(id).getInt(get_elem(id).getColumnIndex(DBHelper.KEY_SET)) == 1);
        if (get_elem(id).getInt(get_elem(id).getColumnIndex(DBHelper.KEY_SET)) == 1)
            holder.editText.setPaintFlags(holder.editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else holder.editText.setPaintFlags(holder.editText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
    }

    @Override
    public int getItemCount() {
        return (int) DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_DOING);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final EditText editText;
        final CheckBox checkBox;
        final ImageButton but_remove;
        ViewHolder(View view){
            super(view);
            editText = view.findViewById(R.id.edittext);
            checkBox = view.findViewById(R.id.checkBox);
            but_remove = view.findViewById(R.id.but_remove);
        }
    }
}
