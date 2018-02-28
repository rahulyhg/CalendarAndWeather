package com.iexamcenter.odiacalendar;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.widget.TextView;

import com.iexamcenter.odiacalendar.icache.RequestManager;
import com.iexamcenter.odiacalendar.utility.LocaleHelper;
import com.iexamcenter.odiacalendar.utility.PrefManager;

import java.util.Locale;


public class CalendarWeatherApp extends Application {
    static CalendarWeatherApp sInstance;
    PrefManager pref;
    private boolean DEVELOPER_MODE = false;
    public static String[] l_month_arr, e_month_arr, l_masa_arr, l_bara_arr, l_ebara_arr, l_festival_arr, l_number, bara_arr,e_bara_full_arr;
public  static String myLang;
    public void onCreate() {

        super.onCreate();
        // LocaleHelper.setLocale(this,"or");
        myLang="or";
        Locale locale = new Locale(myLang);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        pref = PrefManager.getInstance(this);
        pref.setMyLanguage(myLang);
        pref.load();


        Resources resource = getResources();
        l_month_arr = resource.getStringArray(R.array.l_month_arr);
        e_month_arr = resource.getStringArray(R.array.e_month_arr);
        l_masa_arr = resource.getStringArray(R.array.l_masa_arr);
        l_bara_arr = resource.getStringArray(R.array.l_bara_arr);
        l_ebara_arr = resource.getStringArray(R.array.e_bara_arr);
        e_bara_full_arr = resource.getStringArray(R.array.e_bara_full_arr);

        l_number = resource.getStringArray(R.array.l_number_arr);
        bara_arr = resource.getStringArray(R.array.bara_arr);


        l_festival_arr = getResources().getStringArray(R.array.l_festival_arr);
        sInstance = this;
        if (DEVELOPER_MODE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .detectAll()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectLeakedSqlLiteObjects()
                            .detectLeakedClosableObjects()
                            .penaltyLog()
                            .penaltyDeath()
                            .detectAll()
                            .build());
        }
        RequestManager.init(this);

    }


    public synchronized static CalendarWeatherApp getInstance() {
        return sInstance;
    }

    public void setTextView(TextView v, String txt, int type) {

        Configuration config = getResources().getConfiguration();
        String lang = pref.getMyLanguage();

        if (lang.contains("or") && type == 1) {
            Typeface face1 = Typeface.createFromAsset(getAssets(), "kalinga.ttf");
            v.setTypeface(face1);
        } else if (lang.contains("gu") && type == 1) {
            Typeface face1 = Typeface.createFromAsset(getAssets(), "shruti.ttf");
            v.setTypeface(face1);
        } else if (lang.contains("pa") && type == 1) {
            Typeface face1 = Typeface.createFromAsset(getAssets(), "raavi.ttf");
            v.setTypeface(face1);
        }
        v.setText(txt);
    }
}