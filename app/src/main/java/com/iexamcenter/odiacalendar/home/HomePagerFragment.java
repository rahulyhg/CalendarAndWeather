package com.iexamcenter.odiacalendar.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;

/**
 * Created by sasikanta on 11/14/2017.
 * QuotePagerFragment
 */

public class HomePagerFragment extends Fragment {
    public static String ARG_POSITION;

    ViewGroup rootView;
    RecyclerView recyclerView;
    int num;
    int pageType = 1;
    MainActivity activity;

    public static HomePagerFragment newInstance() {

        return new HomePagerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_home_sub_page, container, false);
        setRetainInstance(false);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        num = bundle != null ? bundle.getInt(ARG_POSITION, 0) : 0;

        return rootView;
    }



    protected void setUp(View view) {


    }
}
