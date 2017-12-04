package com.arjava.moviefavoritcp.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by arjava on 11/20/17.
 */

public class NowPlayingFragment extends Fragment {

    private ProgressBar progresBar;

    private static final String TAG = NowPlayingFragment.class.getSimpleName();
    private CardView cardNoConnected;

    public NowPlayingFragment() {
        //empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cardNoConnected.setVisibility(View.GONE);
        if (isOnline()) {
            loadMovieNow();
        } else {
            cardNoConnected.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nowplaying, container, false);
        progresBar = view.findViewById(R.id.progressBarMain);
        cardNoConnected = view.findViewById(R.id.card_not_connect);
        return view;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void loadMovieNow() {

        progresBar.setVisibility(View.VISIBLE);
        final RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ApiInterface apiInterface = ApiClient.getRetrofit(getContext()).create(ApiInterface.class);
        Call<MovieModel> call = apiInterface.getNowPlaying();
        call.enqueue(new Callback<MovieModel>() {
            //ketika server meresponse
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                MovieModel data = response.body();
                if (data.getResults().size() == 0) {
                    Toast.makeText(getContext(), "maaf data yang anda cari tidak ditemukan", Toast.LENGTH_SHORT).show();
                    progresBar.setVisibility(View.GONE);
                } else {
                    recyclerView.setAdapter(new MovieAdapter(data.getResults(), R.layout.content_recycler, getContext()));
                    Log.e(TAG, "onResponse: hasil pemanggilan" + call);
                    progresBar.setVisibility(View.GONE);
                }
            }

            //ketika gagal mendapatkan response
            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal", Toast.LENGTH_SHORT).show();
                Log.d(TAG, t.toString());
                progresBar.setVisibility(View.GONE);
                cardNoConnected.setVisibility(View.VISIBLE);
            }
        });
    }
}
