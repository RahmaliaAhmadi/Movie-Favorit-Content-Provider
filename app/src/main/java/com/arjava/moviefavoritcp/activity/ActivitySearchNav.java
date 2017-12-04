package com.arjava.moviefavoritcp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arjava.moviefavoritcp.R;
import com.arjava.moviefavoritcp.adapter.MovieAdapter;
import com.arjava.moviefavoritcp.model.MovieModel;
import com.arjava.moviefavoritcp.request.ApiClient;
import com.arjava.moviefavoritcp.request.ApiInterface;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("Registered")
public class ActivitySearchNav extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_nav);
        ButterKnife.bind(this);

        inputSearch = findViewById(R.id.edtSearch);
        progressBar = findViewById(R.id.progressBarSnav);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search_nav);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        progressBar.setVisibility(View.VISIBLE);


    }

    @OnClick(R.id.btnSearch)
    public void submitSearch(View view) {
        if (view.getId()==R.id.btnSearch) {
            getMovieSearch();
            hideSoftKeyboard(this);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


    private void getMovieSearch() {
        //menggunakan search berdasarkan input user
        String input_movie = inputSearch.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        final RecyclerView recyclerView = findViewById(R.id.recyclerMovie);
        recyclerView.setLayoutManager(new LinearLayoutManager(ActivitySearchNav.this));
        ApiInterface apiInterface = ApiClient.getRetrofit(getApplicationContext()).create(ApiInterface.class);
        Call<MovieModel> call = apiInterface.getMovieItems(input_movie);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                MovieModel data = response.body();
                if (data.getResults().size() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.data_nothing, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    recyclerView.setAdapter(new MovieAdapter(data.getResults(), R.layout.content_recycler, getApplicationContext()));
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Gagal", Toast.LENGTH_SHORT).show();
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
