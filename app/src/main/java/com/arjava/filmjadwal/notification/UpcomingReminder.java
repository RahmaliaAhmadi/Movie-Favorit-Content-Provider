package com.arjava.filmjadwal.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.arjava.filmjadwal.BuildConfig;
import com.arjava.filmjadwal.R;
import com.arjava.filmjadwal.activity.DetailsMovie;
import com.arjava.filmjadwal.model.MovieModel;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

/**
 *
 * Created by arjava on 12/28/17.
 */

public class UpcomingReminder extends GcmTaskService {

    public static final String TAG = "TAG_UpComing_Service";
    public static String TAG_TASK_UPCOMING_LOG = "UpcomingTask";
    private int notifId = 100;

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_TASK_UPCOMING_LOG)) {
            getUpComingMovieData();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }
        return result;
    }

    private void getUpComingMovieData() {
        final ArrayList<MovieModel> movieItemses = new ArrayList<>();
        SyncHttpClient client = new SyncHttpClient();
        String url = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + BuildConfig.API_KEY + "&language=en-US";
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    int listSize;
                    listSize = list.length();
                    Random r = new Random();
                    int randomJsonIndex = r.nextInt(listSize);

                    JSONObject movie = list.getJSONObject(randomJsonIndex);
                    MovieModel movieItems = new MovieModel(movie);
                    movieItemses.add(movieItems);

                    int id_movie = movieItemses.get(0).getId();
                    String title = movieItemses.get(0).getTitle();
                    String message = getString(R.string.upcoming_reminder) + title + getString(R.string.release);
                    showNotification(getApplicationContext(), id_movie, title, message, ++notifId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        UpComingTask mUpComingTask = new UpComingTask(this);
        mUpComingTask.createPeriodicTask();
    }

    private void showNotification(Context context, int id, String title, String message, int notifId) {
        Intent notifDetailIntent = new Intent(context, DetailsMovie.class);
        notifDetailIntent.putExtra(DetailsMovie.EXTRA_ID, id);
        notifDetailIntent.putExtra(DetailsMovie.EXTRA_TITLE, title);

        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addParentStack(DetailsMovie.class)
                .addNextIntent(notifDetailIntent)
                .getPendingIntent(notifId, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(notifId, builder.build());
    }

    public static class UpComingTask {

        private GcmNetworkManager mGcmNetworkManager;

        public UpComingTask(Context context) {
            mGcmNetworkManager = GcmNetworkManager.getInstance(context);
        }

        public void createPeriodicTask() {
            Task periodicTask = new PeriodicTask.Builder()
                    .setService(UpcomingReminder.class)
                    .setPeriod(60)
                    .setFlex(10)
                    .setTag(UpcomingReminder.TAG_TASK_UPCOMING_LOG)
                    .setPersisted(true)
                    .build();

            mGcmNetworkManager.schedule(periodicTask);
            Log.d("TAG", "createPeriodicTask: 1");
        }

    }
}
