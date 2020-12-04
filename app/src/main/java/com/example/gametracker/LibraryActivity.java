package com.example.gametracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.gametracker.db.AppDatabase;
import com.example.gametracker.db.GameTrackerDAO;

public class LibraryActivity extends AppCompatActivity {

    private Integer mUserId;

    private GameTrackerDAO mGameTrackerDAO;

    private TextView mTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        mUserId = getIntent().getIntExtra(MainActivity.USER_ID_KEY, -1);

        mTextView = findViewById(R.id.textViewLibrary);

        mTextView.setText(mUserId.toString());

        getDatabase();
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