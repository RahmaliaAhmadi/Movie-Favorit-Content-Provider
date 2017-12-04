package com.arjava.moviefavoritcp.request;

import com.arjava.moviefavoritcp.BuildConfig;
import com.arjava.moviefavoritcp.model.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
 * Created by arjava on 11/17/17.
 */

public interface ApiInterface {

    //endpoint untuk pencarian
    @GET("/3/search/movie?api_key="+ BuildConfig.API_KEY +"&language=en-US&")
    Call<MovieModel> getMovieItems(
            @Query("query") String name_movie
    );

    //endpoint untuk upcoming
    @GET("/3/movie/upcoming?api_key="+ BuildConfig.API_KEY+ "&language=en-US")
    Call<MovieModel> getMovieUpcoming();

    //endpoint untuk now playing
    @GET("/3/movie/now_playing?api_key="+ BuildConfig.API_KEY +"&language=en-ID")
    Call<MovieModel> getNowPlaying();

    //endpoint untuk top rated
    @GET("/3/movie/top_rated?api_key="+ BuildConfig.API_KEY + "&language=en-US")
    Call<MovieModel> getTopRated();

}
