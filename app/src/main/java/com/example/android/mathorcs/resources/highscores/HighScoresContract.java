package com.example.android.mathorcs.resources.highscores;

import android.provider.BaseColumns;

/**
 * Created by kskotheim on 6/29/17.
 */

public class HighScoresContract {

    private HighScoresContract(){}

    public static final class HighScoresEntry implements BaseColumns{
        public static final String TABLE_NAME = "highScores";
        public static final String PLAYER_NAME = "playerName";
        public static final String SCORE_DATE = "scoreDate";
        public static final String HIGH_SCORE = "thisHighScore";
        public static final String DIFFICULTY = "difficulty";
        public static final String MEGA_MODE = "megaMode";
        public static final String LEVEL = "level";

    }

}
