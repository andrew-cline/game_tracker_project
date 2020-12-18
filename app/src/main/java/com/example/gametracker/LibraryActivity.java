package com.example.gametracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gametracker.db.AppDatabase;
import com.example.gametracker.db.GameTrackerDAO;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryActivity extends AppCompatActivity {

    private ArrayList<GameItem> gameList = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private LibraryRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Integer mUserId;

    private GameTrackerDAO mGameTrackerDAO;

    private Button addGameButton;
    private Button mAdminButton;
    private Button mDeleteAccountButton;

    private User mUser;

    private boolean mCompleted;
    private boolean mIsAdmin;

    private Game mCurrentGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mUserId = getIntent().getIntExtra(MainActivity.USER_ID_KEY, -1);

        addGameButton = findViewById(R.id.libraryAddGameButton);
        mAdminButton = findViewById(R.id.AdminButton);
        mDeleteAccountButton = findViewById(R.id.DeleteAccountButton);

        getDatabase();
        
        init();

        storeUserData();
        Toast.makeText(this, "Logged in as " + mUser.getUsername(), Toast.LENGTH_SHORT).show();

        checkPrivilege();


        HashMap<Integer, Pair<Integer, Boolean>> userGameMap = mUser.getUserGameList();
        for(Map.Entry<Integer, Pair<Integer, Boolean>> Game : userGameMap.entrySet()){
            Game tempGame = mGameTrackerDAO.getGameById((int)Game.getKey());
            String tempName = tempGame.getName();
            Pair<Integer, Boolean> tempPair = Game.getValue();
            String playTimeString = tempPair.getP1().toString() + " hours";
            double timeLeft = tempGame.getAverageCompleteTime() - tempPair.getP1();
            String timeLeftString = "Something is wrong";
            if(timeLeft > 0){
                timeLeftString = "~" + timeLeft + " hours left";
            }else{
                timeLeftString = "Not enough data";
            }
            gameList.add(new GameItem(tempName, playTimeString, timeLeftString, tempPair.getP2()));
        }

        mRecyclerView = findViewById(R.id.libraryRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new LibraryRecyclerAdapter(gameList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new LibraryRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mCurrentGame = mGameTrackerDAO.getGameByName(gameList.get(position).getGameName());
                changeHoursAlert(position);
            }
        });

        addGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddGameActivity.intentFactory(LibraryActivity.this, mUserId);
                startActivity(intent);
            }
        });
        mAdapter.setOnItemLongClickListener(new LibraryRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onLongItemClick(int position) {
                mCurrentGame = mGameTrackerDAO.getGameByName(gameList.get(position).getGameName());
                if(!mUser.getUserGameList().get(mCurrentGame.getGameId()).getP2()){
                    removeGameAlert(position);
                    gameList.remove(position);
                }else{
                    toastMaker("Can not remove games that are completed", Toast.LENGTH_LONG);
                }
            }
        });
        mDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccountAlert();
            }
        });
        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = AdminActivity.intentFactory(LibraryActivity.this, mUserId);
                startActivity(intent);
            }
        });
    }

    private void deleteAccountAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Delete Account?");
        alert.setMessage("Would you like to delete your account (No recovery possible)");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mUserId = -1;
                storeUserData();
                deleteAccount();
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    private void deleteAccount() {
        HashMap<Integer, Pair<Integer, Boolean>> deleteList = mUser.getUserGameList();
        for(Map.Entry<Integer, Pair<Integer, Boolean>> gameEntry : deleteList.entrySet()){
            Game tempGame = mGameTrackerDAO.getGameById(gameEntry.getKey());
            ArrayList<Integer> tempGameList = tempGame.getUserIdList();
            tempGameList.remove(mUserId);
            tempGame.setUserIdList(tempGameList);
            mGameTrackerDAO.update(tempGame);
        }
        mGameTrackerDAO.delete(mUser);
        Intent intent = MainActivity.intentFactory(LibraryActivity.this, -1);
        startActivity(intent);
    }

    private void toastMaker(String s, int length) {
        Toast.makeText(this, s, length).show();
    }

    private void removeGameAlert(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Warning");
        alert.setMessage("Do you want to remove this game from your library? (Won't be removed from player statistics");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                removeGameFromUserList(position);
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    private void removeGameFromUserList(int position) {
        HashMap<Integer, Pair<Integer, Boolean>> userGameList = mUser.getUserGameList();
        userGameList.remove(mCurrentGame.getGameId());
        mUser.setUserGameList(userGameList);
        mGameTrackerDAO.update(mUser);
        mAdapter.notifyItemRemoved(position);
    }


    private void storeUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(MainActivity.USER_ID_KEY, mUserId);

        editor.apply();
    }


    //pulled some code from this thread
    //https://stackoverflow.com/questions/18799216/how-to-make-a-edittext-box-in-a-dialog
    private void changeHoursAlert(final int position){
        if(mUser.getUserGameList().get(mCurrentGame.getGameId()).getP2()){
            toastMaker("Can not changed time on completed game", Toast.LENGTH_SHORT);
            return;
        }
        final View view = getLayoutInflater().inflate(R.layout.alert_update_game_layout, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText mTimePlayedEditText = (EditText) view.findViewById(R.id.editTextLibraryTimeUpdate);
        final SwitchMaterial mCompletedSwitch = (SwitchMaterial) view.findViewById(R.id.CompleteSwitchLibrary);
        alert.setMessage("Enter new time played");
        alert.setTitle("Update " + mCurrentGame.getName() );
        mCompletedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCompleted = b;
            }
        });
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!mTimePlayedEditText.getText().toString().equals("")){
                    int timePlayed = Integer.parseInt(mTimePlayedEditText.getText().toString());
                    if(timePlayed > mUser.getUserGameList().get(mCurrentGame.getGameId()).getP1()) {
                        updateUserInfo(Integer.parseInt(mTimePlayedEditText.getText().toString()));
                        updateGameInfo();
                        gameList.get(position).changeHoursPlayed(mTimePlayedEditText.getText().toString());
                        gameList.get(position).setCompleted(mCompleted);
                        mAdapter.notifyItemChanged(position);
                    }
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setView(view);

        alert.show();

    }

    private void updateUserInfo(int timePlayed) {
        HashMap<Integer, Pair<Integer, Boolean>> userGameListUser = mUser.getUserGameList();
        userGameListUser.put(mCurrentGame.getGameId(), new Pair<Integer, Boolean>(timePlayed, mCompleted));
        mUser.setUserGameList(userGameListUser);
        mGameTrackerDAO.update(mUser);
    }

    private void updateGameInfo() {
        ArrayList<Integer> tempList = mCurrentGame.getUserIdList();
        tempList.add(mUserId);
        mCurrentGame.setUserIdList(tempList);
        if(mCompleted){
            List<User> userList = mGameTrackerDAO.getUsersById(tempList);
            if(userList.size() > 0){
                int sum = 0;
                int size = userList.size();
                for(User user: userList){
                    if(user.getUserGameList().get(mCurrentGame.getGameId()).getP2()){
                        sum += user.getUserGameList().get(mCurrentGame.getGameId()).getP1();
                    }
                }
                mCurrentGame.setAverageCompleteTime((sum / size));
            }
        }
        mGameTrackerDAO.update(mCurrentGame);
    }


    private void checkPrivilege() {

    }

    private void init() {
        mUser = mGameTrackerDAO.getUserByUserId(mUserId);
        mIsAdmin = mUser.isAdmin();
        if(mIsAdmin){
            mAdminButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed(){
        logoutAlert();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logged_in_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutMenuButton:
                logoutAlert();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logoutAlert() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setMessage("Logout of session?");

        alertBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logout();
                    }
                });
        alertBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        alertBuilder.setCancelable(true);

        alertBuilder.show();
    }

    private void logout() {
        mUserId = -1;
        storeUserData();
        toastMaker("Logged out", Toast.LENGTH_SHORT);
        Intent intent = MainActivity.intentFactory(LibraryActivity.this, -1);
        startActivity(intent);
        finish();
    }

    private void getDatabase(){
        mGameTrackerDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).
                allowMainThreadQueries()
                .build()
                .getGameTrackerDAO();
    }



    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, LibraryActivity.class);
        intent.putExtra(MainActivity.USER_ID_KEY, userId);
        return intent;
    }
}