package com.example.cats.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;

@Entity
public class Box {

    @PrimaryKey(autoGenerate = true)
    public int bid;

    @ColumnInfo(name = "time_to_open")
    public String timeToOpen;

    @ColumnInfo(name = "slot_number")
    public Integer slotNumber;

    public long userId;



}
