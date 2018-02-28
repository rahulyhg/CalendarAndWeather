package com.iexamcenter.odiacalendar.news;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.iexamcenter.odiacalendar.sqlite.SqliteHelper;
import com.iexamcenter.odiacalendar.utility.Constant;
import com.iexamcenter.odiacalendar.utility.PrefManager;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.Executor;

/**
 * Created by sasikanta on 4/26/2016.
 */

public class DownloadTask extends AsyncTask<String, String, String> {

    private Context mContext;
    //  private String[] mRssArr;
    private PrefManager mPref;
    private BroadcastNotifier mBroadcaster;
    private String mKey;
    int mType;
    boolean isBroadCasted = false;

    /*
        public DownloadTask(Context context) {
            mContext = context;
            mBroadcaster = new BroadcastNotifier(mContext);
            mPref = PrefManager.getInstance(context);
            mPref.load();
            if (Build.VERSION.SDK_INT >= 11)
                this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                this.execute();
        }
    */


    public DownloadTask(Context context, String cat, int type) {
        mContext = context;
        mPref = PrefManager.getInstance(context);
        mPref.load();
        mKey = cat;
        mType = type;
        Log.e("DOWNLOAD","DOWNLOAD:"+mKey+":"+mType);
        if (android.os.Build.VERSION.SDK_INT >= 14) {

            try {
                try {
                    AsyncTask.class.getMethod("setDefaultExecutor", Executor.class).invoke(null, AsyncTask.SERIAL_EXECUTOR);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= 11)
            this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            this.execute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        sendBroadcast();
    }

    public void sendBroadcast() {
        isBroadCasted = true;
        Intent intent = new Intent("NEWS_DOWNLOAD_RECEIVER");
        if (mKey != null)
            intent.putExtra("KEY", mKey);
        else
            intent.putExtra("KEY", "Top News");
        mContext.sendBroadcast(intent);
    }

    protected String doInBackground(String... urls) {
        isBroadCasted = false;
        Log.e("DOWNLOAD","DOWNLOAD1:"+mKey+":"+mType);

        Uri contenturi_rss = Uri.withAppendedPath(SqliteHelper.CONTENT_URI_RSS, SqliteHelper.RssEntry.TABLE_RSS);

        Cursor cursor1 = mContext.getContentResolver().query(contenturi_rss, null, null, null, null);


        if (cursor1.getCount() == 0) {
            cursor1.close();
            return null;
        } else if (cursor1 != null) {
            cursor1.close();
        }
        Log.e("DOWNLOAD","DOWNLOAD2:"+mKey+":"+mType);
        Uri contenturi_news = Uri.withAppendedPath(SqliteHelper.CONTENT_URI_NEWS, SqliteHelper.NewsEntry.TABLE_NEWS);
        String timeStamp = "" + ((System.currentTimeMillis() / 1000) - Constant.DAY_KEEP * 24 * 60 * 60);

        String selection1 = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " > ? ";
        String[] selectionArgs1 = {timeStamp};

        Cursor c = mContext.getContentResolver().query(contenturi_news, null, selection1, selectionArgs1, null);
        Log.e("DOWNLOAD","DOWNLOAD3:"+mKey+":"+mType);
        if (c != null && c.getCount() > 0) {
            // String timeStamp = "" + ((System.currentTimeMillis() / 1000) - 3 * 24 * 60 * 60);

            String selection = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " < ? ";
            String[] selectionArgs = {timeStamp};
            mContext.getContentResolver().delete(contenturi_news, selection, selectionArgs);
        }

        if (c != null)
            c.close();

        String[] projection = {"*"};


        Cursor cursor2 = null;
        if (mKey == null) {
            mKey = "Top News";
        }
        String lang = mPref.getMyLanguage();
        Log.e("DOWNLOAD","DOWNLOAD4:"+mKey+":"+lang);
        if (mType == 0 || mType == 1) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CAT + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " IN (?,?)";
            String[] selArg = {mKey + "%", lang, "en"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 100) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "en"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 101) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " = ?";
            String[] selArg = {mKey + "%", "hi"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 102) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "bn"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 103) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "gu"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 104) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "kn"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 105) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "ml"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 106) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "mr"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 107) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "or"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 108) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "pa"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 109) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "ta"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else if (mType == 110) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " =?";
            String[] selArg = {mKey + "%", "te"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        } else {
            String selection = SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " =1 AND " + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " LIKE ? AND " + SqliteHelper.RssEntry.KEY_RSS_LANG + " IN (?,?)";
            String[] selArg = {mKey + "%", lang, "en"};
            cursor2 = mContext.getContentResolver().query(contenturi_rss, projection, selection, selArg, null);

        }
        Log.e("DOWNLOAD","DOWNLOAD5:"+mKey+":"+lang);
        if (cursor2.moveToFirst()) {
            do {

                String newsLink = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_LINK));
                String newsChannel = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_CHANNEL));
                String newsCat = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_CAT));

                String newsLang = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_LANG));

                int newsType = cursor2.getInt(cursor2.getColumnIndexOrThrow(SqliteHelper.RssEntry.KEY_RSS_TYPE));

                newsLink = Constant.YOUTUBECHANNEL + newsLink;
                String[] rssStrArr = {newsChannel, newsCat, newsLink, "" + newsType, newsLang};

                HttpURLConnection localURLConnection = null;
                InputStream myStream = null;
                URL localURL = null;
                try {


                    localURL = new URL(newsLink);
                    localURLConnection = (HttpURLConnection) localURL.openConnection();
                    if ((localURLConnection instanceof HttpURLConnection)) {
                        myStream = localURLConnection.getInputStream();
                        // RSSPullParser localPicasaPullParser;
                        Vector<ContentValues> retData;
                        if (newsType == 2) {

                            MediaPullParser localPicasaPullParser = new MediaPullParser();
                            localPicasaPullParser.parseXml(rssStrArr,
                                    myStream,
                                    mBroadcaster);


                            retData = localPicasaPullParser.getItemData();
                        } else {

                            RSSPullParser localPicasaPullParser = new RSSPullParser();
                            localPicasaPullParser.parseXml(rssStrArr,
                                    myStream,
                                    mBroadcaster);

                            retData = localPicasaPullParser.getItemData();
                        }

                        if (myStream != null)
                            myStream.close();
                        //  Uri contenturi_news = Uri.withAppendedPath(SqliteHelper.CONTENT_URI_NEWS, SqliteHelper.NewsEntry.TABLE_NEWS);
                        ContentValues[] bulkToInsert = new ContentValues[retData.size()];
                        bulkToInsert = retData.toArray(bulkToInsert);

                        Log.e("DOWNLOAD","DOWNLOAD6:"+mKey+":"+lang);
                        if (bulkToInsert.length > 0) {
                            mContext.getContentResolver().bulkInsert(contenturi_news, bulkToInsert);
                            Log.e("DOWNLOAD","DOWNLOAD7:"+mKey+":"+lang);
                            if (!isBroadCasted) {
                                sendBroadcast();
                            }


                        }


                    }
                    if (localURLConnection != null) {
                        localURLConnection.disconnect();
                    }
                } catch (Exception e) {
                    if (localURLConnection != null) {
                        localURLConnection.disconnect();
                    }
                    e.printStackTrace();


                } finally {
                    if (localURLConnection != null) {
                        localURLConnection.disconnect();
                    }
                }
            } while (cursor2.moveToNext());

        }
        if (cursor2 != null)
            cursor2.close();
        // String UPDATE_KEY=mKey+"--"+mType;
        String UPDATE_KEY = mPref.getMyLanguage() + "--" + mKey + "--" + mType;

        mPref.setNewsUpdateTime(UPDATE_KEY, System.currentTimeMillis());
        Log.e("DOWNLOAD","DOWNLOAD8:"+mKey+":"+lang);
        return null;
    }

}