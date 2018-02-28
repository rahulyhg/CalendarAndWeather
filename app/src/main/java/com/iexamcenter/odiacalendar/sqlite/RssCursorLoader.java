package com.iexamcenter.odiacalendar.sqlite;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.iexamcenter.odiacalendar.utility.PrefManager;

public class RssCursorLoader extends CursorLoader {
    private static final String TAG = "RSS";
    private static Uri url = SqliteHelper.CONTENT_URI_RSS;
    private static final Uri contenturi = Uri.withAppendedPath(url, SqliteHelper.RssEntry.TABLE_RSS);
    private Context mContext;
    PrefManager mPref;
    String mLang = "";

    public RssCursorLoader(Context context, Bundle arg) {
        super(context);

        mPref = PrefManager.getInstance(context);
        this.mContext = context;
        mPref.load();
        mLang = mPref.getMyLanguage();

    }

    @Override
    public Cursor loadInBackground() {

        Cursor c = null;
        String sort = SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " ASC ";
        String projection[] = new String[]{"rowid", "*"};
        if (mLang.contains("en")) {
            String selection = SqliteHelper.RssEntry.KEY_RSS_LANG + " in(?)";
            String[] selectionArgs = {mLang};
            c = getContext().getContentResolver().query(contenturi, null, selection, selectionArgs, sort);
            c.setNotificationUri(getContext().getContentResolver(), contenturi);

        } else {
            String selection = SqliteHelper.RssEntry.KEY_RSS_LANG + " in(?,?,?)";
            String[] selectionArgs = {mLang, "hi", "en"};
            c = getContext().getContentResolver().query(contenturi, null, selection, selectionArgs, sort);
            c.setNotificationUri(getContext().getContentResolver(), contenturi);

        }


        return c;
    }

}
