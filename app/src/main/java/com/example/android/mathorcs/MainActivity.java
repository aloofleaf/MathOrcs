package com.example.android.mathorcs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.mathorcs.resources.highscores.HighScores;
import com.example.android.mathorcs.resources.Orc;

/**
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    private Button playGameButton;
    private  Button achievementsButton;
    private  Button highScoresButton;
    private  Button settingsButton;

    public static Activity currentActivity;

    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private Toast currentToast;

    public static String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mControlsView = findViewById(R.id.buttons_linear_layout);
        mContentView = findViewById(R.id.fullscreen_content);

        currentActivity = this;

        setupSettings();
        HighScores.createHighScores(this.getApplicationContext());


    }

    public void setupSettings(){

        SharedPreferences sharedP = PreferenceManager.getDefaultSharedPreferences(this);

        int difficultyKey = Integer.parseInt(sharedP.getString(getString(R.string.difficulty_key), getString(R.string.easy_difficulty_preference_value)));

        PlayGame.difficulty = difficultyKey;

        if(difficultyKey==0){
            PlayGame.timerEnabled=false;
            difficulty = getString(R.string.no_timer_difficulty_preference);
        }
        if(difficultyKey==1){
            PlayGame.timerEnabled=true;
            PlayGame.startingTimePerQuestion = 8500;
            PlayGame.timePerQuestionToApproach = 3500;
            PlayGame.timePerBonus = 4000;
            difficulty = getString(R.string.easy_difficulty_preference);
        }
        if(difficultyKey==2){
            PlayGame.timerEnabled=true;
            PlayGame.startingTimePerQuestion = 7000;
            PlayGame.timePerQuestionToApproach = 2500;
            PlayGame.timePerBonus = 3000;
            difficulty = getString(R.string.medium_difficulty_preference);
        }
        if(difficultyKey==3){
            PlayGame.timerEnabled=true;
            PlayGame.startingTimePerQuestion = 5500;
            PlayGame.timePerQuestionToApproach = 1500;
            PlayGame.timePerBonus = 2000;
            difficulty = getString(R.string.hard_difficulty_preference);
        }

        if(sharedP.getBoolean(getString(R.string.timer_bar_pref_key), true)){
            PlayGame.timerBarEnabled = true;
        }else{
            PlayGame.timerBarEnabled = false;
        }

        Orc.megaModifier = 0;
        if(sharedP.getBoolean(getString(R.string.mega_mode_pref_key), true)) {          //is mega mode enabled? if so ...
            Orc.megaModifier = Integer.parseInt(sharedP.getString(getString(R.string.mega_mode_levels_key), getString(R.string.mega_mode_level_1_value)));
        }
        PlayGame.timeDecreaseFactor = .95 - .01*Orc.megaModifier;

        PlayGame.numberOfLevels = 20;

        if(sharedP.getBoolean(getString(R.string.tips_pref_key), true)){
            PlayGame.tipsEnabled = true;
        }else{
            PlayGame.tipsEnabled = false;
        }

    }

    public void handleButton(View v){

        Intent intent;

        switch (v.getId()){
            case R.id.play_game_button:
                intent = new Intent(MainActivity.this, PlayGame.class);
                startActivity(intent);
                break;

            case R.id.settings_button:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.achievements_button:
                intent = new Intent(this, AchivementsActivity.class);
                startActivity(intent);
                break;

            case R.id.high_scores_button:
                intent = new Intent(this, HighScoresActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void makeToast(String text){
        if(currentToast != null) {
            currentToast.cancel();
        }
        currentToast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        currentToast.show();
    }
}
