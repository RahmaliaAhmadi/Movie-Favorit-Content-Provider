package com.arjava.filmjadwal.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/*
 * Created by arjava on 11/13/17.
 */

public class MovieModel implements Parcelable {

    /**
     * vote_count : 895
     * id : 346364
     * video : false
     * vote_average : 7.6
     * title : It
     * popularity : 581.361694
     * poster_path : /9E2y5Q7WlCVNEhP5GiVTjhEhx1o.jpg
     * original_language : en
     * original_title : It
     * genre_ids : [12,18,27]
     * backdrop_path : /tcheoA2nPATCm2vvXw2hVQoaEFD.jpg
     * adult : false
     * overview : In a small town in Maine, seven children known as The Losers Club come face to face with life problems, bullies and a monster that takes the shape of a clown called Pennywise.
     * release_date : 2017-09-05
     */

    private int vote_count;
    private int id;
    private int movie_id;
    private boolean video;
    private double vote_average;
    private String title;
    private double popularity;
    private String poster_path;
    private String original_language;
    private String original_title;
    private String backdrop_path;
    private boolean adult;
    private String overview;
    private String release_date;

    public MovieModel() {
    }

    public MovieModel(JSONObject object) {
        try {
            int id = object.getInt("id");
            int movie_id = object.getInt("id");
            String title = object.getString("title");
            String original_title = object.getString("original_title");
            String overview = object.getString("overview");
            String release_date = object.getString("release_date");
            String poster_path = object.getString("poster_path");
            String backdrop_path = object.getString("backdrop_path");
            double vote_average = object.getDouble("vote_average");

            this.id = id;
            this.movie_id = movie_id;
            this.title = title;
            this.original_title = original_title;
            this.overview = overview;
            this.release_date = release_date;
            this.poster_path = poster_path;
            this.backdrop_path = backdrop_path;
            this.vote_average = vote_average;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected MovieModel(Parcel in) {
        vote_count = in.readInt();
        id = in.readInt();
        movie_id = in.readInt();
        video = in.readByte() != 0;
        vote_average = in.readDouble();
        title = in.readString();
        popularity = in.readDouble();
        poster_path = in.readString();
        original_language = in.readString();
        original_title = in.readString();
        backdrop_path = in.readString();
        adult = in.readByte() != 0;
        overview = in.readString();
        release_date = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(vote_count);
        parcel.writeInt(id);
        parcel.writeInt(movie_id);
        parcel.writeByte((byte) (video ? 1 : 0));
        parcel.writeDouble(vote_average);
        parcel.writeString(title);
        parcel.writeDouble(popularity);
        parcel.writeString(poster_path);
        parcel.writeString(original_language);
        parcel.writeString(original_title);
        parcel.writeString(backdrop_path);
        parcel.writeByte((byte) (adult ? 1 : 0));
        parcel.writeString(overview);
        parcel.writeString(release_date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(int movie_id) {
        this.movie_id = movie_id;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}