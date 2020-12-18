package com.example.gametracker;

public class GameItem {
    private String mGameName;
    private String mPlayTime;
    private String mTimeLeft;
    private boolean mCompleted;


    public GameItem(String GameName, String PlayTime, String timeLeft, boolean completed){
        mGameName = GameName;
        mPlayTime = PlayTime;
        mTimeLeft = timeLeft;
        mCompleted = completed;
    }

    public String getGameName(){
        return mGameName;
    }

    public String getPlayTime(){
        return mPlayTime;
    }

    public boolean getCompleted(){ return mCompleted;}

    public void changeHoursPlayed(String name){
        mPlayTime = name + " hours";
    }
    public void setCompleted(boolean completed){
        mCompleted = completed;
    }

    public String getmTimeLeft() {
        return mTimeLeft;
    }

    public void setmTimeLeft(String mTimeLeft) {
        this.mTimeLeft = mTimeLeft;
    }
}
