package com.iexamcenter.odiacalendar;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.iexamcenter.odiacalendar.event.EventMainFragment;
import com.iexamcenter.odiacalendar.home.HomeMainFragment;
import com.iexamcenter.odiacalendar.month.CalendarMainFragment;
import com.iexamcenter.odiacalendar.omg.OmgMainFragment;
import com.iexamcenter.odiacalendar.weather.WeatherMainFragment;

/**
 * Created by sasikanta on 11/14/2017.
 * RootPagerAdapter
 */

public class RootPagerAdapter extends FragmentPagerAdapter {
    MainActivity activity;
    RootPagerAdapter(FragmentManager fm, MainActivity activity) {
        super(fm);
        this.activity=activity;
    }

    @Override
    public Fragment getItem(int position) {
        Resources res = activity.getResources();
        switch (position) {
            case 0:
                return HomeMainFragment.newInstance(res.getStringArray(R.array.tab_home));
            case 1:
                return OmgMainFragment.newInstance(res.getStringArray(R.array.tab_omg));
            case 2:
                return WeatherMainFragment.newInstance(res.getStringArray(R.array.tab_weather));
            case 3:
                return CalendarMainFragment.newInstance(res.getStringArray(R.array.tab_calendar));
            case 4:
                return EventMainFragment.newInstance(res.getStringArray(R.array.tab_event));
            default:
                return HomeMainFragment.newInstance(res.getStringArray(R.array.tab_home));

        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}
