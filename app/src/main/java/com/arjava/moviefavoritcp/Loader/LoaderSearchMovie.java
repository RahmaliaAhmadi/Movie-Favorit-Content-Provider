package com.arjava.moviefavoritcp.Loader;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.arjava.moviefavoritcp.BuildConfig;
import com.arjava.moviefavoritcp.activity.SearchActivity;
import com.arjava.moviefavoritcp.model.MovieModel;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

import static com.arjava.moviefavoritcp.MainActivity.BASE_URL;
import static com.arjava.moviefavoritcp.MainActivity.LANGUAGE_MOVIE;

/*
 * Created by arjava on 12/15/17.
 */

public class LoaderSearchMovie extends AsyncTaskLoader<ArrayList<MovieModel>> {

    private ArrayList<MovieModel> sDataMovie;
    private static SearchActivity parent;
    private boolean hasResult = false;
    private String input_movie;
    private String TAG = "LoaderSearchMovie";

    public LoaderSearchMovie(Context context, String input_movie) {
        super(context);
        this.input_movie = input_movie;
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged()){
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
            onReleaseResources(sDataMovie);
            hasResult = false;
        }
    }

    private void onReleaseResources(ArrayList<MovieModel> sDataMovie) {

    }

    @Override
    public ArrayList<MovieModel> loadInBackground() {

        final ArrayList<MovieModel> movieModels = new ArrayList<>();
        String urlSearchMovie = "search/movie?api_key=";
        String url = BASE_URL + urlSearchMovie + BuildConfig.API_KEY + LANGUAGE_MOVIE + input_movie;
        Log.d(TAG, "loadInBackground: LoaderSeachActivity = "+input_movie);
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
                    int total_result = responseJson.getInt("total_results");
                    JSONArray list = responseJson.getJSONArray("results");

                    if (total_result != 0){
                        for (int i = 0; i < list.length(); i++ ) {
                            JSONObject object = list.getJSONObject(i);
                            MovieModel movieModel = new MovieModel(object);
                            movieModels.add(movieModel);
                        }
                    } else {
                        Log.d(TAG, "onSuccess: Tidak ada hasil pencarian dengan kata = "+ input_movie);
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
