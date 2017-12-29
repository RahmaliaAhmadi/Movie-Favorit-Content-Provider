package com.arjava.moviesfavorite.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arjava.moviesfavorite.R;
import com.arjava.moviesfavorite.activity.DetailsMovie;
import com.arjava.moviesfavorite.db.DatabaseContract;
import com.arjava.moviesfavorite.model.MovieModel;
import com.bumptech.glide.Glide;

import static android.provider.BaseColumns._ID;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.BACKDROP_PATH;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.ID_SHARE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.POSTER_PATH;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.VOTE;

/*
 * Created by arjava on 11/15/17.
 */

public class MovieAdapter extends CursorAdapter {

    //konstruktor
    public MovieAdapter(Context context, Cursor cursor, boolean autoRequery) {
        super(context, cursor, autoRequery);
    }

    //mengatur tampilan layout
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_row_movie, viewGroup, false);
    }

    public Cursor getCursor() {
        return super.getCursor();
    }

    //menampilkan hasil dari data yang kita ambil dari API
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        String url_image = "http://image.tmdb.org/t/p/w342/";
        String id_poster = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.POSTER_PATH));
        String poster_image =  url_image +id_poster;
        Log.d("Movie Adapter", "bindView: "+ poster_image);

        if (cursor!=null) {

            ImageView imageView = view.findViewById(R.id.imageViewItemMovie);
            TextView textViewTitle = view.findViewById(R.id.textViewItemTitle);
            TextView textViewDescription = view.findViewById(R.id.textViewItemDesc);
            TextView textViewdate = view.findViewById(R.id.textViewItemDate);
            Button detail = view.findViewById(R.id.btnDetail);
            Button share = view.findViewById(R.id.btnShare);

            textViewTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.TITLE)));
            textViewDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.OVERVIEW)));
            textViewdate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.RELEASE_DATE)));

            //menampilkan gambar
            Glide
                    .with(context)
                    .load(poster_image)
                    .centerCrop()
                    .override(300,400)
                    .placeholder(R.drawable.ic_crop_original_purple_300_24dp)
                    .into(imageView);

            //penanganan ketika button detail diklik
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MovieModel movieModel = new MovieModel();
                    movieModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                    movieModel.setMovie_id(cursor.getInt(cursor.getColumnIndexOrThrow(ID_SHARE)));
                    movieModel.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                    movieModel.setPoster_path(cursor.getString(cursor.getColumnIndexOrThrow(POSTER_PATH)));
                    movieModel.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));
                    movieModel.setRelease_date(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                    movieModel.setBackdrop_path(cursor.getString(cursor.getColumnIndexOrThrow(BACKDROP_PATH)));
                    movieModel.setVote_average(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(VOTE))));

                    Intent intent = new Intent(view.getContext(), DetailsMovie.class);
                    intent.putExtra(DetailsMovie.EXTRA_FAVORIT, movieModel);
                    view.getContext().startActivity(intent);
                }
            });

            //penanganan ketika button share diklik
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String base_url_info_film = "https://www.themoviedb.org/movie/";

                    // share url_info_film menggunakan intent
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, base_url_info_film+cursor.getString(cursor.getColumnIndexOrThrow((DatabaseContract.FavoriteColumns.ID_SHARE))));
                    view.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });

        }else {
            Toast.makeText(context, "No Favorited !", Toast.LENGTH_SHORT).show();
        }
    }
}
