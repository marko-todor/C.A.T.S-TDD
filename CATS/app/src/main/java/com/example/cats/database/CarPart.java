package com.example.cats.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CarPart {


    @PrimaryKey(autoGenerate = true)
    public int pid;

    @ColumnInfo(name = "part_resource_id")
    public Integer partResourceId;

    @ColumnInfo(name = "part_info_resource_id")
    public Integer partInfoResourceId;

    @ColumnInfo(name = "health")
    public Integer health;

    @ColumnInfo(name = "energy")
    public Integer energy;

    @ColumnInfo(name = "power")
    public Integer power;

    public long userId;

    public boolean isPlaced;
}