package com.example.gametracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.gametracker.db.AppDatabase;

import java.util.HashMap;

@Entity(tableName = AppDatabase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUsername;
    private String mPassword;
    //hashmap of games where the gameID is the key and the time played is the value
    private HashMap<Integer, Pair<Integer, Boolean>> mUserGameList;
    //defaults to zero for normal access, 1 is admin level
    private boolean mAdmin;

    public User( String mUsername, String mPassword, boolean mAdmin) {
        this.mUsername = mUsername;
        this.mPassword = mPassword;
        this.mUserGameList = new HashMap<>();
        this.mAdmin = mAdmin;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public HashMap<Integer, Pair<Integer, Boolean>> getUserGameList() {
        return mUserGameList;
    }

    public void setUserGameList(HashMap<Integer, Pair<Integer, Boolean>> mUserGameList) {
        this.mUserGameList = mUserGameList;
    }

    public boolean isAdmin() {
        return mAdmin;
    }

    public void setAdmin(boolean mAdmin) {
        this.mAdmin = mAdmin;
    }
}
