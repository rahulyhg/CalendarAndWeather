package com.iexamcenter.odiacalendar.quote;

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
 * QuotePagerAdapter
 */

class QuotePagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle;
    MainActivity activity;

    private QuotePagerAdapter(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {
        super(fm);
        pageTitle = subPageTitle;
        this.activity = (MainActivity) activity;
    }

    public static QuotePagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {

        return new QuotePagerAdapter(fm, activity, subPageTitle);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = QuotePagerFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt(QuotePagerFragment.ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_nav_quote);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("  " + pageTitle[position]);
        ImageSpan imageSpan = new CenteredImageSpan(activity.getApplicationContext(), R.drawable.ic_nav_quote);
        sb.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;

    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}
