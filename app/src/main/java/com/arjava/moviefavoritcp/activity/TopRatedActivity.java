package com.arjava.moviefavoritcp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.arjava.moviefavoritcp.Loader.LoaderTopRated;
import com.arjava.moviefavoritcp.R;
import com.arjava.moviefavoritcp.adapter.MovieAdapter;
import com.arjava.moviefavoritcp.model.MovieModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("Registered")
public class TopRatedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieModel>> {

    private MovieAdapter movieAdapter;

    @BindView(R.id.progressBarMainRated)
    ProgressBar progressBar;
    @BindView(R.id.recyclerViewRated)
    RecyclerView recyclerView;
    @BindView(R.id.card_error_load)
    CardView cardView_load_error;

    public TopRatedActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_rated);
        ButterKnife.bind(this);

        movieAdapter = new MovieAdapter(this);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.toprated);
        getSupportLoaderManager().initLoader(0, null, this);
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
        cardView_load_error.setVisibility(View.GONE);
        return new LoaderTopRated(this);
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
            cardView_load_error.setVisibility(View.VISIBLE);
            cardView_load_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportLoaderManager().restartLoader(0, null, TopRatedActivity.this);
                    cardView_load_error.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {
        movieAdapter.setMovieItemsList(null);
    }
}