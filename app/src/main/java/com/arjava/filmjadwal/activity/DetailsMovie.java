package com.arjava.filmjadwal.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arjava.filmjadwal.Loader.LoaderDetailMovie;
import com.arjava.filmjadwal.R;
import com.arjava.filmjadwal.database.MovieHelper;
import com.arjava.filmjadwal.model.MovieModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/*
 * Created by arjava on 11/18/17.
 */

@SuppressLint("Registered")
public class DetailsMovie extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieModel>> {

    public static String EXTRA_MOVIE = "movie";
    public static String EXTRA_ID = "id";
    public static String EXTRA_TITLE = "title";

    private static final String TAG = DetailsMovie.class.getSimpleName();

    private MovieModel movieModel;
    private MovieHelper movieHelper;

    private int id_movie;
    private boolean isFavorited;

    //create object

    @BindView(R.id.progressBarDetail)
    ProgressBar progressBar;
    @BindView(R.id.imageViewDetails)
    ImageView imageViewDetails;
    @BindView(R.id.textViewVote)
    TextView textViewVote;
    @BindView(R.id.textTitleDetails)
    TextView textViewTitle;
    @BindView(R.id.overViewDetails)
    TextView textViewOverview;
    @BindView(R.id.fab_favourite)
    FloatingActionButton fab_favorite;
    @BindView(R.id.rvLayoutDetail)
    RelativeLayout view_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ButterKnife.bind(this);

        //set actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get intent data
        movieHelper = new MovieHelper(this);
        movieHelper.open();

        movieModel = getIntent().getParcelableExtra(EXTRA_MOVIE);
        id_movie = getIntent().getIntExtra(EXTRA_ID, 0);

        if (movieModel != null) {
            setFavoriteStatus();
            if (savedInstanceState != null) {
                progressBar.setVisibility(View.GONE);
                getSupportLoaderManager().initLoader(0, null, this);
            } else {
                getSupportLoaderManager().initLoader(0, null, this);
            }
        }
        if (id_movie != 0) {
            progressBar.setVisibility(View.GONE);
            fab_favorite.setVisibility(View.GONE);
            getSupportLoaderManager().initLoader(0, null, this);
        }

    }

    private void setFavoriteStatus() {
        int db_movie = movieHelper.getData(movieModel.getMovie_id());
        if (db_movie == movieModel.getMovie_id()) {
            Log.d(TAG, "setFavoriteStatus: MOVIE ID"+ movieModel.getMovie_id());
            fab_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            fab_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    public boolean isFavorited() {
        int db_movie = movieHelper.getData(movieModel.getMovie_id());
        if (db_movie == movieModel.getMovie_id()) {
            movieHelper.deleteProvider(movieModel.getMovie_id());
            isFavorited = true;
            return true;
        } else {
            MovieModel movieModel1 = new MovieModel();
            movieModel1.setMovie_id(movieModel.getMovie_id());
            movieModel1.setTitle(movieModel.getTitle());
            movieModel1.setOriginal_title(movieModel.getOriginal_title());
            movieModel1.setOverview(movieModel.getOverview());
            movieModel1.setRelease_date(movieModel.getRelease_date());
            movieModel1.setPoster_path(movieModel.getPoster_path());
            movieModel1.setBackdrop_path(movieModel.getBackdrop_path());
            movieModel1.setVote_average(movieModel.getVote_average());
            movieHelper.insertProvider(movieModel);
            isFavorited = false;
            return false;
        }
    }

    @OnClick(R.id.fab_favourite)
    void setFab_favorite() {
        isFavorited = isFavorited();
        if (isFavorited) {
            Toast.makeText(this, R.string.unfavorite, Toast.LENGTH_SHORT).show();
            fab_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            Toast.makeText(this, R.string.favorite, Toast.LENGTH_SHORT).show();
            fab_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
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


    @Override
    public Loader<ArrayList<MovieModel>> onCreateLoader(int id, Bundle args) {
        if (movieModel != null) {
            view_detail.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            return new LoaderDetailMovie(this, movieModel.getMovie_id());
        } else {
            view_detail.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            return new LoaderDetailMovie(this, id_movie);
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> data) {

            progressBar.setVisibility(View.GONE);
            view_detail.setVisibility(View.VISIBLE);

            String url_image = "http://image.tmdb.org/t/p/w342/";
        String titleOri = data.get(0).getOriginal_title();
        String overview = data.get(0).getOverview();
        String backdrop_path = data.get(0).getBackdrop_path();
        Double vote = data.get(0).getVote_average();
            String imageLoad = url_image + backdrop_path;
            Log.d(TAG, "onLoadFinished: POSTER_PATH " + imageLoad);

            //set title actionbar
            getSupportActionBar().setTitle(titleOri);
            //show image backdrop

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

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (movieHelper != null) {
            movieHelper.close();
        }
        getSupportLoaderManager().destroyLoader(0);
    }
}
