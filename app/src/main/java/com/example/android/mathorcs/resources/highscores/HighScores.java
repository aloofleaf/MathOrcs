package com.example.android.mathorcs.resources.highscores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kskotheim on 7/7/17.
 */

public class HighScores{

    public static HighScores theHighScores;
    private static SQLiteDatabase mDb;


    private HighScores(Context context){

        HighScoresDbHelper dbHelper = new HighScoresDbHelper(context);
        mDb = dbHelper.getWritableDatabase();


    }

    public Cursor getHighScores(){
        return mDb.query(HighScoresContract.HighScoresEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HighScoresContract.HighScoresEntry.HIGH_SCORE,
                "10");
    }

    public static int getScoreToBeat(){
        Cursor cursor = mDb.query(HighScoresContract.HighScoresEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                HighScoresContract.HighScoresEntry.HIGH_SCORE,
                "10");

        cursor.moveToLast();
        return -1 * cursor.getInt(cursor.getColumnIndex(HighScoresContract.HighScoresEntry.HIGH_SCORE));
    }

    private long addScore(String name, int score, String date, int mm, String diff, int level){
        ContentValues cv = new ContentValues();
        cv.put(HighScoresContract.HighScoresEntry.PLAYER_NAME, name);
        cv.put(HighScoresContract.HighScoresEntry.HIGH_SCORE, score);
        cv.put(HighScoresContract.HighScoresEntry.SCORE_DATE, date);
        cv.put(HighScoresContract.HighScoresEntry.MEGA_MODE, mm);
        cv.put(HighScoresContract.HighScoresEntry.DIFFICULTY, diff);
        cv.put(HighScoresContract.HighScoresEntry.LEVEL, level);

        return mDb.insert(HighScoresContract.HighScoresEntry.TABLE_NAME, null, cv);
    }

    public static void createHighScores(Context context){
        theHighScores = new HighScores(context);
    }
    public static String addToHighScores(String name, int score, int mm, String diff, int level){

        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);

        String stringDate = dateFormat.format(today);

        if(theHighScores != null) {
            theHighScores.addScore(name, score, stringDate, mm, diff, level);
            return stringDate;
        }else{
            return "false";
        }
    }
}
