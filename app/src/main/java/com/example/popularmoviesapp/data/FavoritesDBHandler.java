package com.example.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesDBHandler extends SQLiteOpenHelper {

    //Database name and version
    public static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_FAVORITES_TABLE = "CREATE TABLE " + FavoritesContract.FavoriteEntry.TABLE_FAVORITES
        + "( " + FavoritesContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY, "
        + FavoritesContract.FavoriteEntry.COLUMN_TITLE + " TEXT, " + FavoritesContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE + " TEXT, "
        + FavoritesContract.FavoriteEntry.COLUMN_OVERVIEW + " TEXT, " + FavoritesContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT, "
        + FavoritesContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT, " + FavoritesContract.FavoriteEntry.COLUMN_VOTE_AVERAGE + ")";

        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoriteEntry.TABLE_FAVORITES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FavoritesContract.FavoriteEntry.TABLE_FAVORITES + "'");

        // re-create database
        onCreate(db);
    }
}
