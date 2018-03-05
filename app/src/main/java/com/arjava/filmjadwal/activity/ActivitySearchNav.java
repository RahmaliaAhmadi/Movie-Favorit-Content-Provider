package com.arjava.filmjadwal.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.arjava.filmjadwal.Loader.LoaderSearchMovie;
import com.arjava.filmjadwal.R;
import com.arjava.filmjadwal.adapter.MovieAdapter;
import com.arjava.filmjadwal.model.MovieModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("Registered")
public class ActivitySearchNav extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieModel>> {

    private MovieAdapter adapter;

    @BindView(R.id.progressBarSnav)
    ProgressBar progressBar;
    @BindView(R.id.edtSearch)
    EditText inputSearch;
    @BindView(R.id.btnSearch)
    Button btnSearch;
    @BindView(R.id.recyclerMovie)
    RecyclerView recyclerView;
    @BindView(R.id.card_error_load)
    CardView cardView_load_error;
    static final String EXTRAS_MOVIE = "EXTRAS_MOVIE";

    public ActivitySearchNav() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_nav);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search_nav);
        getSupportLoaderManager().initLoader(0, null, this);
        adapter = new MovieAdapter(ActivitySearchNav.this);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        cardView_load_error.setVisibility(View.GONE);

        String query_search = inputSearch.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_MOVIE, query_search);

        getSupportLoaderManager().initLoader(0, bundle, this);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
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

    @OnClick(R.id.btnSearch)
    public void setBtnSearch(View view) {
        if (view.getId() == R.id.btnSearch) {
            recyclerView.setVisibility(View.INVISIBLE);
            String querySsearch = inputSearch.getText().toString();

            if (TextUtils.isEmpty(querySsearch)) return;
            Bundle bundle = new Bundle();
            bundle.putString(EXTRAS_MOVIE, querySsearch);
            getSupportLoaderManager().restartLoader(0, bundle, this);

        }
        hideSoftKeyboard(this);
    }

    @Override
    public Loader<ArrayList<MovieModel>> onCreateLoader(int id, Bundle args) {

        progressBar.setVisibility(View.VISIBLE);
        cardView_load_error.setVisibility(View.GONE);

        String queryMovie = "";
        if (args != null) {
            queryMovie = args.getString(EXTRAS_MOVIE);
            new LoaderSearchMovie(this, queryMovie);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        return new LoaderSearchMovie(this, queryMovie);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> data) {
        if (data.size() != 0) {
            cardView_load_error.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setMovieItemsList(data);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.INVISIBLE);
            cardView_load_error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {
        adapter.setMovieItemsList(null);
    }
}
