package com.arjava.moviesfavorite.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.arjava.moviesfavorite.R;
import com.arjava.moviesfavorite.model.MovieModel;
import com.bumptech.glide.Glide;

/*
 * Created by arjava on 11/18/17.
 */

@SuppressLint("Registered")
public class DetailsMovie extends AppCompatActivity {

    public static final String EXTRA_FAVORIT = "extra_favorit";
    //create object
    ImageView imageViewDetails;
    TextView textViewVote;
    TextView textViewTitle;
    TextView textViewOverview;
    FloatingActionButton fab_favorite;

    boolean noEmpty = false;
    private String TAG = DetailsMovie.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        //set actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set title actionbar
        getSupportActionBar().setTitle("Details Movie Favorite");

        //inisiasi
        imageViewDetails = findViewById(R.id.imageViewDetails);
        textViewVote = findViewById(R.id.textViewVote);
        textViewTitle = findViewById(R.id.textTitleDetails);
        textViewOverview = findViewById(R.id.overViewDetails);
        fab_favorite = findViewById(R.id.fab_favourite);

        MovieModel movieModel;
        movieModel = getIntent().getParcelableExtra(EXTRA_FAVORIT);

        String url_image = "http://image.tmdb.org/t/p/w342/";
        String imageLoad = url_image+ movieModel.getBackdrop_path();

        if (movieModel != null) {
            noEmpty = true;
            //show image use library
            Glide
                    .with(DetailsMovie.this)
                    .load(imageLoad)
                    .placeholder(R.drawable.thumbnail_details_image)
                    .into(imageViewDetails);
            Log.d(TAG, "onCreate: IMAGELOAD = "+imageLoad);
            //show vote @Double
            textViewVote.setText(String.valueOf(movieModel.getVote_average()));
            Log.d(TAG, "onCreate: VOTE = "+ movieModel.getVote_average());
            //show Title
            textViewTitle.setText(movieModel.getTitle());
            Log.d(TAG, "onCreate: TITLE = "+ movieModel.getTitle());
            //show Descripton
            textViewOverview.setText(movieModel.getOverview());
            Log.d(TAG, "onCreate: OVERVIEW = "+ movieModel.getOverview());
        }
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

}
