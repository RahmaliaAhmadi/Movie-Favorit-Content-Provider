package com.arjava.filmjadwal.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.arjava.filmjadwal.Loader.LoaderSearchMovie;
import com.arjava.filmjadwal.R;
import com.arjava.filmjadwal.adapter.MovieAdapter;
import com.arjava.filmjadwal.model.MovieModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by arjava on 11/22/17.
 */

@SuppressLint("Registered")
public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieModel>> {

    //inisiasi
    private MovieAdapter movieAdapter;
    private String TAG = SearchActivity.class.getSimpleName();
    private String input_movie = "";

    @BindView(R.id.progressBarSearch)
    ProgressBar progressBar;
    @BindView(R.id.card_error_load)
    CardView cardView_error_load;
    @BindView(R.id.recyclerViewSearch)
    RecyclerView recyclerView;

    public SearchActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        movieAdapter = new MovieAdapter(this);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        cardView_error_load.setVisibility(View.GONE);

        Intent cari_film = getIntent();
        input_movie = cari_film.getStringExtra("cari_film");
        //set home button and title Actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.result_search) + " " + input_movie);
        getSupportLoaderManager().initLoader(0, null, this);

        //get string from MainActivity

        Log.d(TAG, "onCreate: SearchActivity = "+input_movie);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // penanganan untuk mengekspor data / membiarkan data ketika change orientation
        super.onSaveInstanceState(outState);
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
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            return new LoaderSearchMovie(SearchActivity.this, input_movie);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> data) {
        if (data.size() != 0) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            movieAdapter.setMovieItemsList(data);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(movieAdapter);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.INVISIBLE);
            cardView_error_load.setVisibility(View.VISIBLE);
            cardView_error_load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportLoaderManager().restartLoader(0, null, SearchActivity.this);
                    cardView_error_load.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {
        movieAdapter.setMovieItemsList(null);
    }
}
