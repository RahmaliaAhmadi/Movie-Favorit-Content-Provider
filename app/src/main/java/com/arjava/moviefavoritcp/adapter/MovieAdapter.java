package com.arjava.moviefavoritcp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arjava.moviefavoritcp.R;
import com.arjava.moviefavoritcp.activity.DetailsMovie;
import com.arjava.moviefavoritcp.model.MovieModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/*
 * Created by arjava on 11/15/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //create Object inisiasi
    private ArrayList<MovieModel> movieItemsList;

    private Context context;
    private String TAG = MovieAdapter.class.getSimpleName();

    public ArrayList<MovieModel> getMovieItemsList() {
        return movieItemsList;
    }
    public void setMovieItemsList(ArrayList<MovieModel> movieItemsList) {
        this.movieItemsList = movieItemsList;
        notifyDataSetChanged();
    }

    //konstruktor
    public MovieAdapter(Context context) {
        this.context = context;
    }

    //mengatur tampilan layout
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_recycler, parent, false);
        return new MovieViewHolder(view);
    }

    //menampilkan hasil dari data yang kita ambil dari API
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //mengambil posisi
        final MovieModel posisi = getMovieItemsList().get(position);

        //membuat holder
        final MovieAdapter.MovieViewHolder movieViewHolder = (MovieAdapter.MovieViewHolder) holder;

        //poster_id (untuk mengambil gambar)
        final String id_poster = posisi.getPoster_path();
        String url_image = "http://image.tmdb.org/t/p/w342/";
        final String poster_image = url_image +id_poster;

        //menampilkan ke dalam textView
        movieViewHolder.textViewTitle.setText(posisi.getTitle());
        movieViewHolder.textViewDescription.setText(posisi.getOverview());
        movieViewHolder.textViewdate.setText(posisi.getRelease_date());

        Log.d(TAG, "onBindViewHolder: "+getItemCount());

        //menampilkan gambar
        Glide
                .with(context)
                .load(poster_image)
                .centerCrop()
                .override(300, 400)
                .placeholder(R.drawable.ic_crop_original_purple_300_24dp)
                .into(movieViewHolder.imageView);

        //penanganan ketika button detail diklik
        movieViewHolder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int ID_MOVIE = posisi.getId();
                MovieModel movieField = new MovieModel();
                movieField.setMovie_id(ID_MOVIE);
                movieField.setMovie_id(posisi.getMovie_id());
                movieField.setTitle(posisi.getTitle());
                movieField.setOriginal_title(posisi.getOriginal_title());
                movieField.setOverview(posisi.getOverview());
                movieField.setRelease_date(posisi.getRelease_date());
                movieField.setPoster_path(posisi.getPoster_path());
                movieField.setBackdrop_path(posisi.getBackdrop_path());
                movieField.setVote_average(posisi.getVote_average());

                Intent intent = new Intent(view.getContext(), DetailsMovie.class);
                intent.putExtra(DetailsMovie.EXTRA_MOVIE, movieField);
                context.startActivity(intent);
            }
        });

        //penanganan ketika button share diklik
        movieViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String base_url_info_film = "https://www.themoviedb.org/movie/";

                // share url_info_film menggunakan intent
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, base_url_info_film+posisi.getId());
                view.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }
        });

    }

    //inisiasi object view
    static class MovieViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView imageView;
        TextView textViewTitle, textViewDescription, textViewdate;
        Button detail, share;

        MovieViewHolder(View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.linearLayoutContentMovie);
            imageView = itemView.findViewById(R.id.imageViewItemMovie);
            textViewTitle = itemView.findViewById(R.id.textViewItemTitle);
            textViewDescription = itemView.findViewById(R.id.textViewItemDesc);
            textViewdate = itemView.findViewById(R.id.textViewItemDate);
            detail = itemView.findViewById(R.id.btnDetail);
            share = itemView.findViewById(R.id.btnShare);

        }
    }

    //mengambil posisi
    @Override
    public int getItemCount() {
        return getMovieItemsList() == null ? 0: getMovieItemsList().size();
    }
}
