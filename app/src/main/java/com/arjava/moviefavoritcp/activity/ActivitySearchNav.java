package com.arjava.moviefavoritcp.activity;

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
import android.widget.Toast;

import com.arjava.moviefavoritcp.Loader.LoaderSearchMovie;
import com.arjava.moviefavoritcp.R;
import com.arjava.moviefavoritcp.adapter.MovieAdapter;
import com.arjava.moviefavoritcp.model.MovieModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("Registered")
public class ActivitySearchNav extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieModel>> {

    private MovieAdapter adapter;
    private static final String QUERY_SEARCH = "query_search_keep_when_change_orientation";
    private String query_search = "input_search";

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

    public ActivitySearchNav() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_nav);
        ButterKnife.bind(this);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        adapter = new MovieAdapter(ActivitySearchNav.this);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.INVISIBLE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.search_nav);
        inputSearch.setText(QUERY_SEARCH);
        getSupportLoaderManager().initLoader(0, null, this);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(QUERY_SEARCH, inputSearch.getText().toString());
        super.onSaveInstanceState(outState);
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
            query_search = inputSearch.getText().toString();

            boolean isEmpty = false;

            if (TextUtils.isEmpty(query_search)) {
                isEmpty = true;
                Toast.makeText(this, R.string.data_nothing, Toast.LENGTH_SHORT).show();
            }
            if (!isEmpty) {
                getSupportLoaderManager().initLoader(0, null, this);
            }
        }
        hideSoftKeyboard(this);
    }

    @Override
    public Loader<ArrayList<MovieModel>> onCreateLoader(int id, Bundle args) {

        query_search = inputSearch.getText().toString();

        if (!TextUtils.isEmpty(query_search)) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            return new LoaderSearchMovie(this, query_search);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> data) {
        if (data.size() != 0) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setMovieItemsList(data);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.INVISIBLE);
            cardView_load_error.setVisibility(View.VISIBLE);
            cardView_load_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getSupportLoaderManager().restartLoader(0, null, ActivitySearchNav.this);
                    cardView_load_error.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {
        adapter.setMovieItemsList(null);
    }
}
