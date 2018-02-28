package com.iexamcenter.odiacalendar.month;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.iexamcenter.odiacalendar.CalendarWeatherApp;
import com.iexamcenter.odiacalendar.MainActivity;

import java.util.Calendar;


/**
 * Created by sasikanta on 11/14/2017.
 * CalendarPagerAdapter
 */

class CalendarPagerAdapter extends FragmentStatePagerAdapter {
    private String[] pageTitle;
    MainActivity activity;
    int maxPage = 60;
    int currPage = 25;
  //  String[] l_month_arr, e_month_arr;
    int monthKey;

    private CalendarPagerAdapter(FragmentManager fm, FragmentActivity activity, String[] subPageTitle,int monthKey) {
        super(fm);
        pageTitle = subPageTitle;
        this.activity = (MainActivity) activity;
        this.monthKey=monthKey;

     //   l_month_arr = activity.getResources().getStringArray(R.array.l_month_arr);
       // e_month_arr = activity.getResources().getStringArray(R.array.e_month_arr);

    }

    public static CalendarPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, String[] subPageTitle,int monthKey) {
        return new CalendarPagerAdapter(fm, activity, subPageTitle,monthKey);
    }

    @Override
    public Fragment getItem(int position) {
        int page = position - currPage+monthKey;
        Fragment fragment = CalendarPagerFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(CalendarPagerFragment.ARG_POSITION, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, position - currPage+monthKey);
        int monthIndex = cal.get(Calendar.MONTH);
        //String title = l_month_arr[monthIndex] + "(" + e_month_arr[monthIndex].toUpperCase() + ")";
        String title = CalendarWeatherApp.l_month_arr[monthIndex];

        return title;
/*
      //  Drawable image = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_nav_calendar);
        //image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("  " + title);
        ImageSpan imageSpan = new CenteredImageSpan(activity.getApplicationContext(), R.drawable.ic_nav_calendar);

        sb.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
        */
    }

    @Override
    public int getCount() {
        return maxPage;
        //return pageTitle.length;
    }
}
