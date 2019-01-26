package com.example.xyzreader.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.xyzreader.R;
import com.example.xyzreader.remote.MyIntentService;

import androidx.appcompat.app.AppCompatActivity;

public class ArticleLoadingSplash extends AppCompatActivity {
    private final static String TAG = ArticleLoadingSplash.class.getSimpleName();
    SharedPreferences sharedPreferences;
    Boolean roomData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_load);

        if(roomData) {
            //TODO: Set Progress for 10 seconds
            Log.e(TAG, "onCreate: Database created: no changes");
        } else {
            //Starts IntentService to load DB on background thread
            Intent startDownload = new Intent(this, MyIntentService.class);
            startService(startDownload);
            roomData = true;
            //TODO: store boolean 'roomData' as SharedPrefs
        }

        //TODO: Create a forced delay in loading next activity

        //Start new activity
        Intent loadListActivity = new Intent(this, ArticleListActivity.class);
        startActivity(loadListActivity);
    }
}
