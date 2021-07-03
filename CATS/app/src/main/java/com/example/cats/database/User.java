package com.example.cats.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "games_won")
    public Integer gamesWon;

    @ColumnInfo(name = "games_lost")
    public Integer gamesLost;

    @ColumnInfo(name = "generated_new_box")
    public Boolean newBox;

    @ColumnInfo(name = "avatar")
    public int avatar;

    @ColumnInfo(name = "body_upgraded")
    public Boolean bodyUpgraded;

    @Ignore
    public List<CarPart> availableParts;

    @Ignore
    public List<Box> boxes;

}