package com.iexamcenter.odiacalendar.omg;

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
import com.iexamcenter.odiacalendar.news.NewsListFragment;
import com.iexamcenter.odiacalendar.quote.QuotePagerFragment;


/**
 * Created by sasikanta on 11/14/2017.
 * OmgPagerAdapter
 */

class OmgPagerAdapter extends FragmentPagerAdapter {
    private String[] pageTitle;
    MainActivity activity;

    private OmgPagerAdapter(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {
        super(fm);
        pageTitle = subPageTitle;
        this.activity = (MainActivity) activity;
    }

    public static OmgPagerAdapter newInstance(FragmentManager fm, FragmentActivity activity, String[] subPageTitle) {

        return new OmgPagerAdapter(fm, activity, subPageTitle);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args;
        switch (position) {
            case 0:
                fragment = NewsListFragment.getInstance();
                args = new Bundle();
                args.putInt(NewsListFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;

            case 2:
                fragment = QuotePagerFragment.newInstance();
                args = new Bundle();
                args.putInt(QuotePagerFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = MantraFragment.newInstance();
                args = new Bundle();
                args.putInt(MantraFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
            default:
                fragment = MantraFragment.newInstance();
                args = new Bundle();
                args.putInt(MantraFragment.ARG_POSITION, position);
                fragment.setArguments(args);
                break;
        }

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_nav_omg);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        // Replace blank spaces with image icon
        SpannableString sb = new SpannableString("  " + pageTitle[position]);
        ImageSpan imageSpan = new CenteredImageSpan(activity.getApplicationContext(), R.drawable.ic_nav_omg);
        sb.setSpan(imageSpan, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;

    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}
