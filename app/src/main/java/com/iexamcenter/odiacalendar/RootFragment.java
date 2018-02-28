package com.iexamcenter.odiacalendar;


import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iexamcenter.odiacalendar.event.EventMainFragment;
import com.iexamcenter.odiacalendar.home.HomeMainFragment;
import com.iexamcenter.odiacalendar.month.CalendarMainFragment;
import com.iexamcenter.odiacalendar.omg.OmgMainFragment;
import com.iexamcenter.odiacalendar.weather.WeatherMainFragment;

/**
 * Created by Sasikanta_Sahoo on 12/1/2017.
 * RootFragment
 */

public class RootFragment extends Fragment {

    MenuItem prevMenuItem;
    BottomNavigationView bottomNavigationViewHelper;
    BottomNavigationView bottomNavigationView;
    ViewPager mPager;
    MainActivity activity;
    ImageView mSwipeStart, mSwipeEnd;
    final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    public static Fragment newInstance() {
        return new RootFragment();
    }

    public void selectPager(int page) {
        mPager.setCurrentItem(page);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_root_page, container, false);
        bottomNavigationView = activity.bottomNavigationView;
        setRetainInstance(false);
        setUp(rootView);
        return rootView;
    }


    protected void setUp(View rootView) {
        mPager = rootView.findViewById(R.id.pager);
        RootPagerAdapter mPagerAdapter = new RootPagerAdapter(activity.getSupportFragmentManager(),activity);
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                smoothChange(position, positionOffset, positionOffsetPixels);

            }

            @Override
            public void onPageSelected(int position) {

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
          /*      if(position!=0)
                activity.lockUnlockDrawer(true);
                else
                    activity.lockUnlockDrawer(false);
                    */
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int position = item.getItemId();
            Integer[] colors = activity.getPageColors();

            switch (position) {

                case R.id.nav_home:
                    mPager.setCurrentItem(0);
                    changeTheme(colors[0]);
                    break;
                case R.id.nav_omg:
                    mPager.setCurrentItem(1);
                    changeTheme(colors[1]);
                    break;
                case R.id.nav_weather:
                    mPager.setCurrentItem(2);
                    changeTheme(colors[2]);
                    break;
                case R.id.nav_calendar:
                    mPager.setCurrentItem(3);
                    changeTheme(colors[3]);
                    break;
                case R.id.nav_event:
                    mPager.setCurrentItem(4);
                    changeTheme(colors[4]);
                    break;

            }


            return false;
        });
        Integer[] colors = activity.getPageColors();
        int colr = colors[0];

        changeTheme(colr);
    }



    public void smoothChange(int position, float positionOffset, int positionOffsetPixels) {
        Integer[] colors = activity.getPageColors();
        int cnt = colors.length + 1;

        if (position < (cnt - 1) && position < (colors.length - 1)) {
            int colr = (Integer) argbEvaluator.evaluate(positionOffset, colors[position], colors[position + 1]);
            int indCol = manipulateColor(colr, 0.9f);
            changeTheme(indCol);


        } else {
            int colr = colors[colors.length - 1];

            int indCol = manipulateColor(colr, 0.9f);
            changeTheme(indCol);

        }
    }

    public void changeTheme(int clr) {
        mPager.setBackgroundColor(clr);
        HomeMainFragment page1 = (HomeMainFragment) activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 0);
        OmgMainFragment page2 = (OmgMainFragment) activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 1);
        WeatherMainFragment page3 = (WeatherMainFragment) activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 2);
        CalendarMainFragment page4 = (CalendarMainFragment) activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 3);
        EventMainFragment page5 = (EventMainFragment) activity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + 4);

        if (page1 != null) {
            page1.getTabLayout().setBackgroundColor(clr);
            activity.changeTheme(page1.getTabLayout(), 0, clr);

        }
        if (page2 != null) {
            page2.getTabLayout().setBackgroundColor(clr);
            activity.changeTheme(page2.getTabLayout(), 1, clr);
        }
        if (page3 != null) {
            page3.getTabLayout().setBackgroundColor(clr);
            activity.changeTheme(page3.getTabLayout(), 2, clr);
        }
        if (page4 != null) {
            page4.getTabLayout().setBackgroundColor(clr);
            activity.changeTheme(page4.getTabLayout(), 3, clr);
        }
        if (page5 != null) {
            page5.getTabLayout().setBackgroundColor(clr);
            activity.changeTheme(page5.getTabLayout(), 4, clr);
        }


    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

}
