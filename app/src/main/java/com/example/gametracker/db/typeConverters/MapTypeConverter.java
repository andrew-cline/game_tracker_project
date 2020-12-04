package com.example.gametracker.db.typeConverters;

import androidx.room.TypeConverter;

import com.example.gametracker.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

//no idea if this works but I got this code from
//https://medium.com/@amit.bhandari/storing-java-objects-other-than-primitive-types-in-room-database-11e45f4f6d22

public class MapTypeConverter {
    @TypeConverter
    public static String fromMap(HashMap<Game, Integer> list){
        Gson gson = new Gson();

        String json = gson.toJson(list);

        return json;
    }

    @TypeConverter
    public static HashMap<Game, Integer> fromString(String value){
        Type listType = new TypeToken<HashMap<Game, Integer>>(){}.getType();

        return new Gson().fromJson(value, listType);
    }
}
