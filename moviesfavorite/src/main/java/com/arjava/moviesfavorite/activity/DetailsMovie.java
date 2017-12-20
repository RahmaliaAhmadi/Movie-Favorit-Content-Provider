package com.arjava.moviesfavorite.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arjava.moviesfavorite.R;
import com.bumptech.glide.Glide;

import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.BACKDROP_PATH;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.ID_SHARE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.ORIGINAL_TITLE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.OVERVIEW;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.POSTER_PATH;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.RELEASE_DATE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.TITLE;
import static com.arjava.moviesfavorite.db.DatabaseContract.FavoriteColumns.VOTE;

/*
 * Created by arjava on 11/18/17.
 */

@SuppressLint("Registered")
public class DetailsMovie extends AppCompatActivity implements View.OnClickListener {

    //create object
    ImageView imageViewDetails;
    TextView textViewVote;
    TextView textViewTitle;
    TextView textViewOverview;
    FloatingActionButton fab_favorite;

    String url_image = "http://image.tmdb.org/t/p/w342/";
    boolean favorited = false;

    //data movie
    private String title, titleOri, overview, release_date, poster_path, backdrop_path;
    private Double vote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        //inisiasi
        imageViewDetails = findViewById(R.id.imageViewDetails);
        textViewVote = findViewById(R.id.textViewVote);
        textViewTitle = findViewById(R.id.textTitleDetails);
        textViewOverview = findViewById(R.id.overViewDetails);
        fab_favorite = findViewById(R.id.fab_favourite);
        fab_favorite.setOnClickListener(this);

        //set actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get intent data
        Intent movie = getIntent();
        title = movie.getStringExtra("title");
        titleOri = movie.getStringExtra("orititle");
        overview = movie.getStringExtra("overview");
        release_date = movie.getStringExtra("date");
        poster_path = movie.getStringExtra("poster");
        backdrop_path = movie.getStringExtra("backdrop");
        vote = movie.getDoubleExtra("vote",0.0);
        final String imageLoad = url_image+backdrop_path;

        //set title actionbar
        getSupportActionBar().setTitle(titleOri);

        //show image use library
        Glide
                .with(DetailsMovie.this)
                .load(imageLoad)
                .placeholder(R.drawable.thumbnail_details_image)
                .into(imageViewDetails);
        //show vote @Double
        textViewVote.setText(String.valueOf(vote));
        //show Title
        textViewTitle.setText(titleOri);
        //show Descripton
        textViewOverview.setText(overview);
    }

    //penanganan untuk icon back actionbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.fab_favourite) {

            boolean isEmpty = false;
            if (TextUtils.isEmpty(titleOri)) {
                isEmpty = true;
                Toast.makeText(DetailsMovie.this, "Tidak ada data !", Toast.LENGTH_SHORT).show();
            }

            if (!isEmpty) {
                ContentValues values = new ContentValues();
                values.put(TITLE, title);
                values.put(ORIGINAL_TITLE, titleOri);
                values.put(OVERVIEW, overview);
                values.put(RELEASE_DATE, release_date);
                values.put(POSTER_PATH, poster_path);
                values.put(BACKDROP_PATH, backdrop_path);
                values.put(VOTE, vote);
                favorited = true;
            }
        }
    }
}
