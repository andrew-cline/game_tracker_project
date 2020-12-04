package com.example.gametracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Game {

    @PrimaryKey(autoGenerate = true)
    private int mGameId;

    private String mName;
    private double mAverageCompleteTime;
    private String mDescription;
    private int mPlayerAmount;
    //going unused until I find a nice way to implement it
    //Prob not the right way to do images, but this is how I did it in PHP
    private String mImageAddress;

    public Game(int mGameId, String mName, double mAverageCompleteTime, String mDescription, int mPlayerAmount, String mImageAddress) {
        this.mGameId = mGameId;
        this.mName = mName;
        this.mAverageCompleteTime = mAverageCompleteTime;
        this.mDescription = mDescription;
        this.mPlayerAmount = mPlayerAmount;
        this.mImageAddress = mImageAddress;
    }

    public int getGameId() {
        return mGameId;
    }

    public void setGameId(int mGameId) {
        this.mGameId = mGameId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public double getAverageCompleteTime() {
        return mAverageCompleteTime;
    }

    public void setAverageCompleteTime(double mAverageCompleteTime) {
        this.mAverageCompleteTime = mAverageCompleteTime;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public int getPlayerAmount() {
        return mPlayerAmount;
    }

    public void setPlayerAmount(int mPlayerAmount) {
        this.mPlayerAmount = mPlayerAmount;
    }

    public String getImageAddress() {
        return mImageAddress;
    }

    public void setImageAddress(String mImageAddress) {
        this.mImageAddress = mImageAddress;
    }
}
