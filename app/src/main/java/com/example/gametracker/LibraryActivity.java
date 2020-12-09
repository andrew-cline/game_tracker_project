package com.example.gametracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gametracker.db.AppDatabase;
import com.example.gametracker.db.GameTrackerDAO;

public class LibraryActivity extends AppCompatActivity {

    private Integer mUserId;

    private GameTrackerDAO mGameTrackerDAO;

    private TextView mUserIdDisplay, mUsernameDisplay, mAdminDisplay;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mUserId = getIntent().getIntExtra(MainActivity.USER_ID_KEY, -1);

        mUserIdDisplay = findViewById(R.id.TextViewIdLibrary);
        mUsernameDisplay = findViewById(R.id.TextViewUsernameLibrary);
        mAdminDisplay = findViewById(R.id.TextViewAdminLibrary);

        getDatabase();
        
        init();

        storeUserData();
        Toast.makeText(this, "Logged in as " + mUser.getUsername(), Toast.LENGTH_SHORT).show();

        String idPlaceHolder = "User ID: " + mUser.getUserId();
        String usernamePlaceHolder = "Welcome " + mUser.getUsername();
        
        mUserIdDisplay.setText(idPlaceHolder);
        mUsernameDisplay.setText(usernamePlaceHolder);
        checkPrivilege();
    }

    private void storeUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(MainActivity.USER_ID_KEY, mUserId);

        editor.apply();
    }


    private void checkPrivilege() {
        if(mUser.isAdmin()){
            String adminPlaceHolder = mUser.getUsername() + " is an admin";
            mAdminDisplay.setText(adminPlaceHolder);
        }else{
            String adminPlaceHolder = mUser.getUsername() + " is not an admin";
            mAdminDisplay.setText(adminPlaceHolder);
        }
    }

    private void init() {
        mUser = mGameTrackerDAO.getUserByUserId(mUserId);
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
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
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