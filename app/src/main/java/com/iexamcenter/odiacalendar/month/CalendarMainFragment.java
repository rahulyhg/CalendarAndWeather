package com.iexamcenter.odiacalendar.month;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ToxicBakery.viewpager.transforms.AccordionTransformer;
import com.iexamcenter.odiacalendar.CalendarWeatherApp;
import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.utility.MenuDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * QuoteMainFragment
 */

public class CalendarMainFragment extends Fragment {
    MainActivity activity;
    Resources res;
    TabLayout tabLayout;
    int monthKey = 0;
    static String[] subPagesTitle;
    ViewPager tabViewPager;
    CalendarPagerAdapter tabViewpagerAdapter;

    public static CalendarMainFragment newInstance(String[] subPages) {
        subPagesTitle = subPages;
        return new CalendarMainFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_calendar_main, container, false);

        res = activity != null ? activity.getResources() : null;
        setRetainInstance(true);
        setUp(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            changeCalendar(0);

        } else if (id == R.id.action_calendar) {
            createDialogWithoutDateField();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void createDialogWithoutDateField() {
        ArrayList<String> monthPopupArr = new ArrayList<>();

        for (int i = 10; i > 1; i--) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, i);
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.getDefault());
            String month_name = month_date.format(cal.getTime());
            monthPopupArr.add(i + "," + month_name);
        }

        FragmentTransaction ft1 = activity.getFragmentManager().beginTransaction();
        android.app.Fragment prev1 = activity.getFragmentManager().findFragmentByTag("nextprevmonth");
        if (prev1 != null) {
            ft1.remove(prev1);
        }
        final DialogFragment momentDialog = MenuDialog.newInstance(monthPopupArr, 2, 100, 1, CalendarMainFragment.this);
        momentDialog.setCancelable(true);
        momentDialog.show(ft1, "nextprevmonth");
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }

    public void changeCalendar(int monthKey) {
        this.monthKey = monthKey;
        tabViewpagerAdapter = CalendarPagerAdapter.newInstance(getChildFragmentManager(), activity, subPagesTitle, monthKey);
        tabViewPager.setAdapter(tabViewpagerAdapter);
        tabViewPager.setCurrentItem(25);
        tabViewPager.setPageTransformer(true, new AccordionTransformer());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthKey);
        int monthIndex = cal.get(Calendar.MONTH);
        activity.toolbar.setTitle(CalendarWeatherApp.e_month_arr[monthIndex].toUpperCase());
        activity.toolbar.setSubtitle("" + cal.get(Calendar.YEAR));


    }

    protected void setUp(View rootView) {
        long start = System.currentTimeMillis();
        Log.e("TIME:", "TIMESTART:" + start);
        tabViewPager = rootView.findViewById(R.id.tabViewPager12);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabViewpagerAdapter = CalendarPagerAdapter.newInstance(getChildFragmentManager(), activity, subPagesTitle, monthKey);
        tabViewPager.setAdapter(tabViewpagerAdapter);
        tabViewPager.setCurrentItem(25);
        tabViewPager.setPageTransformer(true, new AccordionTransformer());
        long end = System.currentTimeMillis();
        Log.e("TIME:", "TIMESTART:" + end);
        Log.e("TIME:", "TIMESTART:" + (end - start));


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthKey);
        int monthIndex = cal.get(Calendar.MONTH);
        activity.toolbar.setTitle(CalendarWeatherApp.e_month_arr[monthIndex].toUpperCase());
        activity.toolbar.setSubtitle("" + cal.get(Calendar.YEAR));

        tabViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.MONTH, position - 25 + monthKey);
                int monthIndex = cal.get(Calendar.MONTH);
                // String title=e_month_arr[monthIndex].toUpperCase()+"("+l_month_arr[monthIndex]+")";
                activity.toolbar.setTitle(CalendarWeatherApp.e_month_arr[monthIndex].toUpperCase());
                activity.toolbar.setSubtitle("" + cal.get(Calendar.YEAR));


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

}