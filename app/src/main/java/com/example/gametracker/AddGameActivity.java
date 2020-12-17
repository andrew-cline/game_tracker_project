package com.example.gametracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.gametracker.db.AppDatabase;
import com.example.gametracker.db.GameTrackerDAO;

import java.util.HashMap;

public class AddGameActivity extends AppCompatActivity {

    private EditText mGameNameEditText, mTimePlayedEditText;

    private Integer mUserId;

    private Switch mCompletedSwitch;

    private Button mAddGameButton;

    private Game mGame;

    private boolean mCompleted;
    private int mTimePlayed;
    private String mGameName;

    private GameTrackerDAO mGameTrackerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
        mGameNameEditText = findViewById(R.id.editTextGameName);
        mTimePlayedEditText = findViewById(R.id.editTextTimePlayed);
        mCompletedSwitch = findViewById(R.id.CompleteSwitch);
        mAddGameButton = findViewById(R.id.AddGameButtonConfirm);

        mUserId = getIntent().getIntExtra(MainActivity.USER_ID_KEY, -1);

        getDatabase();

        mAddGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                if(checkForGame()){
                    updateGameValues();
                }else{
                    insertIntoDatabase();
                    updateUserList();
                }
            }
        });

        mCompletedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCompleted = b;
            }
        });
    }

    private void updateUserList() {
        User mUser = mGameTrackerDAO.getUserByUserId(mUserId);
        HashMap<Integer, Pair<Integer>> mUserGameList = mUser.getUserGameList();
        int mGameId = mGameTrackerDAO.getGameByName(mGameName).getGameId();
        int temp = 0;
        if(mCompleted){
            temp = 1;
        }
        mUserGameList.put(mGameId, new Pair<Integer>(mTimePlayed, temp));
        mUser.setUserGameList(mUserGameList);
        mGameTrackerDAO.update(mUser);
    }

    private void updateGameValues() {
    }

    //returns true if game exists in db
    private boolean checkForGame() {
        mGame = mGameTrackerDAO.getGameByName(mGameName);
        return mGame != null;
    }

    private void getDatabase(){
        mGameTrackerDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).
                allowMainThreadQueries()
                .build()
                .getGameTrackerDAO();
    }

    private void insertIntoDatabase() {
        mGame = new Game(mGameName, 1);
        if(mCompleted){
            mGame.setAverageCompleteTime(mTimePlayed);
        }
        mGameTrackerDAO.insert(mGame);
    }

    private void getValuesFromDisplay() {
        mGameName = mGameNameEditText.getText().toString();
        mTimePlayed = -1;
        try{
            mTimePlayed = Integer.parseInt(mTimePlayedEditText.getText().toString());
        }catch (NumberFormatException e){
            Log.d("GAME", "Could not parse play time for insertion");
            return;
        }
        if(mTimePlayed == -1){
            Toast.makeText(AddGameActivity.this, "Something is wrong with time played", Toast.LENGTH_LONG).show();
        }
        if(mGameName.equals("")){
            Toast.makeText(AddGameActivity.this, "Invalid Game Name", Toast.LENGTH_LONG).show();
        }
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, LibraryActivity.class);
        intent.putExtra(MainActivity.USER_ID_KEY, userId);
        return intent;
    }
}