package com.arjava.filmjadwal.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.arjava.filmjadwal.BuildConfig;
import com.arjava.filmjadwal.R;
import com.arjava.filmjadwal.database.MovieHelper;
import com.arjava.filmjadwal.model.MovieModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 * Created by arjava on 12/26/17.
 */

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<MovieModel> sWidgetItems = new ArrayList<>();
    private Context sContext;
    private MovieHelper movieHelper;

    public StackRemoteViewsFactory(Context context) {
        sContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        movieHelper = new MovieHelper(sContext);
        movieHelper.open();
        sWidgetItems.addAll(movieHelper.getDataAll());
    }

    @Override
    public void onDestroy() {
        if (movieHelper != null) {
            movieHelper.close();
        }
    }

    @Override
    public int getCount() {
        return sWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(sContext.getPackageName(), R.layout.item_widget);

        Bitmap bmp = null;
        try {

            bmp = Glide.with(sContext)
                    .load(BuildConfig.URL_POSTER + sWidgetItems.get(position).getPoster_path())
                    .asBitmap()
                    .error(new ColorDrawable(sContext.getResources().getColor(R.color.colorAccent)))
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();

        }catch (InterruptedException | ExecutionException e){
            Log.d("Widget Load Error","error");
        }

        views.setImageViewBitmap(R.id.imageView,bmp);
        Bundle extras = new Bundle();
        extras.putInt(StackWidgetMovie.EXTRA_ITEM, position);

        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        views.setOnClickFillInIntent(R.id.imageView, fillIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
