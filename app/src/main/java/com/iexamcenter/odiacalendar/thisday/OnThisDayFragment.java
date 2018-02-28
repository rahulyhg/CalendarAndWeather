package com.iexamcenter.odiacalendar.thisday;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.icache.RequestManager;
import com.iexamcenter.odiacalendar.request.BaseGsonRequest;
import com.iexamcenter.odiacalendar.request.HttpRequestObject;
import com.iexamcenter.odiacalendar.response.OnThisDayResponse;
import com.iexamcenter.odiacalendar.sqlite.SqliteHelper;
import com.iexamcenter.odiacalendar.sqlite.OnThisDayCursorLoader;
import com.iexamcenter.odiacalendar.utility.Constant;
import com.iexamcenter.odiacalendar.utility.PrefManager;
import com.iexamcenter.odiacalendar.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by sasikanta on 9/27/2016.
 */
public class OnThisDayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    ArrayList<horoscopeItem> horoscopeAl = new ArrayList<>();

    PrefManager mPref;
    String lang;
    ArrayList<OnThisDayResponse.WhatToDayList> resList;
    private int URL_LOADER = 121;
    OnThisDayAdapter mMomentListAdapter;
    RecyclerView momentListView;
    TextView header, msg;
    static private MainActivity mContext;

    public static OnThisDayFragment newInstance() {

        return new OnThisDayFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            mContext = (MainActivity) getActivity();
            setHasOptionsMenu(false);
            Bundle arg = getArguments();
            URL_LOADER = arg.getInt("position");
            Integer[] colors = Utility.getInstance(mContext).getPageColors();
            rootView.setBackgroundColor(colors[URL_LOADER]);
            mPref = PrefManager.getInstance(mContext);
            mPref.load();
            lang = mPref.getMyLanguage();
            horoscopeAl = new ArrayList<>();
            momentListView = rootView.findViewById(R.id.rashiphalaList);

            msg = rootView.findViewById(R.id.pick_my_city);
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  mContext.refreshWhatToday();
                }
            });

            mMomentListAdapter = new OnThisDayAdapter(mContext, horoscopeAl, URL_LOADER);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            momentListView.setLayoutManager(llm);
            momentListView.setHasFixedSize(false);
            momentListView.setAdapter(mMomentListAdapter);
            String type = "*";
            String title = "";
          /*  if (URL_LOADER == 0) {
                type = "event";
                title = "Historical event on this day";
            } else if (URL_LOADER == 1) {
                type = "birthday";
                title = "Birthdays on this day";
            } else if (URL_LOADER == 2) {
                type = "death";
                title = "Deaths on this day";
            } else if (URL_LOADER == 3) {
                type = "wedding";
                title = "Popular wedding & divorce";
            }*/
          //  type = "wedding";
            type = "birthday";
            //type = "death";
            Bundle b = new Bundle();
            b.putString("type", type);
            getLoaderManager().restartLoader(URL_LOADER, b, OnThisDayFragment.this);
        } catch (Exception e) {
            String errorFeedBack = "BIRTHDAYFRAG-" + e.getMessage();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_whattodayitem, null);
        refreshWhatToday();
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        OnThisDayCursorLoader data = null;
        //switch (id) {
        //    case URL_LOADER:
        data = new OnThisDayCursorLoader(mContext, bundle);
        //        break;
        //  }
        return data;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            horoscopeAl.clear();
            msg.setVisibility(View.GONE);
            String horoscopeDt = "";
            do {

                String title = cursor.getString(cursor.getColumnIndex(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_TITLE));
                String year = cursor.getString(cursor.getColumnIndex(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_YEAR));
                String month = cursor.getString(cursor.getColumnIndex(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_MONTH));

                String day = cursor.getString(cursor.getColumnIndex(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_DAY));
                String type = cursor.getString(cursor.getColumnIndex(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_TYPE));
                String wiki = cursor.getString(cursor.getColumnIndex(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_WIKI));

                month = Character.toUpperCase(month.charAt(0)) + month.substring(1);
                //  System.out.println(":::::" + title + "::" + year);
                horoscopeItem horoscopeItem = new horoscopeItem();
                horoscopeItem.date = month + "-" + day + ", " + year;
                horoscopeItem.title = title;//.replaceFirst(year,"").trim();
                horoscopeItem.year = year;
                horoscopeItem.type = type;
                horoscopeItem.wiki = wiki;
                horoscopeAl.add(horoscopeItem);

            } while (cursor.moveToNext());
            cursor.close();


        } else {
            msg.setVisibility(View.VISIBLE);
            // msg.setVisibility(View.VISIBLE);
            //credit.setVisibility(View.GONE);
        }
        mMomentListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static class horoscopeItem {
        public String title, year, date, type, wiki;
    }


    public void refreshWhatToday() {

        mPref=PrefManager.getInstance(mContext);
        mPref.load();
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        // CalendarWeatherApp.log("CalendarWeatherApp::::"+dayOfMonth + "-" + month + "-" + year);
        if (!mPref.getWhattoday().contains(dayOfMonth + "-" + month + "-" + year)) {
            String json1 = "{'YEAR':'" + year + "','MONTH':'" + month + "','DAYOFMONTH':'" + dayOfMonth + "','LANG':'" + lang + "'}";
            loadData(json1, Constant.WHATTODAY_API);
        } else {
            // Utility.getInstance(mContext).newToast("Updated..");
        }

    }

    private void loadData(String profileJson, int api) {
        HttpRequestObject mReqobject = new HttpRequestObject(mContext);
        try {
            JSONObject jsonHeader = mReqobject.getRequestBody(api, profileJson);

            switch (api) {
                case Constant.WHATTODAY_API:
                    BaseGsonRequest<OnThisDayResponse> whatTodayGsonRequest = new BaseGsonRequest<>(Request.Method.POST, Constant.URL_API, OnThisDayResponse.class, jsonHeader, null,
                            new Response.Listener<OnThisDayResponse>() {

                                @Override
                                public void onResponse(OnThisDayResponse res) {


                                    resList = res.getList();
                                    whatToDayInBackground();


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError res) {
                            // mainLoader.setVisibility(View.GONE);
                        }
                    }
                    );
                    RequestManager.getRequestQueue().add(whatTodayGsonRequest).setTag("TAG" + api);
                    break;


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void whatToDayInBackground() {
        whatToDayInBackground(mContext);

    }

    public void whatToDayInBackground(final MainActivity mContext) {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                whatToDayInBg();
                subscriber.onNext(1);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        whatToDayInPost(integer);
                    }
                });
    }

    public void whatToDayInBg() {
        Uri pguri = SqliteHelper.CONTENT_URI_WHATTODAY;
        if (resList.size() > 0) {
            mContext.getContentResolver().delete(pguri, null, null);
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            mPref.setWhattoday(day + "-" + month + "-" + year);

        }


        ArrayList<ContentValues> cva = new ArrayList<>();
        for (int i = 0; i < resList.size(); i++) {
            OnThisDayResponse.WhatToDayList obj = resList.get(i);

            ContentValues cv = new ContentValues();
            cv.put(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_TITLE, obj.getTitle());
            cv.put(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_YEAR, obj.getYear());
            cv.put(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_MONTH, obj.getMonth());
            cv.put(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_DAY, obj.getDay());
            cv.put(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_TYPE, obj.getType());

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                cv.put(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_WIKI, obj.getWiki());
            } else {
                cv.put(SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_WIKI, " ");
            }


            cva.add(cv);

        }

        if (cva.size() > 0) {
            ContentValues[] bulkToInsert = new ContentValues[cva.size()];

            bulkToInsert = cva.toArray(bulkToInsert);
            mContext.getContentResolver().bulkInsert(pguri, bulkToInsert);
        }

    }

    public void whatToDayInPost(Integer token) {
       String type = "event";
        type = "birthday";
        type = "death";
        type = "wedding";
        Bundle b = new Bundle();
        b.putString("type", type);
        getLoaderManager().restartLoader(URL_LOADER, b, OnThisDayFragment.this);
    }

}
