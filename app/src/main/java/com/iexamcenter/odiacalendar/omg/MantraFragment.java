package com.iexamcenter.odiacalendar.omg;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.news.YoutubeListAdapter;
import com.iexamcenter.odiacalendar.utility.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sasikanta on 11/14/2017.
 * QuotePagerFragment
 */

public class MantraFragment extends Fragment {
    public static String ARG_POSITION;

    ViewGroup rootView;
    RecyclerView recyclerView;
    int num;
    int pageType = 1;
    MainActivity activity;
    ArrayList<YoutubeList> hl;
    PrefManager mPref;

    public static MantraFragment newInstance() {

        return new MantraFragment();
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
                R.layout.fragment_omg_mantra, container, false);
        setRetainInstance(false);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        num = bundle != null ? bundle.getInt(ARG_POSITION, 0) : 0;
        mPref = PrefManager.getInstance(activity);
        mPref.load();
        setUp(rootView);
        return rootView;
    }


    protected void setUp(View view) {

        String[] l_festival_arr = getResources().getStringArray(R.array.youtube);
        hl = new ArrayList<>();
        for (int i = 0; i < l_festival_arr.length; i++) {
            JSONObject festReader = null;
            try {
                festReader = new JSONObject(l_festival_arr[i]);

                String key = festReader.getString("key");
                String titleL = festReader.getString("titleL");
                String titleE = festReader.getString("titleE");
                String lang = festReader.getString("lang");
                YoutubeList obj = new YoutubeList();
                obj.vKey = key;
                obj.lTitle = titleL;
                obj.eTitle = titleE;
                if (lang.contains("all") || lang.contains(mPref.getMyLanguage()))
                    hl.add(obj);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        RecyclerView listview = (RecyclerView) view.findViewById(R.id.video);
        YoutubeListAdapter mAdapter = new YoutubeListAdapter(activity, hl);
        listview.setLayoutManager(new GridLayoutManager(activity, 3));
        listview.setHasFixedSize(true);
        listview.setAdapter(mAdapter);

    }

    public static class YoutubeList {
        public String vKey, lTitle, eTitle;
    }
}
