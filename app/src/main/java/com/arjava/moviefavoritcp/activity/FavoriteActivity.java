package com.arjava.moviefavoritcp.activity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arjava.moviefavoritcp.R;
import com.arjava.moviefavoritcp.adapter.MovieAdapter;
import com.arjava.moviefavoritcp.database.MovieHelper;
import com.arjava.moviefavoritcp.model.MovieModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by arjava on 12/18/17.
 */

public class FavoriteActivity extends AppCompatActivity {

    private static final String TAG = "TAG_Favorite";
    private MovieHelper movieHelper;
    private ArrayList<MovieModel> sDataMovie;
    private MovieAdapter adapter;

    @BindView(R.id.card_error_load)CardView cardNotFound;
    @BindView(R.id.rcv_movie)RecyclerView rcvlistMovie;
    @BindView(R.id.progress)ProgressBar loading;

    public FavoriteActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        ButterKnife.bind(this);

        rcvlistMovie.setLayoutManager(new LinearLayoutManager(this));
        rcvlistMovie.setHasFixedSize(true);

        adapter = new MovieAdapter(this);
        movieHelper = new MovieHelper(this);
        movieHelper.open();

        sDataMovie = new ArrayList<>();
        adapter.setMovieItemsList(sDataMovie);
        rcvlistMovie.setAdapter(adapter);

        if (savedInstanceState != null) {
            Log.d(TAG, "onCreate: SavedInstanceState Not Null");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoaderFavorite().execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class LoaderFavorite extends AsyncTask<Void, Void, ArrayList<MovieModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading.setVisibility(View.VISIBLE);

            if (sDataMovie.size() > 0) {
                sDataMovie.clear();
            }
        }

        @Override
        protected ArrayList<MovieModel> doInBackground(Void... voids) {
            return movieHelper.getDataAll();
        }

        @Override
        protected void onPostExecute(ArrayList<MovieModel> movieModels) {
            super.onPostExecute(movieModels);
            loading.setVisibility(View.GONE);
            cardNotFound.setVisibility(View.GONE);

            sDataMovie.addAll(movieModels);
            adapter.setMovieItemsList(sDataMovie);

            if (sDataMovie.size() == 0) {
                cardNotFound.setVisibility(View.VISIBLE);
                cardNotFound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new LoaderFavorite();
                        cardNotFound.setVisibility(View.GONE);
                    }
                });
                Toast.makeText(FavoriteActivity.this, "Data Tidak Ada", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
