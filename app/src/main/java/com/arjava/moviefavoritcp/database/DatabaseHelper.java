package com.arjava.moviefavoritcp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.arjava.moviefavoritcp.database.DatabaseContract.FavoriteColumns.BACKDROP_PATH;
import static com.arjava.moviefavoritcp.database.DatabaseContract.FavoriteColumns.ID_ROOT;
import static com.arjava.moviefavoritcp.database.DatabaseContract.FavoriteColumns.ID_SHARE;
import static com.arjava.moviefavoritcp.database.DatabaseContract.FavoriteColumns.ORIGINAL_TITLE;
import static com.arjava.moviefavoritcp.database.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.arjava.moviefavoritcp.database.DatabaseContract.FavoriteColumns.POSTER_PATH;
import static com.arjava.moviefavoritcp.database.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static com.arjava.moviefavoritcp.database.DatabaseContract.FavoriteColumns.TITLE;
import static com.arjava.moviefavoritcp.database.DatabaseContract.FavoriteColumns.VOTE;
import static com.arjava.moviefavoritcp.database.DatabaseContract.TABLE_MOVIE_FAVORITE;

/*
 * Created by arjava on 12/13/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "db_movie";

    private static final int DB_VERSION = 3;

    public static final String SQL_CREATE_TABLE_FAVORITE_MOVIE = "CREATE TABLE " + TABLE_MOVIE_FAVORITE
                    + " (" + ID_ROOT +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ID_SHARE +" TEXT NOT NULL, "
                    + TITLE +" TEXT NOT NULL, "
                    + ORIGINAL_TITLE +" TEXT NOT NULL, "
                    + OVERVIEW +" TEXT NOT NULL, "
                    + RELEASE_DATE +" TEXT NOT NULL, "
                    + POSTER_PATH +" TEXT NOT NULL, "
                    + BACKDROP_PATH +" TEXT NOT NULL, "
                    + VOTE +" TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_FAVORITE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_MOVIE_FAVORITE);
        onCreate(sqLiteDatabase);
    }
}
