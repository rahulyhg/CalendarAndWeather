package com.iexamcenter.odiacalendar.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

public class RssContentProvider extends ContentProvider {


    public static final String TABLE_NAME = SqliteHelper.RssEntry.TABLE_RSS;
    public SqliteHelper mOpenHelper;

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();


		/*added code start*/

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);


		/*added code end*/

        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;

    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub
        long rowId = 0;
        try {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            rowId = db.insertOrThrow(TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Uri itemUri = null;

        if (rowId > 0) {
            itemUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return itemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int status = db.delete(TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        // TODO Auto-generated method stub
        return status;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int cntUpdate = db.update(TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cntUpdate;


    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {


        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numInserted = 0;

        try {
            db.beginTransaction();

            String sql = "INSERT INTO  " + TABLE_NAME
                    + " (" + SqliteHelper.RssEntry.KEY_RSS_CAT + " ,"
                    + SqliteHelper.RssEntry.KEY_RSS_DELETE + " ,"
                    + SqliteHelper.RssEntry.KEY_RSS_LAST_MODIFIED + " ,"
                    + SqliteHelper.RssEntry.KEY_RSS_PUBLISH + " ,"
                     + SqliteHelper.RssEntry.KEY_RSS_CHANNEL + " ,"
                    + SqliteHelper.RssEntry.KEY_RSS_LINK + " ,"
                   + SqliteHelper.RssEntry.KEY_RSS_LANG + " ,"
                    + SqliteHelper.RssEntry.KEY_RSS_ORDER + " ,"
                   + SqliteHelper.RssEntry.KEY_RSS_SUBSCRIBE + " ,"
                     + SqliteHelper.RssEntry.KEY_RSS_TYPE + ") VALUES "
                    + "(?,?,?,?,?,?,?, ?,?, ?)";

            SQLiteStatement insert =
                    db.compileStatement(sql);
        //    Log.e("RSS","RSS:"+sql);
            for (ContentValues value : values) {

                String rssLink = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_LINK);
                String rssChannel = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_CHANNEL);
                String rssType = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_TYPE);
                String rssCat = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_CAT);
                String rssLang = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_LANG);
                String rssSbc = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_SUBSCRIBE);
                String rssOrder = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_ORDER);
                String rssDel = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_DELETE);
                String rssModTime = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_LAST_MODIFIED);
                String rssPub = value.getAsString(SqliteHelper.RssEntry.KEY_RSS_PUBLISH);

                insert.bindString(1, rssCat);
                insert.bindString(2, rssDel);
                insert.bindString(3, rssModTime);
                insert.bindString(4, rssPub);
                insert.bindString(5, rssChannel);
                insert.bindString(6, rssLink);
                insert.bindString(7, rssLang);
                insert.bindString(8, rssOrder);
                insert.bindString(9, rssSbc);
                insert.bindString(10, rssType);
                insert.execute();
              //  Log.e("RSS","RSS:"+rssChannel);
            }


            db.setTransactionSuccessful();
            numInserted = values.length;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numInserted;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = SqliteHelper.getInstance(getContext());
        return true;
    }

}