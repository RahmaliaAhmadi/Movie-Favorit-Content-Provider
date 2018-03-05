package com.arjava.filmjadwal.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.arjava.filmjadwal.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by arjava on 11/23/17.
 */

@SuppressLint("Registered")
public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.img_facebook) ImageView facebook;
    @BindView(R.id.img_github) ImageView github;
    @BindView(R.id.img_linkedin) ImageView linkedin;
    Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        facebook.setOnClickListener(this);
        github.setOnClickListener(this);
        linkedin.setOnClickListener(this);

        getSupportActionBar().setTitle(R.string.about);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.img_facebook) {
            try {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("https://www.facebook.com/As.rokhman"));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/As.rokhman")));
            }
        } else if (view.getId()==R.id.img_github) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/arjava")));
        } else if (view.getId()==R.id.img_linkedin) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/arjava/")));
        }
    }
}
