package com.arjava.moviefavoritcp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.arjava.moviefavoritcp.model.MovieModel;

import java.util.ArrayList;

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
 * Created by arjava on 12/14/17.
 */

public class MovieHelper {

    private static String DATABASE_TABLE = TABLE_MOVIE_FAVORITE;
    private Context context;
    private SQLiteDatabase database;


    public MovieHelper(Context context) {
        this.context = context;
    }

    public void open() throws SQLException {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    public Cursor searchQuery(int title){
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " +
                ID_SHARE + " LIKE '%" + title + "%'", null);
    }

    public int getData(int id) {
        int hasil = 0;
        Cursor cursor = searchQuery(id);
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            hasil = cursor.getInt(1);

            for (; !cursor.isAfterLast();cursor.moveToNext()) {
                hasil = cursor.getInt(1);
            }
        }
        cursor.close();
        return hasil;
    }

    //mengambil semua data
    public Cursor queryDataAll() {
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " ORDER BY "+
                ID_SHARE + " ASC", null);
    }

    public ArrayList<MovieModel> getDataAll(){
        ArrayList<MovieModel> arrayList = new ArrayList<>();
        Cursor cursor = queryDataAll();
        cursor.moveToFirst();
        MovieModel movieItems;

        if (cursor.getCount()>0){
            do {
                movieItems = new MovieModel();
                movieItems.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.ID_ROOT)));
                movieItems.setMovie_id(cursor.getInt(cursor.getColumnIndexOrThrow(ID_SHARE)));
                movieItems.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movieItems.setOriginal_title(cursor.getString(cursor.getColumnIndexOrThrow(ORIGINAL_TITLE)));
                movieItems.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                movieItems.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                movieItems.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                movieItems.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                movieItems.setVote_average(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTE)));

                arrayList.add(movieItems);

                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    //menyimpan data ke dalam provider
    public void insertProvider(MovieModel movieItems) {
        ContentValues initialValue = new ContentValues();
        initialValue.put(ID_SHARE, movieItems.getMovie_id());
        initialValue.put(TITLE, movieItems.getTitle());
        initialValue.put(ORIGINAL_TITLE, movieItems.getOriginal_title());
        initialValue.put(OVERVIEW, movieItems.getOverview());
        initialValue.put(RELEASE_DATE, movieItems.getRelease_date());
        initialValue.put(POSTER_PATH, movieItems.getPoster_path());
        initialValue.put(BACKDROP_PATH, movieItems.getBackdrop_path());
        initialValue.put(VOTE, movieItems.getVote_average());
        Log.d("TAG", "insert: success");
        database.insert(DATABASE_TABLE, null, initialValue);
    }

    //menghapus data dari provider
    public void deleteProvider(int id) {
        database.delete(DATABASE_TABLE, ID_ROOT + " = '"+id+ "'",null);
    }
}
