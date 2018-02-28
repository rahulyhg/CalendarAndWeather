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

public class HoroscopeContentProvider extends ContentProvider {


    public static final String TABLE_NAME = SqliteHelper.HoroscopeEntry.TABLE_HOROSCOPE;
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
            //  rowId = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

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
                    + " (" + SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_TITLE + " ,"
                    + SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_DATE + " ,"
                    + SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_DESC + ") VALUES "
                    + "(?,?,?)";

            SQLiteStatement insert =
                    db.compileStatement(sql);
            //    Log.e("RSS","RSS:"+sql);
            for (ContentValues value : values) {


                String title = value.getAsString(SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_TITLE);
                String desc = value.getAsString(SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_DESC);
                String pubDate = value.getAsString(SqliteHelper.HoroscopeEntry.KEY_HOROSCOPE_DATE);

                insert.bindString(1, title);
                insert.bindString(2, pubDate);
                insert.bindString(3, desc);
                insert.execute();
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