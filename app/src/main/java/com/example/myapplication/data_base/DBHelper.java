package com.example.myapplication.data_base;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication.recycle_list.Elem;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "travellingBD";
    public static final String TABLE_CONSTANTS = "list_laguage";
    public static final String TABLE_DOING = "list_doing";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SET = "setting";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CONSTANTS + "(" + KEY_ID +
                " integer primary key," + KEY_NAME + " text," + KEY_SET + " integer" + ")");

        db.execSQL("CREATE TABLE " + TABLE_DOING + "(" + KEY_ID +
                " integer primary key," + KEY_NAME + " text," + KEY_SET + " integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSTANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOING);

        onCreate(db);
    }
}
