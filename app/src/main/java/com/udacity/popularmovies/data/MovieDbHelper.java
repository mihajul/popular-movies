package com.udacity.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.udacity.popularmovies.data.MovieContract.*;


public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE "  + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID                    + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_MOVIE_ID        + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_TITLE           + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE  + " TEXT , " +
                MovieEntry.COLUMN_IMAGE_URL       + " TEXT , " +
                MovieEntry.COLUMN_OVERVIEW        + " TEXT , " +
                MovieEntry.COLUMN_RELEASE_DATE    + " DATETIME , " +
                MovieEntry.COLUMN_VOTE_AVERAGE    + " INTEGER );";

        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
