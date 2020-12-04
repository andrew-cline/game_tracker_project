package com.example.gametracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gametracker.db.AppDatabase;
import com.example.gametracker.db.GameTrackerDAO;

public class LoginActivity extends AppCompatActivity {
    private User mUser;

    private EditText mUsernameField;
    private EditText mPasswordField;

    private Button mLoginButton;

    private GameTrackerDAO mGameTrackerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getDatabase();

        mLoginButton = findViewById(R.id.buttonLogin);
        mUsernameField = findViewById(R.id.EditTextUsernameLogin);
        mPasswordField = findViewById(R.id.EditTextPasswordLogin);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForUser();
            }
        });


    }

    private void checkForUser() {
        mUser = mGameTrackerDAO.getUserByUsername(mUsernameField.getText().toString());
        if(mUser == null){
            Toast.makeText(LoginActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mUser.getPassword().equals(mPasswordField.getText().toString())){
            Intent intent = LibraryActivity.intentFactory(LoginActivity.this, mUser.getUserId());
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDatabase(){
        mGameTrackerDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).
                allowMainThreadQueries()
                .build()
                .getGameTrackerDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }
}