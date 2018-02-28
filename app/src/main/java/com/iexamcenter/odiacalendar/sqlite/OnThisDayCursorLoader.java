package com.iexamcenter.odiacalendar.sqlite;

import android.support.v4.content.CursorLoader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

public class OnThisDayCursorLoader extends CursorLoader {
    private static final String TAG = "WHATTODAY";
    private static Uri url = SqliteHelper.CONTENT_URI_WHATTODAY;
    private static final Uri contenturi = Uri.withAppendedPath(url, SqliteHelper.WhatTodayEntry.TABLE_WHATTODAY);
    private Context mContext;
String type;

    public OnThisDayCursorLoader(Context context, Bundle arg) {
        super(context);
        this.mContext = context;
        type=arg.getString("type","");

    }

    @Override
    public Cursor loadInBackground() {
        Cursor c = null;
        String projection[] = new String[]{"rowid", "*"};
        String selection=SqliteHelper.WhatTodayEntry.KEY_WHATTODAY_TYPE+" LIKE ?";
        String[] selectionArgs={type+"%"};
        String sort;
        c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, null);
        c.setNotificationUri(getContext().getContentResolver(), contenturi);

        return c;
    }

}
