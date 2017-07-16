package com.example.android.mathorcs;

import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.mathorcs.R;

public class AchivementsActivity extends AppCompatActivity {

    private TextView mTotalKillsTextView;
    private TextView mTotalTimeTextView;
    private TextView mHighestLevelTextView;
    private TextView mGamesFinishedTextView;
    private TextView mGamesWonTextView;

    private int totalKills;
    private int totalTime;
    private int highestLevelEasy;
    private int highestLevelMedium;
    private int highestLevelHard;
    private int gamesFinished;
    private int gamesWon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achivements);
        MainActivity.currentActivity = this;

        mTotalKillsTextView = (TextView) findViewById(R.id.achievements_total_kills_textview);
        mTotalTimeTextView = (TextView) findViewById(R.id.achievements_total_time_textview);
        mHighestLevelTextView = (TextView) findViewById(R.id.achievements_highest_level_textview);
        mGamesFinishedTextView = (TextView) findViewById(R.id.achievements_games_finished_textview);
        mGamesWonTextView = (TextView) findViewById(R.id.achievements_games_won_textview);

        findPreferenceValues();

        displayPreferenceValues();
    }

    private void findPreferenceValues(){

        SharedPreferences sharedP = getSharedPreferences(getString(R.string.shared_prefs_key), MODE_PRIVATE);

        totalKills = sharedP.getInt(getString(R.string.all_time_kills_key), 0);

        totalTime = sharedP.getInt(getString(R.string.all_time_time_key), 0);

        highestLevelEasy = sharedP.getInt(getString(R.string.highest_level_reached_easy_key), 0);
        highestLevelMedium = sharedP.getInt(getString(R.string.highest_level_reached_medium_key), 0);
        highestLevelHard = sharedP.getInt(getString(R.string.highest_level_reached_hard_key), 0);

        gamesFinished = sharedP.getInt(getString(R.string.all_time_games_finised_key), 0);
        gamesWon = sharedP.getInt(getString(R.string.all_time_games_won_key),0);

    }

    private void displayPreferenceValues(){

        mTotalKillsTextView.setText(getString(R.string.all_time_kills_title) + " " + totalKills);


        if(totalTime < 60) mTotalTimeTextView.setText(getString(R.string.all_time_time_title) + " " + totalTime + " seconds");
        else if(totalTime < 3600) mTotalTimeTextView.setText(getString(R.string.all_time_time_title) + " " + Math.round(Math.floor((double)totalTime/60)) + " mins");
        else mTotalTimeTextView.setText(getString(R.string.all_time_time_title) + " " + (double)Math.round((double)totalTime/360)/10 + " hrs");

        mHighestLevelTextView.setText(getString(R.string.highest_level_reached_title) + " " + highestLevelEasy + " / " + highestLevelMedium + " / " + highestLevelHard);

        mGamesFinishedTextView.setText(getString(R.string.all_time_games_finished_title) + " " + gamesFinished);

        mGamesWonTextView.setText(getString(R.string.all_time_games_won_title) + " " + gamesWon);
    }

    public void handleButton(View v){

        switch(v.getId()){

            case R.id.return_from_achievements_button:

                NavUtils.navigateUpFromSameTask(this);
                break;

        }


    }


}
