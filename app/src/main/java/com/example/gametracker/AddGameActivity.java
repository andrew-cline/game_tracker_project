package com.example.gametracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
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
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGameActivity extends AppCompatActivity {

    private EditText mGameNameEditText, mTimePlayedEditText;

    private Integer mUserId;

    private SwitchMaterial mCompletedSwitch;

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
                }
                updateUserList();
                Intent intent = LibraryActivity.intentFactory(AddGameActivity.this, mUserId);
                startActivity(intent);
            }
        });

        mCompletedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCompleted = b;
                if(mCompleted){
                    completeAlert();
                }
            }
        });
    }

    private void completeAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Warning");
        alert.setMessage("Once you mark a game as complete you can not update its play time.");
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    private void updateUserList() {
        User mUser = mGameTrackerDAO.getUserByUserId(mUserId);
        HashMap<Integer, Pair<Integer, Boolean>> mUserGameList = mUser.getUserGameList();
        int mGameId = mGameTrackerDAO.getGameByName(mGameName).getGameId();
        mUserGameList.put(mGameId, new Pair<>(mTimePlayed, mCompleted));
        mUser.setUserGameList(mUserGameList);
        mGameTrackerDAO.update(mUser);
    }

    private void updateGameValues() {
        mGame = mGameTrackerDAO.getGameByName(mGameName);
        mGame.setPlayerAmount(mGame.getPlayerAmount() + 1);
        ArrayList<Integer> tempList = mGame.getUserIdList();
        tempList.add(mUserId);
        mGame.setUserIdList(tempList);
        if(mCompleted){
            List<User> userList = mGameTrackerDAO.getUsersById(tempList);
            if(userList.size() > 0){
                int sum = 0;
                int size = userList.size();
                for(User user: userList){
                    if(user.getUserGameList().get(mGame.getGameId()).getP2()){
                        sum += user.getUserGameList().get(mGame.getGameId()).getP1();
                    }
                }
                mGame.setAverageCompleteTime((sum / size));
            }else{
                mGame.setAverageCompleteTime(mTimePlayed);
            }
        }
        mGameTrackerDAO.update(mGame);
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
        ArrayList<Integer> tempList = new ArrayList<>();
        tempList.add(mUserId);
        mGame.setUserIdList(tempList);
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
        Intent intent = new Intent(context, AddGameActivity.class);
        intent.putExtra(MainActivity.USER_ID_KEY, userId);
        return intent;
    }
}