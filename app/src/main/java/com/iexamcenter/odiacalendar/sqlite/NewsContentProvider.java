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

public class NewsContentProvider extends ContentProvider {


    public static final String TABLE_NAME = SqliteHelper.NewsEntry.TABLE_NEWS;
    public SqliteHelper mOpenHelper;
    private boolean isDuplicate = false;

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
            isDuplicate = true;
            e.printStackTrace();
        }
        Uri itemUri = null;

        if (rowId > 0) {
            itemUri = ContentUris.withAppendedId(uri, rowId);
            System.out.println("::itemUri::" + itemUri + ":::" + values.get(SqliteHelper.NewsEntry.KEY_NEWS_LINK));
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
                    + " (" + SqliteHelper.NewsEntry.KEY_NEWS_TITLE + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_CAT + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_TYPE + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_PUB_DATE + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_LINK + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_IMAGE + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_LANG + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_IS_NEW + " ,"
                    + SqliteHelper.NewsEntry.KEY_NEWS_VIDEO_ID + ") VALUES "
                    + "(?,?,?,?,?,?,?,?,?,?,?)";

            SQLiteStatement insert =
                    db.compileStatement(sql);
            for (ContentValues value : values) {


                String title = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_TITLE);
                String cat = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_CAT);
                String type = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_TYPE);
                String channel = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL);
                String pubDate = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_PUB_DATE);
                String timestamp = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP);
                String link = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_LINK);
                String image = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_IMAGE);
                String lang = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_LANG);
                String videoId = value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_VIDEO_ID);
                String isNew = "1";//value.getAsString(SqliteHelper.NewsEntry.KEY_NEWS_IS_NEW);

                insert.bindString(1, title);
                insert.bindString(2, cat);
                insert.bindString(3, type);
                insert.bindString(4, channel);
                insert.bindString(5, pubDate);
                insert.bindString(6, timestamp);
                insert.bindString(7, link);
                insert.bindString(8, image);
                insert.bindString(9, lang);
                insert.bindString(10, isNew);
                insert.bindString(11, videoId);
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

    /* @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        int numValues = values.length;
        isDuplicate = false;

        try {
            for (int i = 0; i < numValues; i++) {
                try {
                    Uri myUrl = insert(uri, values[i]);
                    if (myUrl == null || isDuplicate) {
                        System.out.print(myUrl+"Duplicate:"+isDuplicate);
                        return i;
                    }
                } catch (Exception e) {
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return numValues;

    }
*/
    @Override
    public boolean onCreate() {
        mOpenHelper = SqliteHelper.getInstance(getContext());
        return true;
    }

}