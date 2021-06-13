package com.example.cats.database;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cats.Car;

import java.util.List;

@Dao
public abstract class UserDao {

    @Query("SELECT * FROM user")
    public abstract List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    public abstract List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE username LIKE :username" +
            " LIMIT 1")
    public abstract User findByName(String username);

    @Query("UPDATE user SET games_won = games_won + 1 WHERE username LIKE :username")
    public abstract void incGamesWon(String username);

    @Query("UPDATE user SET games_lost = games_lost + 1 WHERE username LIKE :username")
    public abstract void incGamesLost(String username);

    @Query("UPDATE user SET generated_new_box = :gen WHERE username LIKE :username")
    public abstract void updateGeneratedNewBox(String username, Boolean gen);

    @Delete
    public abstract void deleteBox(Box box);

    public void deleteUserBox(String username, int slot) {
         List<Box> boxes = getUsersBoxes(username);
         for(int i =0; i < boxes.size(); i++) {
             if(boxes.get(i).slotNumber == slot) {
                 deleteBox(boxes.get(i));
             }
         }
    }

    @Insert
    public abstract void insertAll(User... users);

    @Insert
    public abstract void insert(User user);

    @Query("SELECT * FROM CarPart WHERE userId =:userId")
    public abstract List<CarPart> getPartList(int userId);

    @Query("SELECT * FROM box WHERE userId =:userId")
    public abstract List<Box> getBoxList(int userId);

    @Query("UPDATE user SET body_upgraded = :upgraded WHERE username LIKE :username")
    public abstract void updateBodyUpgraded(String username, Boolean upgraded);

    @Insert
    public abstract void insertPartList(List<CarPart> parts);

    @Insert
    public abstract void insertBoxesList(List<Box> boxes);

    @Insert
    public abstract void insertBox(Box box);

    @Delete
    abstract public void delete(User user);

    public void addBox(String username, Box box) {
        User user = findByName(username);
        box.userId = user.uid;
        insertBox(box);
    }

    public void insertUserWithParts(User user, List<CarPart> parts) {
        insert(user);

        user = findByName(user.username);

        for (int i = 0; i < parts.size(); i++) {
            parts.get(i).userId = user.uid;
        }

        insertPartList(parts);


    }

    public User getUserWithParts(String username) {
        User user = findByName(username);
        List<CarPart> parts = getPartList(user.uid);
        user.availableParts = parts;
        return user;
    }

    public List<Box> getUsersBoxes(String username) {
        User user = findByName(username);
        List<Box> boxes = getBoxList(user.uid);
        user.boxes = boxes;
        return boxes;
    }

}
