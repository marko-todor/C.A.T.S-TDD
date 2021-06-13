package com.example.cats.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cats.Car;

import java.util.List;

@Dao
public interface PartDao {

    @Query("SELECT * FROM carpart")
    List<CarPart> getAll();

    @Query("SELECT * FROM carpart WHERE pid IN (:partIds)")
    List<CarPart> loadAllByIds(int[] partIds);

    @Query("SELECT * FROM carpart WHERE part_resource_id LIKE :resource" +
            " LIMIT 1")
    CarPart findByResourceId(String resource);

    @Query("UPDATE carpart SET isPlaced = :placed WHERE part_resource_id=:pid AND userId =:uid")
    void updateIsPlaced(boolean placed, Integer pid, Integer uid);

    @Insert
    void insertAll(CarPart... parts);

    @Insert
    void insert(CarPart carPart);

    @Delete
    void delete(CarPart carPart);
}
