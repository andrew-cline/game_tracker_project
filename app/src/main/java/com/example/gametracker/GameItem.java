package com.example.gametracker;

public class GameItem {
    private String mGameName;
    private String mPlayTime;

    public GameItem(String GameName, String PlayTime){
        mGameName = GameName;
        mPlayTime = PlayTime;
    }

    public String getGameName(){
        return mGameName;
    }

    public String getPlayTime(){
        return mPlayTime;
    }
}
