package com.iexamcenter.odiacalendar.horoscope;


import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.iexamcenter.odiacalendar.CalendarWeatherApp;
import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.saxrssreader.RssFeed;
import com.iexamcenter.odiacalendar.saxrssreader.RssItem;
import com.iexamcenter.odiacalendar.saxrssreader.RssReader;
import com.iexamcenter.odiacalendar.sqlite.HoroscopeCursorLoader;
import com.iexamcenter.odiacalendar.sqlite.SqliteHelper;
import com.iexamcenter.odiacalendar.utility.Connectivity;
import com.iexamcenter.odiacalendar.utility.Constant;
import com.iexamcenter.odiacalendar.utility.PrefManager;
import com.iexamcenter.odiacalendar.utility.Utility;

import org.jsoup.Jsoup;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HoroscopeFrag extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ArrayList<horoscopeItem> horoscopeAl = new ArrayList<>();
    TextView msg;

    PrefManager mPref;
    String lang;
    Resources res;

    private final int URL_LOADER = 180;
    HoroscopeListAdapter mMomentListAdapter;
    RecyclerView momentListView;

    private MainActivity mContext;

    public static HoroscopeFrag newInstance() {

        return new HoroscopeFrag();
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
            Bundle arg = getArguments();

            Integer[] colors = Utility.getInstance(mContext).getPageColors();
            rootView.setBackgroundColor(colors[arg.getInt("position")]);


            Resources res = mContext.getResources();
            String[] rasi_kundali_arr = res.getStringArray(R.array.rasi_kundali_arr);

            String[] en_rasi_kundali_arr = res.getStringArray(R.array.en_rasi_kundali_arr);

            String[] lrasi_kundali_arr = res.getStringArray(R.array.l_rasi_kundali_arr);

            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            //  txt1 = (TextView) rootView.findViewById(R.id.txt1);
         //   credit = (TextView) rootView.findViewById(R.id.credit);
            msg = (TextView) rootView.findViewById(R.id.pick_my_city);
            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // mContext.refreshWhatToday();
                }
            });
            res = mContext.getResources();
            horoscopeAl = new ArrayList<>();
            momentListView = (RecyclerView) rootView.findViewById(R.id.rashiphalaList);

            mMomentListAdapter = new HoroscopeListAdapter(mContext, horoscopeAl, rasi_kundali_arr, en_rasi_kundali_arr, lrasi_kundali_arr);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            momentListView.setLayoutManager(llm);
            momentListView.setHasFixedSize(false);
            momentListView.setAdapter(mMomentListAdapter);
            if (!Connectivity.isConnected(mContext))
                Utility.getInstance(mContext).newToast("Please check internet connection.");
            new DownloadFilesTask().execute();

            msg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Connectivity.isConnected(mContext))
                        new DownloadFilesTask().execute();
                    else
                        Utility.getInstance(mContext).newToast("Please check internet connection.");

                }
            });

        } catch (Exception e) {
            String errorFeedBack = "HOROSCOPEFRAG-" + e.getMessage() ;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mContext == null)
            return null;

        View rootView = inflater.inflate(R.layout.frag_horoscope, null);

        return rootView;

    }


    private class DownloadFilesTask extends AsyncTask<Void, Void, RssFeed> {
        protected RssFeed doInBackground(Void... urls) {
            URL url = null;
            RssFeed feed = new RssFeed();
            try {

                url = new URL(Constant.HOROSCOPE_RSS);
                feed = RssReader.read(url);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return feed;
        }


        protected void onPostExecute(RssFeed feed) {
            try {

                //   Toast.makeText(mContext,"TOAST1",Toast.LENGTH_SHORT).show();
                if (feed != null) {
                    ArrayList<RssItem> rssItems = feed.getRssItems();
                    Uri pguri = SqliteHelper.CONTENT_URI_HOROSCOPE;
                    if (rssItems.size() > 0) {
                        mContext.getContentResolver().delete(pguri, null, null);

                        String horoscopeDt = "";
                        ArrayList<ContentValues> cva = new ArrayList<>();

                        for (RssItem rssItem : rssItems) {
                            String desc = rssItem.getDescription();
                            String title = rssItem.getTitle();
                            desc = Jsoup.parse(desc).text();
                            desc = desc.replace("AstroSage.com,", "");
                            desc = desc.replace("astrosage.com,", "");
                            desc = desc.replace("www.AstroSage.com,", "");
                            desc = desc.replace("www.astrosage.com,", "");
                            //  Toast.makeText(mContext,title+"TOAS::"+teaser,Toast.LENGTH_LONG).show();

                            System.out.println("RSSFEED::" + desc + ":::" + rssItem.getTitle());

                            ContentValues cv = new ContentValues();
                            cv.put(SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_TITLE, title);
                            cv.put(SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_DESC, desc);
                            cv.put(SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_DATE, rssItem.getPubDate().toString());

                            cva.add(cv);

//                            Uri ret = mContext.getContentResolver().insert(pguri, cv);
                            //                          System.out.println("RSSFEED::" + ret);

                            //                      Toast.makeText(mContext,"Tuuu:"+title+"::::"+teaser,Toast.LENGTH_LONG).show();


                        }
                        if (cva.size() > 0) {
                            ContentValues[] bulkToInsert = new ContentValues[cva.size()];

                            bulkToInsert = cva.toArray(bulkToInsert);
                            mContext.getContentResolver().bulkInsert(pguri, bulkToInsert);
                        }


                    }
                }
                getLoaderManager().restartLoader(URL_LOADER, null, HoroscopeFrag.this);
            } catch (Exception e) {
                Toast.makeText(mContext, "msg:" + e.getMessage(), Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        HoroscopeCursorLoader data = null;
        switch (id) {
            case URL_LOADER:
                data = new HoroscopeCursorLoader(mContext, bundle);
                break;
        }
        return data;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst()) {
            horoscopeAl.clear();
            String horoscopeDt = "";
            //   mMomentListAdapter.notifyDataSetChanged();
            do {
               // credit.setVisibility(View.GONE);
                msg.setVisibility(View.GONE);
                String title = cursor.getString(cursor.getColumnIndex(SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_TITLE));
                String desc = cursor.getString(cursor.getColumnIndex(SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_DESC));
                String date = cursor.getString(cursor.getColumnIndex(SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_DATE));
                System.out.println(":::::" + title + "::" + desc);
                horoscopeItem horoscopeItem = new horoscopeItem();
                horoscopeItem.date = date;
                horoscopeItem.title = title;
                horoscopeItem.desc = desc;
                //  if(horoscopeDt.isEmpty()) {
                //  horoscopeDt = date;
                //   mPref.setHoroscope(horoscopeDt);
                // String creditTxt="Source: AstroSage.com";
                //  credit.setText(creditTxt+"\n"+horoscopeDt);
                //  }

                horoscopeAl.add(horoscopeItem);

            } while (cursor.moveToNext());
          //  cursor.close();


        } else {
            msg.setVisibility(View.VISIBLE);
          //  credit.setVisibility(View.GONE);
        }
        mMomentListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public static class horoscopeItem {
        public String title, desc, date;
    }
}

