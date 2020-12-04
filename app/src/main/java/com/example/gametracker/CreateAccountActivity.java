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

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;

    private String mUsername;
    private String mPassword;

    private Button mCreateButton;

    private boolean mIsAdmin;

    private GameTrackerDAO mGameTrackerDAO;

    private User mUser;

    private int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mUsernameField = findViewById(R.id.EditTextUsername);
        mPasswordField = findViewById(R.id.EditTextPassword);
        mCreateButton = findViewById(R.id.buttonCreateAccount);

        getDatabase();

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });
    }

    private void createUser() {
        checkAdminUser();
        if(checkUsernameAndPassword()){
            mUser = new User(mUsername, mPassword, mIsAdmin);
            mGameTrackerDAO.insert(mUser);
            mUserId = mGameTrackerDAO.getUserByUsername(mUsername).getUserId();
            Intent intent = LibraryActivity.intentFactory(CreateAccountActivity.this, mUserId);
            startActivity(intent);
            finish();
        }
    }

    private boolean checkUsernameAndPassword() {
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
        if(!mUsername.equals("")){
            if(!mPassword.equals("")){
                return true;
            }else{
                Toast.makeText(CreateAccountActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(CreateAccountActivity.this, "Invalid Username", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void checkAdminUser() {
        if(mUsernameField.getText().toString().equals("ancline")){
            mIsAdmin = true;
        }
    }

    private void getDatabase(){
        mGameTrackerDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).
                allowMainThreadQueries()
                .build()
                .getGameTrackerDAO();
    }

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}