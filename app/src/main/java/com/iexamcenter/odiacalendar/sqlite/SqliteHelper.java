package com.iexamcenter.odiacalendar.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

public class SqliteHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "com.iexamcenter.odiacalendar1.db";
    private static String DATABASE_PATH = "path";
    private static final int DATABASE_VERSION = 41;
    public static final String PROVIDER_NAME_FORECAST = "com.iexamcenter.odiacalendar.authoritiesforecast";
    public static final String PROVIDER_NAME_RSS = "com.iexamcenter.odiacalendar.authoritiesrss";

    public static final String PROVIDER_NAME_NEWS = "com.iexamcenter.odiacalendar.authoritiesnews";
    public static final String PROVIDER_NAME_HOROSCOPE = "com.iexamcenter.odiacalendar.authoritieshoroscope";
    public static final String PROVIDER_NAME_NOTIF = "com.iexamcenter.odiacalendar.authoritiesnotif";
    public static final String PROVIDER_NAME_WHATTODAY = "com.iexamcenter.odiacalendar.authoritieswhattoday";
    public static final String PROVIDER_NAME_WEATHER = "com.iexamcenter.odiacalendar.authoritiesweather";

    public static final Uri CONTENT_URI_FORECAST = Uri.parse("content://" + PROVIDER_NAME_FORECAST);
    public static final Uri CONTENT_URI_RSS = Uri.parse("content://" + PROVIDER_NAME_RSS);

    public static final Uri CONTENT_URI_NEWS = Uri.parse("content://" + PROVIDER_NAME_NEWS);
    public static final Uri CONTENT_URI_WEATHER = Uri.parse("content://" + PROVIDER_NAME_WEATHER);
    public static final Uri CONTENT_URI_HOROSCOPE = Uri.parse("content://" + PROVIDER_NAME_HOROSCOPE);
    public static final Uri CONTENT_URI_NOTIF = Uri.parse("content://" + PROVIDER_NAME_NOTIF);
    public static final Uri CONTENT_URI_WHATTODAY = Uri.parse("content://" + PROVIDER_NAME_WHATTODAY);

    public static abstract class ForecastEntry implements BaseColumns {
        public static final String TABLE_FORECAST = "FORECAST";
        public static final String KEY_FORECAST_JSON = "FORECAST_JSON";
        public static final String KEY_FORECAST_TYPE = "FORECAST_TYPE";
        public static final String KEY_FORECAST_DATE = "FORECAST_DATE";
    }

    public static abstract class RssEntry implements BaseColumns {
        public static final String TABLE_RSS = "RSS";
        public static final String KEY_RSS_LINK = "RSS_LINK";
        public static final String KEY_RSS_CHANNEL = "RSS_CHANNEL";
        public static final String KEY_RSS_CAT = "RSS_CAT";
        public static final String KEY_RSS_TYPE = "RSS_TYPE";
        public static final String KEY_RSS_LANG = "RSS_LANG";
        public static final String KEY_RSS_PUBLISH = "RSS_PUBLISH";
        public static final String KEY_RSS_SUBSCRIBE = "RSS_SUBSCRIBE";
        public static final String KEY_RSS_ORDER = "RSS_ORDER";
        public static final String KEY_RSS_DELETE = "RSS_DELETE";
        public static final String KEY_RSS_LAST_MODIFIED = "RSS_LAST_MODIFIED";
    }

    public static abstract class NewsEntry implements BaseColumns {
        public static final String TABLE_NEWS = "NEWS";
        public static final String KEY_NEWS_TITLE = "NEWS_TITLE";
        public static final String KEY_NEWS_CAT = "NEWS_CAT";
        public static final String KEY_NEWS_TYPE = "NEWS_TYPE";
        public static final String KEY_NEWS_CHANNEL = "NEWS_CHANNEL";
        public static final String KEY_NEWS_PUB_TIMESTAMP = "NEWS_PUB_TIMESTAMP";
        public static final String KEY_NEWS_PUB_DATE = "NEWS_PUB_DATE";
        public static final String KEY_NEWS_IMAGE = "NEWS_IMAGE";
        public static final String KEY_NEWS_STATUS = "NEWS_STATUS";
        public static final String KEY_NEWS_LANG = "NEWS_LANG";
        public static final String KEY_NEWS_LINK = "NEWS_LINK";
        public static final String KEY_NEWS_VIDEO_ID = "NEWS_VIDEO_ID";
        public static final String KEY_NEWS_IS_NEW = "IS_NEWS_IS_NEW";
    }

    public static abstract class WhatTodayEntry implements BaseColumns {
        public static final String TABLE_WHATTODAY = "WHATTODAY";
        public static final String KEY_WHATTODAY_YEAR = "WHATTODAY_YEAR";
        public static final String KEY_WHATTODAY_MONTH = "WHATTODAY_MONTH";
        public static final String KEY_WHATTODAY_DAY = "WHATTODAY_DAY";
        public static final String KEY_WHATTODAY_TITLE = "WHATTODAY_TITLE";
        public static final String KEY_WHATTODAY_TYPE = "WHATTODAY_TYPE";
        public static final String KEY_WHATTODAY_IS_POPULAR = "WHATTODAY_IS_POPULAR";
        public static final String KEY_WHATTODAY_WIKI = "WHATTODAY_WIKI";
    }

    public static abstract class NotifEntry implements BaseColumns {
        public static final String TABLE_NOTIF = "NOTIF";
        public static final String KEY_NOTIF_SERVERID = "NOTIF_SERVER_ID";
        public static final String KEY_NOTIF_UNWANTED = "NOTIF_UNWANTED_SEL";
        public static final String KEY_NOTIF_DATE = "NOTIF_DATE";
        public static final String KEY_NOTIF_TITLE = "NOTIF_TITLE";
        public static final String KEY_NOTIF_URL = "NOTIF_URL";
        public static final String KEY_NOTIF_SELECTOR = "NOTIF_STORY_SEL";
        public static final String KEY_NOTIF_LANG = "NOTIF_LANG";
        public static final String KEY_NOTIF_IS_VIEWED = "NOTIF_IS_VIEWED";
        public static final String KEY_NOTIF_TEASER = "NOTIF_TEASER";
    }

    public static abstract class HoroscopeEntry implements BaseColumns {
        public static final String TABLE_HOROSCOPE = "HOROSCOPE";
        public static final String KEY_HOROSCOPE_DESC = "HOROSCOPE_DESC";
        public static final String KEY_HOROSCOPE_DATE = "HOROSCOPE_DATE";
        public static final String KEY_HOROSCOPE_TITLE = "HOROSCOPE_TITLE";
    }

    public static abstract class WeatherEntry implements BaseColumns {
        public static final String TABLE_WEATHER = "WEATHER";
        public static final String KEY_WEATHER_ID = "WEATHER_ID";
        public static final String KEY_WEATHER_CITY_ID = "WEATHER_CITY_ID";
        public static final String KEY_WEATHER_CITY_NAME = "WEATHER_CITY_NAME";
        public static final String KEY_WEATHER_COUNTRY = "WEATHER_COUNTRY";
        public static final String KEY_WEATHER_SUNRISE = "WEATHER_SUNRISE";
        public static final String KEY_WEATHER_SUNSET = "WEATHER_SUNSET";
        public static final String KEY_WEATHER_LAT = "WEATHER_LAT";
        public static final String KEY_WEATHER_LNG = "WEATHER_LNG";
        public static final String KEY_WEATHER_MAIN = "WEATHER_MAIN";
        public static final String KEY_WEATHER_DESC = "WEATHER_DESC";
        public static final String KEY_WEATHER_ICON = "WEATHER_ICON";
        public static final String KEY_WEATHER_TEMP = "WEATHER_TEMP";
        public static final String KEY_WEATHER_TEMP_MIN = "WEATHER_TEMP_MIN";
        public static final String KEY_WEATHER_TEMP_MAX = "WEATHER_TEMP_MAX";
        public static final String KEY_WEATHER_PRESS = "WEATHER_PRESS";
        public static final String KEY_WEATHER_PRESS_SEA_LVL = "WEATHER_PRESS_SEA_LVL";
        public static final String KEY_WEATHER_PRESS_GND_LVL = "WEATHER_PRESS_GND_LVL";
        public static final String KEY_WEATHER_HUMIDITY = "WEATHER_HUMIDITY";
        public static final String KEY_WEATHER_WIND_SPEED = "WEATHER_WIND_SPEED";
        public static final String KEY_WEATHER_WIND_DGR = "WEATHER_WIND_DGR";
        public static final String KEY_WEATHER_MEASURE_TIME = "WEATHER_MEASURE_TIME";
        public static final String KEY_WEATHER_VISIBILITY = "WEATHER_VISIBILITY";
        public static final String KEY_WEATHER_CREATED_AT = "WEATHER_CREATED_AT";

    }

  //  private static final String CREATE_NOTIF_TABLE = "CREATE  TABLE  IF NOT EXISTS " + NotifEntry.TABLE_NOTIF + "  ( " + NotifEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,  " + NotifEntry.KEY_NOTIF_TITLE + " TEXT," + NotifEntry.KEY_NOTIF_TEASER + " TEXT,  " + NotifEntry.KEY_NOTIF_UNWANTED + " TEXT,  " + NotifEntry.KEY_NOTIF_LANG + " TEXT,  " + NotifEntry.KEY_NOTIF_DATE + " TEXT  ,  " + NotifEntry.KEY_NOTIF_URL + " TEXT,  " + NotifEntry.KEY_NOTIF_SELECTOR + " TEXT," + NotifEntry.KEY_NOTIF_IS_VIEWED + " INTEGER," + NotifEntry.KEY_NOTIF_SERVERID + " INTEGER      );";
    private static final String CREATE_WHATTODAY_TABLE = "CREATE  TABLE  IF NOT EXISTS " + WhatTodayEntry.TABLE_WHATTODAY + "  ( " + WhatTodayEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,  " + WhatTodayEntry.KEY_WHATTODAY_TITLE + " TEXT, " + WhatTodayEntry.KEY_WHATTODAY_IS_POPULAR + " INTEGER," + WhatTodayEntry.KEY_WHATTODAY_WIKI + " TEXT ," + WhatTodayEntry.KEY_WHATTODAY_TYPE + " TEXT," + WhatTodayEntry.KEY_WHATTODAY_YEAR + " TEXT," + WhatTodayEntry.KEY_WHATTODAY_MONTH + " TEXT," + WhatTodayEntry.KEY_WHATTODAY_DAY + " TEXT);";
    private static final String CREATE_HOROSCOPE_TABLE = "CREATE  TABLE  IF NOT EXISTS " + HoroscopeEntry.TABLE_HOROSCOPE + "  ( " + HoroscopeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,  " + HoroscopeEntry.KEY_HOROSCOPE_DESC + " TEXT,  " + HoroscopeEntry.KEY_HOROSCOPE_TITLE + " TEXT,  " + HoroscopeEntry.KEY_HOROSCOPE_DATE + " TEXT  );";
    private static final String CREATE_FORECAST_TABLE = "CREATE  TABLE  IF NOT EXISTS  " + ForecastEntry.TABLE_FORECAST + "  ( " + ForecastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,  " + ForecastEntry.KEY_FORECAST_JSON + " TEXT,  " + ForecastEntry.KEY_FORECAST_TYPE + " TEXT  NOT NULL UNIQUE,  " + ForecastEntry.KEY_FORECAST_DATE + " TEXT  );";
    private static final String CREATE_RSS_TABLE = "CREATE  TABLE  IF NOT EXISTS  " + RssEntry.TABLE_RSS + "  ( " + RssEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,  " + RssEntry.KEY_RSS_CHANNEL + " TEXT,  " + RssEntry.KEY_RSS_LINK + " TEXT  NOT NULL UNIQUE,  " + RssEntry.KEY_RSS_CAT + " TEXT ,  " + RssEntry.KEY_RSS_LAST_MODIFIED + " TEXT ,  " + RssEntry.KEY_RSS_PUBLISH + " INTEGER,  " + RssEntry.KEY_RSS_TYPE + " INTEGER  ," + RssEntry.KEY_RSS_SUBSCRIBE + " INTEGER  ," + RssEntry.KEY_RSS_DELETE + " INTEGER ," + RssEntry.KEY_RSS_ORDER + " INTEGER  ,  " + RssEntry.KEY_RSS_LANG + " TEXT );";
    private static final String CREATE_NEWS_TABLE = "CREATE  TABLE  IF NOT EXISTS  " + NewsEntry.TABLE_NEWS + "  ( " + NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,  " + NewsEntry.KEY_NEWS_TITLE + " TEXT,  " + NewsEntry.KEY_NEWS_LINK + " TEXT ,  " + NewsEntry.KEY_NEWS_CHANNEL + " TEXT ,  " + NewsEntry.KEY_NEWS_CAT + " TEXT ,  " + NewsEntry.KEY_NEWS_IMAGE + " TEXT,  " + NewsEntry.KEY_NEWS_LANG + " INTEGER," + NewsEntry.KEY_NEWS_TYPE + " INTEGER,  " + NewsEntry.KEY_NEWS_STATUS + " INTEGER,  " + NewsEntry.KEY_NEWS_PUB_DATE + " TEXT, " + NewsEntry.KEY_NEWS_PUB_TIMESTAMP + " TEXT,  " + NewsEntry.KEY_NEWS_VIDEO_ID + " TEXT , " + NewsEntry.KEY_NEWS_IS_NEW + " TEXT , UNIQUE (" + NewsEntry.KEY_NEWS_LINK + ", " + NewsEntry.KEY_NEWS_CAT + ") );";
    private static final String CREATE_WEATHER_TABLE = "CREATE  TABLE  IF NOT EXISTS   " + WeatherEntry.TABLE_WEATHER + "  ( " + WeatherEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,  " + WeatherEntry.KEY_WEATHER_CITY_ID + " TEXT,  " + WeatherEntry.KEY_WEATHER_CITY_NAME + " TEXT,  " + WeatherEntry.KEY_WEATHER_COUNTRY + " TEXT,  " + WeatherEntry.KEY_WEATHER_SUNRISE + " TEXT,  " + WeatherEntry.KEY_WEATHER_SUNSET + " TEXT,  " + WeatherEntry.KEY_WEATHER_LAT + " TEXT,  " + WeatherEntry.KEY_WEATHER_LNG + " TEXT,  " + WeatherEntry.KEY_WEATHER_MAIN + " TEXT,  " + WeatherEntry.KEY_WEATHER_DESC + " TEXT,  " + WeatherEntry.KEY_WEATHER_ICON + " TEXT, " + WeatherEntry.KEY_WEATHER_TEMP + " TEXT, " + WeatherEntry.KEY_WEATHER_TEMP_MIN + " TEXT, " + WeatherEntry.KEY_WEATHER_TEMP_MAX + " TEXT, " + WeatherEntry.KEY_WEATHER_PRESS + " TEXT, " + WeatherEntry.KEY_WEATHER_PRESS_SEA_LVL + " TEXT, " + WeatherEntry.KEY_WEATHER_PRESS_GND_LVL + " TEXT, " + WeatherEntry.KEY_WEATHER_HUMIDITY + " TEXT, " + WeatherEntry.KEY_WEATHER_WIND_SPEED + " TEXT, " + WeatherEntry.KEY_WEATHER_WIND_DGR + " TEXT, " + WeatherEntry.KEY_WEATHER_MEASURE_TIME + " TEXT NOT NULL UNIQUE, " + WeatherEntry.KEY_WEATHER_VISIBILITY + " TEXT, " + WeatherEntry.KEY_WEATHER_CREATED_AT + " TEXT );";
    private static SqliteHelper sInstance;


    public static SqliteHelper getInstance(Context context) {

        if (sInstance == null) {
            DATABASE_PATH = DATABASE_NAME;
            sInstance = new SqliteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_WEATHER_TABLE);
        db.execSQL(CREATE_FORECAST_TABLE);
        db.execSQL(CREATE_HOROSCOPE_TABLE);
    //    db.execSQL(CREATE_NOTIF_TABLE);
        db.execSQL(CREATE_WHATTODAY_TABLE);
        db.execSQL(CREATE_RSS_TABLE);
        db.execSQL(CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          if(oldVersion < newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WeatherEntry.TABLE_WEATHER);
        db.execSQL("DROP TABLE IF EXISTS " + ForecastEntry.TABLE_FORECAST);
        db.execSQL("DROP TABLE IF EXISTS " + HoroscopeEntry.TABLE_HOROSCOPE);
      //  db.execSQL("DROP TABLE IF EXISTS " + NotifEntry.TABLE_NOTIF);
        db.execSQL("DROP TABLE IF EXISTS " + WhatTodayEntry.TABLE_WHATTODAY);
        db.execSQL("DROP TABLE IF EXISTS " + NewsEntry.TABLE_NEWS);
        db.execSQL("DROP TABLE IF EXISTS " + RssEntry.TABLE_RSS);

        onCreate(db);
        }

    }

}
