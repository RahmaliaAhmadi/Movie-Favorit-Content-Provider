package com.arjava.moviesfavorite.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arjava.moviesfavorite.R;
import com.arjava.moviesfavorite.activity.DetailsMovie;
import com.arjava.moviesfavorite.model.MovieModel;
import com.bumptech.glide.Glide;

import java.util.List;

import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.BACKDROP_PATH;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.ID_SHARE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.ORIGINAL_TITLE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.POSTER_PATH;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.VOTE;
import static com.arjava.moviesfavorite.db.DatabaseContract.getColumnString;

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
        View view = LayoutInflater.from(context).inflate(R.layout.item_row_movie, viewGroup, false);
        return view;
    }

    public Cursor getCursor() {
        return super.getCursor();
    }

    //menampilkan hasil dari data yang kita ambil dari API
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {

        String url_image = "http://image.tmdb.org/t/p/w342/";
        String id_poster = getColumnString(cursor, POSTER_PATH);
        String poster_image =  url_image +id_poster;

        if (cursor!=null) {

            LinearLayout linearLayout = view.findViewById(R.id.linearLayoutContentMovie);
            ImageView imageView = view.findViewById(R.id.imageViewItemMovie);
            TextView textViewTitle = view.findViewById(R.id.textViewItemTitle);
            TextView textViewDescription = view.findViewById(R.id.textViewItemDesc);
            TextView textViewdate = view.findViewById(R.id.textViewItemDate);
            Button detail = view.findViewById(R.id.btnDetail);
            Button share = view.findViewById(R.id.btnShare);

            textViewTitle.setText(getColumnString(cursor, TITLE));
            textViewDescription.setText(getColumnString(cursor, OVERVIEW));
            textViewdate.setText(getColumnString(cursor, RELEASE_DATE));

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
                    Intent det = new Intent(view.getContext(), DetailsMovie.class);
                    det.putExtra("title", getColumnString(cursor, TITLE));
                    det.putExtra("orititle", getColumnString(cursor, ORIGINAL_TITLE));
                    det.putExtra("overview", getColumnString(cursor, OVERVIEW));
                    det.putExtra("date", getColumnString(cursor, RELEASE_DATE));
                    det.putExtra("poster", getColumnString(cursor, POSTER_PATH));
                    det.putExtra("backdrop", getColumnString(cursor, BACKDROP_PATH));
                    det.putExtra("vote", getColumnString(cursor, VOTE));
                    view.getContext().startActivity(det);
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
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, base_url_info_film+getColumnString(cursor, String.valueOf(ID_SHARE)));
                    view.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

                }
            });

        }else {
            Toast.makeText(context, "No Favorited !", Toast.LENGTH_SHORT).show();
        }
    }
}
