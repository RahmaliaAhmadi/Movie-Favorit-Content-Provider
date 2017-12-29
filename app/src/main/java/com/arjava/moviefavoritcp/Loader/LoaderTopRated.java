package com.arjava.moviefavoritcp.Loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.arjava.moviefavoritcp.BuildConfig;
import com.arjava.moviefavoritcp.model.MovieModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.arjava.moviefavoritcp.activity.MainActivity.BASE_URL;
import static com.arjava.moviefavoritcp.activity.MainActivity.LANGUAGE_MOVIE;

/*
 * Created by arjava on 12/16/17.
 */

public class LoaderTopRated extends AsyncTaskLoader<ArrayList<MovieModel>> {

    private ArrayList<MovieModel> sDataMovie;
    private boolean hasResult = false;
    private String TAG = LoaderTopRated.class.getSimpleName();

    public LoaderTopRated(Context context) {
        super(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        } else if (hasResult) {
            deliverResult(sDataMovie);
        }
    }

    @Override
    public void deliverResult(ArrayList<MovieModel> data) {
        sDataMovie = data;
        hasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (hasResult) {
            hasResult = false;
        }
    }

    @Override
    public ArrayList<MovieModel> loadInBackground() {

        final ArrayList<MovieModel> movieModels = new ArrayList<>();
        String urlTopRated = "movie/top_rated?api_key=";
        String url = BASE_URL + urlTopRated + BuildConfig.API_KEY + LANGUAGE_MOVIE;
        SyncHttpClient client = new SyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseJson = new JSONObject(result);
                    JSONArray list = responseJson.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++ ) {
                        JSONObject object = list.getJSONObject(i);
                        MovieModel movieModel = new MovieModel(object);
                        movieModels.add(movieModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "onFailure: Gagal Memuat data ! ");
            }
        });
        return movieModels;

    }
}
