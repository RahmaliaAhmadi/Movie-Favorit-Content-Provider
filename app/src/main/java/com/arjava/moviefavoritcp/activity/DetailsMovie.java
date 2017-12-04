package com.arjava.moviefavoritcp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.arjava.moviefavoritcp.R;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by arjava on 11/18/17.
 */

@SuppressLint("Registered")
public class DetailsMovie extends AppCompatActivity {

    //create object
    ImageView imageViewDetails;
    @BindView(R.id.textViewVote)
    TextView textViewVote;
    @BindView(R.id.textTitleDetails)
    TextView textViewTitle;
    @BindView(R.id.overViewDetails)
    TextView textViewOverview;

    String url_image = "http://image.tmdb.org/t/p/w342/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        //inisiasi
        imageViewDetails = findViewById(R.id.imageViewDetails);
//        textViewVote = findViewById(R.id.textViewVote);
//        textViewTitle = findViewById(R.id.textTitleDetails);
//        textViewOverview = findViewById(R.id.overViewDetails);

        //set actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get intent data
        Intent movie = getIntent();
        final String titleOri = movie.getStringExtra("orititle");
        final Double voteAvg = movie.getDoubleExtra("vote",0.0);
        final String overview = movie.getStringExtra("overview");
        final String backdrop_path = movie.getStringExtra("backdrop");
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
        textViewVote.setText(String.valueOf(voteAvg));
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

}
