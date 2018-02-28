package com.iexamcenter.odiacalendar.event;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.iexamcenter.odiacalendar.CenteredImageSpan;
import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;


/**
 * Created by sasikanta on 11/14/2017.
 * EventPagerAdapter
 */

class EventPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle;
    private MainActivity activity;

    private EventPagerAdapter(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {
        super(fm);
        this.activity = (MainActivity) activity;
        pageTitle = subPageTitle;
    }

    public static EventPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {

        return new EventPagerAdapter(fm, activity, subPageTitle);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = EventPagerFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(EventPagerFragment.ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_nav_event);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("  " + pageTitle[position]);
        ImageSpan imageSpan = new CenteredImageSpan(activity.getApplicationContext(), R.drawable.ic_nav_event);
        sb.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;

    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}
