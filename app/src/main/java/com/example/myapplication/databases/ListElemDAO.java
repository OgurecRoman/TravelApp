package com.example.myapplication.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListElemDAO {
    @Insert
    void insertAll(ListElem... elems);

    @Delete
    void delete(ListElem elem);

    // Получение всех Person из бд
    @Query("SELECT * FROM listelem")
    List<ListElem> getAllPeople();
}
