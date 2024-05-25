package com.example.myapplication.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ListElem.class}, version = 1)
public abstract class ElemsDatabase extends RoomDatabase {
    public abstract ListElemDAO getListElemDAO();
}
