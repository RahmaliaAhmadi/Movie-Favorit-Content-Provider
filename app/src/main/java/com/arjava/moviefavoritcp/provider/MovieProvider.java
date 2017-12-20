package com.arjava.moviefavoritcp.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arjava.moviefavoritcp.database.DatabaseHelper;
import com.arjava.moviefavoritcp.database.MovieHelper;

import static android.provider.BaseColumns._ID;
import static com.arjava.moviefavoritcp.database.DatabaseContract.AUTHORITY;
import static com.arjava.moviefavoritcp.database.DatabaseContract.CONTENT_URI;
import static com.arjava.moviefavoritcp.database.DatabaseContract.TABLE_MOVIE_FAVORITE;

/*
 * Created by arjava on 12/14/17.
 */

public class MovieProvider extends ContentProvider {

    private static final int FAVORIT = 1;
    private static final int FAVORIT_ID = 2;

    private static final UriMatcher rUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // content com.arjava.moviefavoritcp/favorit
        rUriMatcher.addURI(AUTHORITY, TABLE_MOVIE_FAVORITE, FAVORIT);

        // content com.arjava.moviefavoritcp/favorit/id
        rUriMatcher.addURI(AUTHORITY, TABLE_MOVIE_FAVORITE+ "/#", FAVORIT_ID);
    }

    private MovieHelper movieHelper;
    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        database = databaseHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cursor = null;

        if (rUriMatcher.match(uri) == FAVORIT) {
            cursor = database.query(TABLE_MOVIE_FAVORITE, null, null, null, null, null, _ID+ " ASC");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long added = database.insert(TABLE_MOVIE_FAVORITE, null, contentValues);

        if (added > 0) {
            Uri sUri = ContentUris.withAppendedId(CONTENT_URI, added);
            getContext().getContentResolver().notifyChange(uri, null);
            return sUri;
        }
        throw new SQLException("failed insert URI : "+uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deleted;
        switch (rUriMatcher.match(uri)) {
            case FAVORIT:
                deleted = database.delete(TABLE_MOVIE_FAVORITE, s, strings);
                break;
                default:
                    deleted = 0;
                    break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updated;
        switch (rUriMatcher.match(uri)) {
            case FAVORIT:
                updated = database.update(TABLE_MOVIE_FAVORITE, contentValues, s, strings);
                break;
                default:
                    throw new IllegalArgumentException("Updated failed URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updated;
    }
}