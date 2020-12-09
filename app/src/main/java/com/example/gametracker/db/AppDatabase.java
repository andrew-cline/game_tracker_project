package com.example.gametracker.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.gametracker.Game;
import com.example.gametracker.User;
import com.example.gametracker.db.typeConverters.MapTypeConverter;

@Database(entities = {User.class, Game.class}, version = 1)
@TypeConverters(MapTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "GAMETRACKER_DB";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String GAME_TABLE = "GAME_TABLE";

    public abstract GameTrackerDAO getGameTrackerDAO();
}
