package com.example.myapplication.databases;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ListElem {
    @PrimaryKey int id;
    String name;
    Boolean on;
}
