package com.iexamcenter.odiacalendar;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.iexamcenter.odiacalendar.sqlite.SqliteHelper;
import com.iexamcenter.odiacalendar.utility.BottomNavigationViewBehavior;
import com.iexamcenter.odiacalendar.utility.LocaleHelper;
import com.iexamcenter.odiacalendar.utility.PrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public BottomNavigationView bottomNavigationView;
    public NavigationView navigationView;
    public Toolbar toolbar;
    DrawerLayout drawer;

    public void lockUnlockDrawer(Boolean isLock) {
        if (isLock)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        else
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    public void doRssUpdateInBackground() {
        String[] mRssArr = getResources().getStringArray(R.array.rss);
        Uri contenturi_rss = Uri.withAppendedPath(SqliteHelper.CONTENT_URI_RSS, SqliteHelper.RssEntry.TABLE_RSS);

        ArrayList<ContentValues> cva = new ArrayList<>();
        int i = 0;
        for (String rssStr : mRssArr) {
            // String rssStr = mRssArr[i];
            String[] rssStrArr = rssStr.split(Pattern.quote("],["));
            String rssLink = rssStrArr[4];
            String rssChannel = rssStrArr[2];
            String rssCat = rssStrArr[3];
            String rssLang = rssStrArr[0];
            String rssType = rssStrArr[1];
            ContentValues cv = new ContentValues();
            cv.put(SqliteHelper.RssEntry.KEY_RSS_LINK, rssLink);
            cv.put(SqliteHelper.RssEntry.KEY_RSS_CHANNEL, rssChannel);
            cv.put(SqliteHelper.RssEntry.KEY_RSS_TYPE, rssType);
            cv.put(SqliteHelper.RssEntry.KEY_RSS_CAT, rssCat);
            cv.put(SqliteHelper.RssEntry.KEY_RSS_LANG, rssLang);
            //if (cursor1.getCount() == 0) {
            cv.put(SqliteHelper.RssEntry.KEY_RSS_SUBSCRIBE, "0");
            cv.put(SqliteHelper.RssEntry.KEY_RSS_ORDER, "" + i++);
            cv.put(SqliteHelper.RssEntry.KEY_RSS_DELETE, "0");
            cv.put(SqliteHelper.RssEntry.KEY_RSS_LAST_MODIFIED, "0");
            cv.put(SqliteHelper.RssEntry.KEY_RSS_PUBLISH, "1");


            cva.add(cv);
        }
        ContentValues[] bulkToInsert = new ContentValues[cva.size()];

        bulkToInsert = cva.toArray(bulkToInsert);
        getContentResolver().bulkInsert(contenturi_rss, bulkToInsert);

    }

    private void addUpdateRss() {

        MyRxJava.newInstance().rssInBackground(this);
/*
        new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {

                String[] mRssArr = mContext.getResources().getStringArray(R.array.rss);
                Uri contenturi_rss = Uri.withAppendedPath(SqliteHelper.CONTENT_URI_RSS, SqliteHelper.RssEntry.TABLE_RSS);

                ArrayList<ContentValues> cva = new ArrayList<>();
                int i = 0;
                for (String rssStr : mRssArr) {
                    // String rssStr = mRssArr[i];
                    String[] rssStrArr = rssStr.split(Pattern.quote("],["));
                    String rssLink = rssStrArr[4];
                    String rssChannel = rssStrArr[2];
                    String rssCat = rssStrArr[3];
                    String rssLang = rssStrArr[0];
                    String rssType = rssStrArr[1];
                    ContentValues cv = new ContentValues();
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_LINK, rssLink);
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_CHANNEL, rssChannel);
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_TYPE, rssType);
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_CAT, rssCat);
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_LANG, rssLang);
                    //if (cursor1.getCount() == 0) {
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_SUBSCRIBE, "0");
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_ORDER, "" + i++);
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_DELETE, "0");
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_LAST_MODIFIED, "0");
                    cv.put(SqliteHelper.RssEntry.KEY_RSS_PUBLISH, "1");


                    cva.add(cv);
                }
                ContentValues[] bulkToInsert = new ContentValues[cva.size()];

                bulkToInsert = cva.toArray(bulkToInsert);
                mContext.getContentResolver().bulkInsert(contenturi_rss, bulkToInsert);


                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
            }

        }.execute();

*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PrefManager mPref = PrefManager.getInstance(this);
        mPref.load();
        if (mPref.isFirstUse()) {
            mPref.setFirstUse(false);
            addUpdateRss();
        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //  drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag(AppConstants.FRAG_MAIN_PAGER_TAG);
        FragmentTransaction ft = fm.beginTransaction();
        if (frag == null)
            frag = RootFragment.newInstance();
        ft.replace(R.id.constraintLayout, frag, AppConstants.FRAG_MAIN_PAGER_TAG);
        ft.addToBackStack(AppConstants.FRAG_MAIN_PAGER_TAG);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void changeTheme(TabLayout tabLayout, int page, int clr) {

        //  tabLayout.setBackgroundColor(clr);
        bottomNavigationView.setBackgroundColor(clr);
        toolbar.setBackgroundColor(clr);
        setStatusBarColor(clr);

    }


    public void setStatusBarColor(Integer clr) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(clr);
        }
    }

    public Integer[] getPageColors() {
        MainActivity mContext = this;

        Integer[] colors = new Integer[]{mContext.getResources().getColor(R.color.page1), mContext.getResources().getColor(R.color.page2), mContext.getResources().getColor(R.color.page3), mContext.getResources().getColor(R.color.page4)
                , mContext.getResources().getColor(R.color.page5)};
        return colors;

    }




}
