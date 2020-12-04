package com.example.gametracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gametracker.db.AppDatabase;
import com.example.gametracker.db.GameTrackerDAO;

public class MainActivity extends AppCompatActivity {

    public static final String USER_ID_KEY = "USER_ID_KEY";
    public static final String SHARED_PREFS = "sharedPrefs";

    private int mUserId = -1;

    Button mLoginButton;
    Button mCreateAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkForUser();

        mLoginButton = findViewById(R.id.loginButton);
        mCreateAccountButton = findViewById(R.id.createAccountButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.intentFactory(MainActivity.this);
                startActivity(intent);
            }
        });
        mCreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkForUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        mUserId = sharedPreferences.getInt(USER_ID_KEY, -1);
        if(mUserId != -1){
            Intent intent = LibraryActivity.intentFactory(MainActivity.this, mUserId);
            startActivity(intent);
        }
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}