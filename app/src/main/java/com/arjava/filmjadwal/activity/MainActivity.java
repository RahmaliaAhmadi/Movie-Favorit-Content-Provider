package com.arjava.filmjadwal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.arjava.filmjadwal.R;
import com.arjava.filmjadwal.fragment.NowPlayingFragment;
import com.arjava.filmjadwal.fragment.UpcomingFragment;
import com.arjava.filmjadwal.notification.DailyReminder;
import com.arjava.filmjadwal.notification.UpcomingReminder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String LANGUAGE_MOVIE = "&language=en-US";
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    SearchView searchView;
    NavigationView navigationView;
    private DrawerLayout drawer;

    //notification
    DailyReminder dailyReminder;
    UpcomingReminder.UpComingTask upComingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPagers);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabsLyout);
        tabLayout.setupWithViewPager(viewPager);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchView = findViewById(R.id.search_name);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getResources().getString(R.string.search));

        dailyReminder = new DailyReminder();
        upComingTask = new UpcomingReminder.UpComingTask(MainActivity.this);
        dailyReminder.setDailyReminderMovieAlarm(this, DailyReminder.TYPE_DAILY_REMINDER,
                getString(R.string.message_daily));
        upComingTask.createPeriodicTask();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new NowPlayingFragment(), getResources().getString(R.string.now_playing));
        adapter.addFrag(new UpcomingFragment(), getResources().getString(R.string.up_coming));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        searchView.clearFocus();
        if (s.isEmpty() || s.equals("."+","+"~"+"!"+"@"+"#"+"$"+"%"+"^"+"&"+"*"+"("+")"+"_"+"+"+"-"+"=")) {
            Toast.makeText(getApplicationContext(), "tidak boleh kosong atau menggunakan karakter \n" +
                    " . , ~ ! @ # $ % ^ & * ( ) _ + - =", Toast.LENGTH_SHORT).show();
        }else{
            Intent ts = new Intent(MainActivity.this, SearchActivity.class);
            ts.putExtra("cari_film", s);
            startActivity(ts);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        getIntent().putExtra("cari_film", s);

        if(s.length() > 15){
            Toast.makeText(getApplicationContext(), "Nama yang anda input terlalu panjang !", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> stringList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        public int getCount() {
            return fragmentList.size();
        }

        void addFrag(Fragment fragment, String title) {
            fragmentList.add(fragment);
            stringList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return stringList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setMessage(R.string.exit_msg)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            alert.create();
            alert.show();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_top_rated) {
            startActivity(new Intent(MainActivity.this, TopRatedActivity.class));
        } else if (id == R.id.nav_search) {
            startActivity(new Intent(MainActivity.this, ActivitySearchNav.class));
        }else if (id == R.id.nav_favorite) {
            startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        } else if (id == R.id.nav_share) {
            Intent intent;
            String publisher = "arjavax";
            try {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("market://search?q=pub:" + publisher));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/search?q=pub:" + publisher)));
            }
        } else if (id == R.id.nav_language){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setClassName("com.android.settings", "com.android.settings.LanguageSettings");
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
