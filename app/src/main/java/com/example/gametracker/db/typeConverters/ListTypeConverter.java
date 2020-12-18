package com.example.gametracker.db.typeConverters;

import androidx.room.TypeConverter;

import com.example.gametracker.Pair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class ListTypeConverter {

    @TypeConverter
    public static String fromList(ArrayList<Integer> list){
        Gson gson = new Gson();

        String json = gson.toJson(list);

        return json;
    }

    @TypeConverter
    public static ArrayList<Integer> fromString(String value){
        Type listType = new TypeToken<ArrayList<Integer>>(){}.getType();

        return new Gson().fromJson(value, listType);
    }
}
