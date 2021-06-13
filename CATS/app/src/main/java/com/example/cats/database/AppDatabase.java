package com.example.cats.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, CarPart.class, Box.class}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PartDao partDao();
}