package com.iexamcenter.odiacalendar.quote;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;

/**
 * Created by Sasikanta Sahoo on 11/18/2017.
 * QuoteMainFragment
 */

public class QuoteMainFragment extends Fragment {
    MainActivity activity;
    Resources res;
    TabLayout tabLayout;
    static String[] subPagesTitle;
    public static QuoteMainFragment newInstance(String[] subPages) {
        subPagesTitle=subPages;
        return new QuoteMainFragment();
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
                R.layout.fragment_main, container, false);

        res = activity != null ? activity.getResources() : null;
        setRetainInstance(true);
        setUp(rootView);
        return rootView;
    }


public TabLayout getTabLayout(){
        return tabLayout;
}
    protected void setUp(View rootView) {
        ViewPager tabViewPager = rootView.findViewById(R.id.tabViewPager);
        tabLayout = rootView.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(tabViewPager);
        tabViewPager.setAdapter(QuotePagerAdapter.newInstance(getChildFragmentManager(), activity,subPagesTitle));
    }

}