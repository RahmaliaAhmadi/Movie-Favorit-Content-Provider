package com.arjava.moviefavoritcp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arjava.moviefavoritcp.R;
import com.arjava.moviefavoritcp.adapter.MovieAdapter;
import com.arjava.moviefavoritcp.model.MovieModel;
import com.arjava.moviefavoritcp.request.ApiClient;
import com.arjava.moviefavoritcp.request.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * Created by arjava on 11/22/17.
 */

@SuppressLint("Registered")
public class SearchActivity extends AppCompatActivity {

    //inisiasi
    private String TAG = SearchActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private String input_movie = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        progressBar = findViewById(R.id.progressBarSearch);

        //get string from MainActivity
        Intent cari_film = getIntent();
        input_movie = cari_film.getStringExtra("cari_film");

        //set home button and title Actionbar
        getSupportActionBar().setTitle(getResources().getString(R.string.result_search)+" "+input_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get method request movie result
        getMovie();

    }


    public void getMovie() {

        Log.d(TAG, "onClick: input_movie "+ input_movie);

        //show progressBar
        progressBar.setVisibility(View.VISIBLE);

        //inisiasi recyclerview
        final RecyclerView recyclerView = findViewById(R.id.recyclerViewSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        //inisiasi apiclient
        ApiInterface apiInterface = ApiClient.getRetrofit(getApplicationContext()).create(ApiInterface.class);

        //call api_interface use parameter searching
        Call<MovieModel> call = apiInterface.getMovieItems(input_movie);
        call.enqueue(new Callback<MovieModel>() {

            //on success request to server
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                MovieModel data = response.body();
                if (data.getResults().size()==0) {
                    Toast.makeText(getApplicationContext(), R.string.data_nothing, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else {
                    recyclerView.setAdapter(new MovieAdapter(data.getResults(), R.layout.content_recycler, getApplicationContext()));
                    Log.e(TAG, "onResponse: hasil pemanggilan"+ call);
                    progressBar.setVisibility(View.GONE);
                }
            }

            // on failed request to server
            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
                Log.d(TAG, t.toString());
                progressBar.setVisibility(View.GONE);
            }
        });
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
