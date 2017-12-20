package com.arjava.moviefavoritcp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.arjava.moviefavoritcp.Loader.LoaderNowPlaying;
import com.arjava.moviefavoritcp.R;
import com.arjava.moviefavoritcp.adapter.MovieAdapter;
import com.arjava.moviefavoritcp.model.MovieModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by arjava on 11/20/17.
 */

public class NowPlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieModel>> {

    @BindView(R.id.progressBarMain) ProgressBar progresBar;
    @BindView(R.id.card_error_load)
    CardView cardNoConnected;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private MovieAdapter adapter;
    private static final String TAG = NowPlayingFragment.class.getSimpleName();

    public NowPlayingFragment() {
        // dibutuhkan public constructor kosong
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        /*if (savedInstanceState != null) {
            getActivity().getSupportLoaderManager().initLoader(1, null, this);
        }*/
        getActivity().getSupportLoaderManager().initLoader(0,null, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nowplaying, container, false);
        ButterKnife.bind(this, view);

        adapter = new MovieAdapter(getActivity());
        cardNoConnected.setVisibility(View.GONE);
        recyclerView.setVisibility(View.INVISIBLE);

        return view;
    }

    @Override
    public Loader<ArrayList<MovieModel>> onCreateLoader(int id, Bundle args) {
        progresBar.setVisibility(View.VISIBLE);
        cardNoConnected.setVisibility(View.GONE);

        return new LoaderNowPlaying(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieModel>> loader, ArrayList<MovieModel> data) {

        Log.d(TAG, "onLoadFinished: DATA = "+ data);
        if (data.size() != 0) {
            progresBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.setMovieItemsList(data);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);
        } else {
            progresBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.INVISIBLE);
            cardNoConnected.setVisibility(View.VISIBLE);
            cardNoConnected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportLoaderManager().restartLoader(0, null, NowPlayingFragment.this);
                    cardNoConnected.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieModel>> loader) {
        adapter.setMovieItemsList(null);
    }
}
