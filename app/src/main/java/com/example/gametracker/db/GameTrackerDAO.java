package com.example.gametracker.db;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gametracker.User;

@Dao
public interface GameTrackerDAO {
    @Insert
    void insert(User... Users);

    @Update
    void update(User... Users);

    @Delete
    void delete(User User);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUsername = :username")
    User getUserByUsername(String username);
}
