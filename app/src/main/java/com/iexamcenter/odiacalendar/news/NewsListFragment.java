package com.iexamcenter.odiacalendar.news;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.database.Cursor;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.MyRxJava;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.sqlite.NewsCursorLoader;
import com.iexamcenter.odiacalendar.sqlite.SqliteHelper;
import com.iexamcenter.odiacalendar.utility.Connectivity;
import com.iexamcenter.odiacalendar.utility.Constant;
import com.iexamcenter.odiacalendar.utility.PrefManager;
import com.iexamcenter.odiacalendar.utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, PopupMenu.OnMenuItemClickListener {
    public static final String ARG_POSITION = "ARG_POSITION";
    MainActivity mContext;
    String mKey = "Top News";
    int mType = 1;
    FloatingActionButton mypopup;
    private static final int LOADER_NEWS = 2000;
    private MyListCursorAdapter mAdapter;
    private RecyclerView listview;
    PrefManager mPref;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    private BroadcastReceiver mRssDataUpdateReceiver;
    IntentFilter mRssDataUpdateFilter;

    private BroadcastReceiver mNewsDownloadReceiver;
    IntentFilter mNewsDownloadFilter;
    List<String> channelList, hindiChannelList, englishChannelList, categoriesList, titleList, autoCompList;
    TextView searchBy, no_result;

    ArrayAdapter<String> catDataAdapter;
    private AutoCompleteTextView actv;
    // Uri contenturi_news = Uri.withAppendedPath(SqliteHelper.CONTENT_URI_NEWS, SqliteHelper.NewsEntry.TABLE_NEWS);
    private boolean isForeground = false;
    private BroadcastReceiver mThemeReceiver;
    IntentFilter mThemeFilter;
    FrameLayout themeHeader;


    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;
        setSearchByText();
        actv.clearListSelection();
        actv.setText("");
        mKey = "Top News";
        mType = 0;

        actv.setAdapter(null);
        autoCompList.clear();
        autoCompList.addAll(channelList);
        catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
        catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
        actv.setAdapter(catDataAdapter);
        catDataAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();
        isForeground = false;

    }

    public void getNews(String key, int type) {


        mKey = key;
        mType = type;
        if (mType > 1 && actv.getText().toString().isEmpty()) {
            mKey = "";
        }
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE", type);

        bundle.putString("KEY", mKey);

        getLoaderManager().restartLoader(LOADER_NEWS, bundle, NewsListFragment.this);
        if (Connectivity.isConnected(mContext) && type != 3) {
            no_result.setText(mContext.getResources().getString(R.string.no_result));
            String UPDATE_KEY = mPref.getMyLanguage() + "--" + mKey + "--" + mType;
            long lastUpdateTime = mPref.getNewsUpdateTime(UPDATE_KEY);
            long currTime = System.currentTimeMillis();
            if (lastUpdateTime != -1L && ((currTime - lastUpdateTime) > Constant.MIN_NEWS_PULL_TIME)) {
                Log.e("DOWNLOAD", "DOWNLOAD:" + mKey + ":" + mType);
                progressBar.setVisibility(View.VISIBLE);
                new DownloadTask(mContext, mKey, mType);
            } else if (lastUpdateTime == -1L) {

                progressBar.setVisibility(View.VISIBLE);
                new DownloadTask(mContext, mKey, mType);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            no_result.setText(mContext.getResources().getString(R.string.refresh));
            Utility.getInstance(mContext).newToast("Please check internet.");
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void setAutoCompData(MenuItem item) {
        searchBy.setText(item.getTitle() + ":");
        actv.clearListSelection();
        actv.setText("");
        mKey = "";
        actv.setAdapter(null);
        autoCompList.clear();
        autoCompList.addAll(channelList);
        catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
        catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
        actv.setAdapter(catDataAdapter);
        catDataAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.news_publish:
               /* FragmentTransaction ft1 = getFragmentManager().beginTransaction();
                Fragment prev1 = getFragmentManager().findFragmentByTag("NEWSPUBLISH");
                if (prev1 != null) {
                    ft1.remove(prev1);
                }
                final DialogFragment filterDialog1 = NewsPublishDialog.newInstance();
                Bundle args1 = new Bundle();
                args1.putString("TEMP", "txt");

                filterDialog1.setArguments(args1);
                filterDialog1.setCancelable(true);
                filterDialog1.show(ft1, "NEWSPUBLISH");
*/
                break;

            case R.id.news_cricket:
                mType = 1;
                // setAutoCompData(item);
                getNews("Cricket", mType);
                break;

            case R.id.news_movie:
                mType = 1;
                // setAutoCompData(item);
                getNews("Entertainment", mType);
                break;

            case R.id.news_music:
                mType = 1;
                // setAutoCompData(item);
                getNews("Music", mType);
                break;

            case R.id.search_by_bengali_news:
                mType = 102;
                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_gujarati_news:
                mType = 103;
                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_kannada_news:
                mType = 104;
                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_malayalam_news:
                mType = 105;
                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_marathi_news:
                mType = 106;
                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_odia_news:
                mType = 107;

                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_punjabi_news:
                mType = 108;
                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_tamil_news:
                mType = 109;
                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_telugu_news:
                mType = 110;
                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_hindi_news:
                mType = 101;
                setAutoCompData(item);
                getNews("", mType);
                break;
            case R.id.search_by_english_news:
                mType = 100;

                setAutoCompData(item);
                getNews("", mType);
                break;


            case R.id.search_by_category:
                searchBy.setText("Category:");
                mType = 1;
                actv.clearListSelection();
                actv.setText("");
                actv.setAdapter(null);
                autoCompList.clear();
                autoCompList.addAll(categoriesList);
                catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
                catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
                actv.setAdapter(catDataAdapter);
                catDataAdapter.notifyDataSetChanged();

                break;
            case R.id.search_by_bengali_channel:
                mType = 102;
                setAutoCompData(item);
                break;
            case R.id.search_by_gujarati_channel:
                mType = 103;
                setAutoCompData(item);
                break;
            case R.id.search_by_kannada_channel:
                mType = 104;
                setAutoCompData(item);
                break;
            case R.id.search_by_malayalam_channel:
                mType = 105;
                setAutoCompData(item);
                break;
            case R.id.search_by_marathi_channel:
                mType = 106;
                setAutoCompData(item);
                break;
            case R.id.search_by_odia_channel:
                mType = 107;
                setAutoCompData(item);
                break;
            case R.id.search_by_punjabi_channel:
                mType = 108;
                setAutoCompData(item);
                break;
            case R.id.search_by_tamil_channel:
                mType = 109;
                setAutoCompData(item);
                break;
            case R.id.search_by_telugu_channel:
                mType = 110;
                setAutoCompData(item);
                break;
            case R.id.search_by_hindi_channel:
                searchBy.setText(item.getTitle() + ":");
                mType = 101;
                mKey = "";
                actv.clearListSelection();
                actv.setText("");
                actv.setAdapter(null);
                autoCompList.clear();
                autoCompList.addAll(hindiChannelList);
                catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
                catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
                actv.setAdapter(catDataAdapter);

                catDataAdapter.notifyDataSetChanged();


                break;
            case R.id.search_by_english_channel:
                searchBy.setText(item.getTitle() + ":");
                mType = 100;
                mKey = "";
                actv.clearListSelection();
                actv.setText("");
                actv.setAdapter(null);
                autoCompList.clear();
                autoCompList.addAll(englishChannelList);
                catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
                catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
                actv.setAdapter(catDataAdapter);

                catDataAdapter.notifyDataSetChanged();


                break;
            case R.id.search_by_title:
                searchBy.setText("Title:");
                mType = 3;
                mKey = "";
                actv.clearListSelection();
                actv.setText("");
                actv.setAdapter(null);
                autoCompList.clear();
                autoCompList.addAll(titleList);
                catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
                catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
                actv.setAdapter(catDataAdapter);

                catDataAdapter.notifyDataSetChanged();

                break;

            default:
                break;
        }

        return true;
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
            setHasOptionsMenu(true);


            mPref = PrefManager.getInstance(mContext);
            mPref.load();

            searchBy = rootView.findViewById(R.id.search_by);
            no_result = rootView.findViewById(R.id.no_result);
            mypopup = rootView.findViewById(R.id.mypopup);
            progressBar = rootView.findViewById(R.id.progressBar);
            setSearchByText();
            actv = rootView.findViewById(R.id.search);
            if (mPref.isTheme())
                actv.setDropDownBackgroundResource(R.drawable.rounded_bg_page3_auto);
            else
                actv.setDropDownBackgroundResource(R.drawable.rounded_bg_page3_auto_notheme);


            int colr = Utility.getInstance(mContext).getPageColors()[Constant.PAGE_UPDATES];
            //  c = (FrameLayout) rootView.findViewById(R.id.themeHeader);
            // themeHeader.setBackgroundColor(colr);
            mypopup.setBackgroundTintList(ColorStateList.valueOf(colr));
            mThemeFilter = new IntentFilter("CHANGED_THEME");

            mThemeReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int colr = Utility.getInstance(mContext).getPageColors()[Constant.PAGE_UPDATES];
                    themeHeader.setBackgroundColor(colr);

                    mypopup.setBackgroundTintList(ColorStateList.valueOf(colr));
                    if (mPref.isTheme())
                        actv.setDropDownBackgroundResource(R.drawable.rounded_bg_page3_auto);
                    else
                        actv.setDropDownBackgroundResource(R.drawable.rounded_bg_page3_auto_notheme);


                    Bundle bundle = new Bundle();
                    bundle.putString("KEY", mKey);
                    bundle.putInt("TYPE", mType);
                    getLoaderManager().restartLoader(LOADER_NEWS, bundle, (android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>) NewsListFragment.this);

                }
            };


            actv.setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                    // TODO Auto-generated method stub
                    actv.showDropDown();
                    actv.requestFocus();

                    return false;
                }
            });

            autoCompList = new ArrayList<String>();
            titleList = new ArrayList<String>();
            channelList = new ArrayList<String>();
            hindiChannelList = new ArrayList<String>();
            englishChannelList = new ArrayList<String>();
            categoriesList = new ArrayList<String>();


            catDataAdapter = new ArrayAdapter<String>(mContext, R.layout.my_simple_list_item, autoCompList);
            catDataAdapter.setDropDownViewResource(R.layout.my_simple_list_item);
            actv.setAdapter(catDataAdapter);
            actv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || event.getAction() == KeyEvent.ACTION_DOWN
                            && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        mKey = "" + v.getText();
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        if (imm.isActive()) {
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
                        }
                        setTypeVal();
                        getNews(mKey, mType);

                        return true;
                    }
                    return false;
                }
            });
            actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mKey = (String) parent.getItemAtPosition(position);
                    setTypeVal();
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
                    }
                    getNews(mKey, mType);


                }
            });
            swipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);

            swipeRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            getNews(mKey, mType);
                        }
                    }
            );
            mypopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v, 2);
                }
            });

            searchBy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopup(v, 1);
                }
            });

            listview = rootView.findViewById(R.id.list);
            mAdapter = new MyListCursorAdapter(mContext, null);

            listview.setLayoutManager(new LinearLayoutManager(mContext));
            listview.setHasFixedSize(true);


            listview.setAdapter(mAdapter);

            no_result.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNews(mKey, mType);
                }
            });

            getNews(mKey, mType);
            mNewsDownloadFilter = new IntentFilter("NEWS_DOWNLOAD_RECEIVER");

            mNewsDownloadReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        String key = intent.getStringExtra("KEY");
                        // Toast.makeText(mContext, mKey+":MSG:" + key, Toast.LENGTH_SHORT).show();
                        if (mKey.equalsIgnoreCase(key)) {
                            new Handler().postDelayed(new Runnable() {

                                @Override
                                public void run() {

                                    try {
                                        Bundle bundle = new Bundle();
                                        bundle.putString("KEY", mKey);
                                        bundle.putInt("TYPE", mType);
                                        getLoaderManager().restartLoader(LOADER_NEWS, bundle, (android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>) NewsListFragment.this);
                                        swipeRefreshLayout.setRefreshing(false);
                                        progressBar.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, 1000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };


            mRssDataUpdateFilter = new IntentFilter("RSS_DATA_UPDATE");
            mRssDataUpdateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {


                        String key = intent.getStringExtra("KEY");
                        Utility.getInstance(mContext).newToast(key);
                        doExecuteBg();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };


            mContext.registerReceiver(mThemeReceiver, mThemeFilter);
            mContext.registerReceiver(mRssDataUpdateReceiver, mRssDataUpdateFilter);
            mContext.registerReceiver(mNewsDownloadReceiver, mNewsDownloadFilter);

            doExecuteBg();
        } catch (Exception e) {
            String errorFeedBack = "LISTFRAG-" + e.getMessage();
            // CalendarWeatherApp.getInstance().loadData(errorFeedBack, Constant.FEEDBACK_API);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        Bundle b = getArguments();
        int position = b.getInt(ARG_POSITION);
        if (position == 0) {
            mKey = "Top News";
            mType = 0;
        } else if (position == 1) {
            mKey = "Entertainment";
            mType = 1;
        } else if (position == 2) {
            mKey = "Cricket";
            mType = 1;
        }else if (position == 3) {
            mKey = "Music";
            mType = 1;
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            mContext.unregisterReceiver(mNewsDownloadReceiver);
            mContext.unregisterReceiver(mRssDataUpdateReceiver);
            mContext.unregisterReceiver(mThemeReceiver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        switch (id) {
            case LOADER_NEWS:


                NewsCursorLoader c = new NewsCursorLoader(mContext, args);

                return c;


        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {


        if (cursor.getCount() == 0) {
            no_result.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            no_result.setVisibility(View.GONE);
        }

        mAdapter.changeCursor(cursor);


    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
        loader.reset();


        // }

    }

    public void doRssInBg() {
        hindiChannelList.clear();
        englishChannelList.clear();
        channelList.clear();
        String lang = mPref.getMyLanguage();

        Uri url = SqliteHelper.CONTENT_URI_RSS;
        final Uri contenturi = Uri.withAppendedPath(url, SqliteHelper.RssEntry.TABLE_RSS);

        String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " IN (?,?,?)";
        String[] selectionArgs = {lang, "hi", "en"};

        String projection[] = new String[]{SqliteHelper.RssEntry.KEY_RSS_CHANNEL, SqliteHelper.RssEntry.KEY_RSS_LANG};
        String sort = SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " ASC";
        Cursor cursor = mContext.getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        channelList.clear();
        // channelList.add("-:News Channel:-");
        if (cursor.moveToFirst()) {
            do {
                String language = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_LANG));

                String channel = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_CHANNEL));

                if (language.contains("hi"))
                    hindiChannelList.add(channel);
                else if (language.contains("en"))
                    englishChannelList.add(channel);
                else
                    channelList.add(channel);
            } while (cursor.moveToNext());
            // cursor.close();
        }

        cursor.close();
        String projection1[] = new String[]{"distinct " + SqliteHelper.RssEntry.KEY_RSS_CAT};
        sort = SqliteHelper.RssEntry.KEY_RSS_CAT + " ASC";
        cursor = mContext.getContentResolver().query(contenturi, projection1, selection, selectionArgs, sort);
        categoriesList.clear();
        // categoriesList.add("-:Category:-");
        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_CAT));
                categoriesList.add(category);
            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    public void postExecute() {
        refreshChannelVal();
    }

    public void doExecuteBg() {

        MyRxJava.newInstance().rssFetchInBackground(NewsListFragment.this);
    }

   /* private class DownloadFilesTask extends AsyncTask<Void, Integer, Long> {

        protected Long doInBackground(Void... urls) {
            hindiChannelList.clear();
            englishChannelList.clear();
            channelList.clear();
            String lang = mPref.getMyLanguage();
            Cursor cursor = null;
            Uri url = SqliteHelper.CONTENT_URI_RSS;
            final Uri contenturi = Uri.withAppendedPath(url, SqliteHelper.RssEntry.TABLE_RSS);

            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " IN (?,?,?)";
            String[] selectionArgs = {lang, "hi", "en"};

            String projection[] = new String[]{SqliteHelper.RssEntry.KEY_RSS_CHANNEL, SqliteHelper.RssEntry.KEY_RSS_LANG};
            String sort = SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " ASC";
            cursor = mContext.getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
            channelList.clear();
            // channelList.add("-:News Channel:-");
            if (cursor.moveToFirst()) {
                do {
                    String language = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_LANG));

                    String channel = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_CHANNEL));

                    if (language.contains("hi"))
                        hindiChannelList.add(channel);
                    else if (language.contains("en"))
                        englishChannelList.add(channel);
                    else
                        channelList.add(channel);
                } while (cursor.moveToNext());
               // cursor.close();
            }

            cursor.close();
            String projection1[] = new String[]{"distinct " + SqliteHelper.RssEntry.KEY_RSS_CAT};
            sort = SqliteHelper.RssEntry.KEY_RSS_CAT + " ASC";
            cursor = mContext.getContentResolver().query(contenturi, projection1, selection, selectionArgs, sort);
            categoriesList.clear();
            // categoriesList.add("-:Category:-");
            if (cursor.moveToFirst()) {
                do {
                    String category = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_CAT));
                    categoriesList.add(category);
                } while (cursor.moveToNext());

            }
            cursor.close();
            return 1l;
        }


        protected void onPostExecute(Long result) {
            refreshChannelVal();


        }

    }*/

    public void showPopup(View v, int type) {
        PopupMenu popup = new PopupMenu(mContext, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        Menu m = popup.getMenu();

        if (type == 1) {
            inflater.inflate(R.menu.news, m);
            if (mPref.getMyLanguage().contains("bn")) {
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("gu")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("hi")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("kn")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("ml")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("mr")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("or")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("pa")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("ta")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            } else if (mPref.getMyLanguage().contains("te")) {
                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
            } else if (mPref.getMyLanguage().contains("en")) {

                m.removeItem(R.id.search_by_bengali_channel);
                m.removeItem(R.id.search_by_gujarati_channel);
                m.removeItem(R.id.search_by_hindi_channel);
                m.removeItem(R.id.search_by_kannada_channel);
                m.removeItem(R.id.search_by_malayalam_channel);
                m.removeItem(R.id.search_by_marathi_channel);
                m.removeItem(R.id.search_by_odia_channel);
                m.removeItem(R.id.search_by_punjabi_channel);
                m.removeItem(R.id.search_by_tamil_channel);
                m.removeItem(R.id.search_by_telugu_channel);
            }

        } else {
            inflater.inflate(R.menu.news_filter, m);
            if (mPref.getMyLanguage().contains("bn")) {
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("gu")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("hi")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("kn")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("ml")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("mr")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("or")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("pa")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("ta")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_telugu_news);
            } else if (mPref.getMyLanguage().contains("te")) {
                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
            } else if (mPref.getMyLanguage().contains("en")) {

                m.removeItem(R.id.search_by_bengali_news);
                m.removeItem(R.id.search_by_gujarati_news);
                m.removeItem(R.id.search_by_hindi_news);
                m.removeItem(R.id.search_by_kannada_news);
                m.removeItem(R.id.search_by_malayalam_news);
                m.removeItem(R.id.search_by_marathi_news);
                m.removeItem(R.id.search_by_odia_news);
                m.removeItem(R.id.search_by_punjabi_news);
                m.removeItem(R.id.search_by_tamil_news);
                m.removeItem(R.id.search_by_telugu_news);
            }

        }

        popup.show();
    }

    public void setSearchByText() {
        if (mPref.getMyLanguage().contains("bn"))
            searchBy.setText("Bengali Channel:");
        else if (mPref.getMyLanguage().contains("gu"))
            searchBy.setText("Gujarati Channel:");
        else if (mPref.getMyLanguage().contains("hi"))
            searchBy.setText("Hindi Channel:");
        else if (mPref.getMyLanguage().contains("kn"))
            searchBy.setText("Kannada Channel:");
        else if (mPref.getMyLanguage().contains("ml"))
            searchBy.setText("Malayalam Channel:");
        else if (mPref.getMyLanguage().contains("mr"))
            searchBy.setText("Marathi Channel:");
        else if (mPref.getMyLanguage().contains("or"))
            searchBy.setText("Odia Channel:");
        else if (mPref.getMyLanguage().contains("pa"))
            searchBy.setText("Punjabi Channel:");
        else if (mPref.getMyLanguage().contains("ta"))
            searchBy.setText("Tamil Channel:");
        else if (mPref.getMyLanguage().contains("te"))
            searchBy.setText("Telugu Channel:");
        else if (mPref.getMyLanguage().contains("en"))
            searchBy.setText("English Channel:");

    }

    public void setTypeVal() {
        String searchByTxt = searchBy.getText().toString();
        if (searchByTxt.contains("Bengali Channel:"))
            mType = 102;
        else if (searchByTxt.contains("Gujarati Channel:"))
            mType = 103;
        else if (searchByTxt.contains("Hindi Channel:"))
            mType = 101;
        else if (searchByTxt.contains("Kannada Channel:"))
            mType = 104;
        else if (searchByTxt.contains("Malayalam Channel:"))
            mType = 105;
        else if (searchByTxt.contains("Marathi Channel:"))
            mType = 106;
        else if (searchByTxt.contains("Odia Channel:"))
            mType = 107;
        else if (searchByTxt.contains("Punjabi Channel:"))
            mType = 108;
        else if (searchByTxt.contains("Tamil Channel:"))
            mType = 109;
        else if (searchByTxt.contains("Telugu Channel:"))
            mType = 110;
        else if (searchByTxt.contains("English Channel:"))
            mType = 100;

    }

    public void refreshChannelVal() {
        String searchByTxt = searchBy.getText().toString();
        autoCompList.clear();
        if (searchByTxt.contains("Bengali Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Gujarati Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Hindi Channel:")) {

            autoCompList.addAll(hindiChannelList);
        } else if (searchByTxt.contains("Kannada Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Malayalam Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Marathi Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Odia Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Punjabi Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Tamil Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("Telugu Channel:")) {

            autoCompList.addAll(channelList);
        } else if (searchByTxt.contains("English Channel:")) {

            autoCompList.addAll(englishChannelList);
        } else {
            autoCompList.addAll(channelList);
        }
        catDataAdapter.notifyDataSetChanged();

    }


    public static Fragment getInstance() {
        return new NewsListFragment();
    }
}
