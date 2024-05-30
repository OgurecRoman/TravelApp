package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data_base.DBHelper;
import com.example.myapplication.recycle_list.Elem;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final SQLiteDatabase database;

    ItemAdapter(Context context, SQLiteDatabase database) {
        this.inflater = LayoutInflater.from(context);
        this.database = database;
    }

    public int get_id(int pos){
        Cursor cursor = database.query(DBHelper.TABLE_CONSTANTS, null, null,
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
        Cursor cursor = database.query(DBHelper.TABLE_CONSTANTS, null, null,
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
    public ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        int id = get_id(position);
        Cursor cursor = get_elem(id);
        int name_id = cursor.getColumnIndex(DBHelper.KEY_NAME);
        int set_id = cursor.getColumnIndex(DBHelper.KEY_SET);
        String name = cursor.getString(name_id);
        boolean set = cursor.getInt(set_id) == 1;

        holder.but_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete(DBHelper.TABLE_CONSTANTS, "id = " + id, null);
                ItemAdapter.this.notifyDataSetChanged();
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                if (set) {
                    contentValues.put(DBHelper.KEY_SET, 0);
                    holder.editText.setPaintFlags(holder.editText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
                else {
                    contentValues.put(DBHelper.KEY_SET, 1);
                    holder.editText.setPaintFlags(holder.editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                database.update(DBHelper.TABLE_CONSTANTS, contentValues,
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
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, holder.editText.getText().toString());
                database.update(DBHelper.TABLE_CONSTANTS, contentValues,
                        "id = ?", new String[] { "" + id });
            }
        });

        holder.editText.setText(name);
        holder.checkBox.setChecked(set);
        if (set)
            holder.editText.setPaintFlags(holder.editText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else holder.editText.setPaintFlags(holder.editText.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
    }

    @Override
    public int getItemCount() {
        return (int) DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_CONSTANTS);
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
