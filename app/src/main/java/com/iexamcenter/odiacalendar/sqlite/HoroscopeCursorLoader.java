package com.iexamcenter.odiacalendar.sqlite;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;

public class HoroscopeCursorLoader extends CursorLoader {
    private static final String TAG = "HOROSCOPE";
    private static Uri url = SqliteHelper.CONTENT_URI_HOROSCOPE;
    private static final Uri contenturi = Uri.withAppendedPath(url, SqliteHelper.HoroscopeEntry.TABLE_HOROSCOPE);
    private Context mContext;


    public HoroscopeCursorLoader(Context context, Bundle arg) {
        super(context);


        this.mContext = context;

    }

    @Override
    public Cursor loadInBackground() {

        Cursor c = null;
        String projection[] = new String[]{"rowid", "*"};
        String selection;
        String[] selectionArgs;
        String sort;

        c = getContext().getContentResolver().query(contenturi, projection, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), contenturi);

        return c;
    }

}
