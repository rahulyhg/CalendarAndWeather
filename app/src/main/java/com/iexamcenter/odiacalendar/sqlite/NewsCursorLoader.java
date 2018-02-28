package com.iexamcenter.odiacalendar.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;

import com.iexamcenter.odiacalendar.utility.PrefManager;

public class NewsCursorLoader extends CursorLoader {
    private static final String TAG = "NEWS";
    private static Uri url = SqliteHelper.CONTENT_URI_NEWS;
    private static final Uri contenturi = Uri.withAppendedPath(url, SqliteHelper.NewsEntry.TABLE_NEWS);
    private Context mContext;
    private String mKey = "";
    int mType = 1;
    int mRowId;
    PrefManager mPref;
    Cursor c = null;

    public NewsCursorLoader(Context context, Bundle arg) {
        super(context);
        mKey = arg.getString("KEY");
        mType = arg.getInt("TYPE");
        mRowId = arg.getInt("ROW_ID");
        mPref = PrefManager.getInstance(context);
        mPref.load();
        this.mContext = context;

    }

    @Override
    public Cursor loadInBackground() {


        String projection[] = new String[]{"rowid", "*"};
        String lang = mPref.getMyLanguage();

        if (mType == 0) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CAT + " LIKE ? AND  " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + " = ?";
            String[] selectionArgs = {"%" + mKey + "%", lang};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_TYPE + " DESC ," + SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 1) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CAT + " LIKE ? AND  " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + " IN (?,?,?)";
            String[] selectionArgs = {"%" + mKey + "%", lang, "hi", "en"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 2) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + " IN (?,?)";
            String[] selectionArgs = {"%" + mKey + "%", lang, "en"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 3) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_TITLE + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + " IN (?,?)";
            String[] selectionArgs = {"%" + mKey + "%", lang, "en"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 4) {
            String selection = SqliteHelper.NewsEntry._ID + " = ?";
            String[] selectionArgs = {"" + mRowId};
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, null);

        } else if (mType == 5) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CAT + " LIKE ? AND  " + SqliteHelper.NewsEntry.KEY_NEWS_TYPE + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + " IN (?,?)";
            String[] selectionArgs = {"%" + mKey + "%", "" + 2, lang, "en"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);

        } else if (mType == 100) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + " = ?";
            String[] selectionArgs = {"%" + mKey + "%", "en"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 101) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "hi"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 102) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "bn"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 103) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "gu"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 104) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "kn"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 105) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "ml"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 106) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "mr"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 107) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "or"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 108) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "pa"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 109) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "ta"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        } else if (mType == 110) {
            String selection = SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " LIKE ? AND " + SqliteHelper.NewsEntry.KEY_NEWS_LANG + "  = ?";
            String[] selectionArgs = {"%" + mKey + "%", "te"};
            String sort = SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " DESC";
            c = getContext().getContentResolver().query(contenturi, projection, selection, selectionArgs, sort);
        }

        c.setNotificationUri(getContext().getContentResolver(), contenturi);
        return c;
    }

}
