package com.example.android.mathorcs.resources.highscores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kskotheim on 6/29/17.
 */

public class HighScoresDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "highscores.db";
    public static final int DATABASE_VERSION = 4;


    public HighScoresDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){

        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + HighScoresContract.HighScoresEntry.TABLE_NAME + " (" +
                HighScoresContract.HighScoresEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HighScoresContract.HighScoresEntry.HIGH_SCORE + " INTEGER NOT NULL, " +
                HighScoresContract.HighScoresEntry.PLAYER_NAME + " TEXT NOT NULL, " +
                HighScoresContract.HighScoresEntry.SCORE_DATE + " TEXT NOT NULL, " +
                HighScoresContract.HighScoresEntry.DIFFICULTY + " TEXT NOT NULL, " +
                HighScoresContract.HighScoresEntry.MEGA_MODE + " TEXT NOT NULL," +
                HighScoresContract.HighScoresEntry.LEVEL + " INTEGER NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int v1, int v2){

        sqLiteDatabase.execSQL("ALTER TABLE " + HighScoresContract.HighScoresEntry.TABLE_NAME + " ADD COLUMN " + HighScoresContract.HighScoresEntry.LEVEL + " INTEGER NOT NULL DEFAULT 0;");


    }

}
