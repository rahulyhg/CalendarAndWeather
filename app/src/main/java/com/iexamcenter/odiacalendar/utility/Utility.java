package com.iexamcenter.odiacalendar.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.iexamcenter.odiacalendar.CalendarWeatherApp;
import com.iexamcenter.odiacalendar.R;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utility {
    private static Context mContext;
    private static Utility singleton = new Utility();
    private static PrefManager mPref;


    private Utility() {

    }

    static Resources res;

    public static boolean isConnectedMobile() {
        NetworkInfo info = Connectivity.getNetworkInfo(mContext);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public Integer[] getPageThemeColors() {
        // mPref.load();
        if (mPref.isTheme()) {
            Integer[] colors = new Integer[]{R.drawable.rounded_bg_page1, R.drawable.rounded_bg_page2, R.drawable.rounded_bg_page3, R.drawable.rounded_bg_page4, R.drawable.rounded_bg_page5, R.drawable.rounded_bg_page6, R.drawable.rounded_bg_page7, R.drawable.rounded_bg_page8, R.drawable.rounded_bg_page9};
            return colors;
        } else {
            Integer[] colors = new Integer[]{R.drawable.rounded_bg_page1, R.drawable.rounded_bg_page1, R.drawable.rounded_bg_page1, R.drawable.rounded_bg_page1, R.drawable.rounded_bg_page1, R.drawable.rounded_bg_page1, R.drawable.rounded_bg_page1, R.drawable.rounded_bg_page1, R.drawable.rounded_bg_page1};
            return colors;
        }
    }

    public Integer[] getPageColors() {
        //mPref.load();
        if (mPref.isTheme()) {
            Integer[] colors = new Integer[]{mContext.getResources().getColor(R.color.page1), mContext.getResources().getColor(R.color.page2), mContext.getResources().getColor(R.color.page3), mContext.getResources().getColor(R.color.page4)
                    , mContext.getResources().getColor(R.color.page5), mContext.getResources().getColor(R.color.page6), mContext.getResources().getColor(R.color.page7)
                    , mContext.getResources().getColor(R.color.page8), mContext.getResources().getColor(R.color.page9)};
            return colors;
        } else {
            Integer[] colors = new Integer[]{mContext.getResources().getColor(R.color.page1), mContext.getResources().getColor(R.color.page1), mContext.getResources().getColor(R.color.page1), mContext.getResources().getColor(R.color.page1)
                    , mContext.getResources().getColor(R.color.page1), mContext.getResources().getColor(R.color.page1), mContext.getResources().getColor(R.color.page1)
                    , mContext.getResources().getColor(R.color.page1), mContext.getResources().getColor(R.color.page1)};
            return colors;
        }
    }

    public static int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public static Utility getInstance(Context context) {
        mPref = PrefManager.getInstance(context);
        mPref.load();
        mContext = context;
        res = mContext.getResources();
        return singleton;
    }

    public void setClickAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out));
    }

    public int setAnimation(View viewToAnimate, int position, int lastPosition) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation;

            animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);

            viewToAnimate.startAnimation(animation);

            lastPosition = position;
        }
        return lastPosition;
    }

    public void newToast(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        View view = toast.getView();

        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.setBackgroundResource(R.drawable.toast);


        toast.show();
    }

    public void newToastLong(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        View view = toast.getView();

        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.setBackgroundResource(R.drawable.toast);


        toast.show();
    }

    public static int daysBetween(Date startDate, Date endDate) {
        int daysBetween = 0;
        while (startDate.before(endDate)) {
            startDate.setTime(startDate.getTime() + 86400000);
            daysBetween++;
        }
        return daysBetween;
    }

    public String getTimeAgo(long time) {
        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;
        long now = 0;
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        String milli = new SimpleDateFormat("yyyyMMddHHmmssSS").format(new Date()).toString();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmssSS");
        try {
            Date d = f.parse(milli);
            now = d.getTime();
        } catch (ParseException p) {
            p.printStackTrace();
        }


        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    public String getDateTimeFormat(Date date) {
        String timeOfDay = new SimpleDateFormat("HH:mm").format(date);
        java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
        java.sql.Timestamp timeStampNow = new Timestamp((new java.util.Date()).getTime());

        long secondDiff = timeStampNow.getTime() / 1000 - timeStampDate.getTime() / 1000;
        int minuteDiff = (int) (secondDiff / 60);
        int hourDiff = (int) (secondDiff / 3600);
        int dayDiff = daysBetween(date, new Date()) - 1;
        if (dayDiff > 0) {

            System.out.println("Posted " + dayDiff + " days ago @ " + timeOfDay);
            return "Posted " + dayDiff + " days ago @ " + timeOfDay;
        } else if (hourDiff > 0) {
            System.out.println("Posted " + hourDiff + " hour(s) ago @ " + timeOfDay);
            return "Posted " + hourDiff + " hour(s) ago @ " + timeOfDay;
        } else if (minuteDiff > 0) {
            System.out.println("Posted " + minuteDiff + " minute(s) ago @ " + timeOfDay);
            return "Posted " + minuteDiff + " minute(s) ago @ " + timeOfDay;
        } else if (secondDiff > 0) {
            System.out.println("Posted " + secondDiff + " second(s) ago @ " + timeOfDay);
            return "Posted " + secondDiff + " second(s) ago @ " + timeOfDay;
        } else
            return "";
    }


    public String getMyDateTimeFormat(String str_date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
        Date date = null;
        try {
            date = sdf.parse(str_date);


            String timeOfDay = new SimpleDateFormat("HH:mm").format(date);
            java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
            java.sql.Timestamp timeStampNow = new Timestamp((new java.util.Date()).getTime());

            long secondDiff = timeStampNow.getTime() / 1000 - timeStampDate.getTime() / 1000;
            int minuteDiff = (int) (secondDiff / 60);
            int hourDiff = (int) (secondDiff / 3600);
            int dayDiff = daysBetween(date, new Date()) - 1;
            if (dayDiff > 0) {

                System.out.println("posted " + dayDiff + " days ago @ " + timeOfDay);
                return "posted " + dayDiff + " days ago @ " + timeOfDay;
            } else if (hourDiff > 0) {
                System.out.println("posted " + hourDiff + " hour(s) ago @ " + timeOfDay);
                return "posted " + hourDiff + " hour(s) ago @ " + timeOfDay;
            } else if (minuteDiff > 0) {
                System.out.println("posted " + minuteDiff + " minute(s) ago @ " + timeOfDay);
                return "posted " + minuteDiff + " minute(s) ago @ " + timeOfDay;
            } else if (secondDiff > 0) {
                System.out.println("posted " + secondDiff + " second(s) ago @ " + timeOfDay);
                return "posted " + secondDiff + " second(s) ago @ " + timeOfDay;
            } else
                return "";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static void closeKeyboard(FragmentActivity fragmentActivity) {
        try {
            View view = fragmentActivity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) fragmentActivity.getSystemService(FragmentActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(fragmentActivity.getWindow().getCurrentFocus().getWindowToken(), 0);

            }


        } catch (Exception e) {

        }
    }

    public String getTimeStamp(String str_date) {
        // String str_date="Fri, 07 Aug 2015 08:08:31 GMT";
        str_date = str_date.trim();
        System.out.println("-:DATA:-" + str_date);
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            date = sdf.parse(str_date);
            return getDateTimeFormat(date);
        } catch (ParseException e) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss z", Locale.US);
                date = sdf.parse(str_date);
                return getDateTimeFormat(date);
            } catch (ParseException e1) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy HH:mm z", Locale.US);

                    date = sdf.parse(str_date);
                    return getDateTimeFormat(date);
                } catch (ParseException e2) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm z", Locale.US);

                        date = sdf.parse(str_date);
                        return getDateTimeFormat(date);
                    } catch (ParseException e3) {
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
                            date = sdf.parse(str_date);
                            return getDateTimeFormat(date);
                        } catch (ParseException e4) {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss Z", Locale.US);
                                date = sdf.parse(str_date);
                                return getDateTimeFormat(date);
                            } catch (ParseException e5) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm Z", Locale.US);
                                    date = sdf.parse(str_date);
                                    return getDateTimeFormat(date);
                                } catch (ParseException e6) {
                                    try {
                                        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yy HH:mm:ss Z", Locale.US);
                                        date = sdf.parse(str_date);
                                        return getDateTimeFormat(date);
                                    } catch (ParseException e7) {
                                        e7.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        return "X";
    /*
    Date localTime = null;
    try {
        localTime = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US).parse(str_date);
        return localTime.getTime();
       // System.out.println("TimeStamp is " +localTime.getTime());
    } catch (java.text.ParseException e) {
        try {
            localTime = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.US).parse(str_date);
            return localTime.getTime();
        } catch (ParseException e1) {
            e1.printStackTrace();
            return 0l;
        }
    }*/
    }

    public String getLanguageFull() {
        String code = mPref.getMyLanguage();
        String lang = " ";
        if (code.contentEquals("or"))
            return "Odia";
        else if (code.contentEquals("bn"))
            return "Bengali";
        else if (code.contentEquals("hi"))
            return "Hindi";
        else if (code.contentEquals("gu"))
            return "Gujarati";
        else if (code.contentEquals("kn"))
            return "Kannada";
        else if (code.contentEquals("mr"))
            return "Marathi";
        else if (code.contentEquals("ml"))
            return "Malayalam";
        else if (code.contentEquals("pa"))
            return "Punjabi";
        else if (code.contentEquals("ta"))
            return "Tamil";
        else if (code.contentEquals("te"))
            return "Telugu";

        return lang;
    }

    public String[] getFestivalList(Resources res) {

        String[] str = res.getStringArray(R.array.l_festival_arr);
/*

    String festCodeStr = reader.getString(festivalKey);
    String[] festCodeArr = festCodeStr.split("\\|");
    int len_code_fest = festCodeArr.length;
    int festTmpCnt = 0;
    // Locale l = Locale.getDefault();
    configMyLocalLanguage();

    for (String myFestCode : festCodeArr) {
        int festCode = Integer.parseInt(myFestCode);
        if (festCode > 0) {
            festTmpCnt = 1;
            String festivalJsonStr = l_festival_arr[festCode - 1];
            JSONObject festReader = new JSONObject(festivalJsonStr);
            String eFestival = festReader.getString("festival");
            String lFestival = festReader.getString("festivalL");
            String festivalIcon = festReader.getString("icon");

            if (len_code_fest > 1) {
                calobj.isHoliday = calobj.isHoliday + festReader.getString("is_holiday") + Constant.separatorStr;
                calobj.highlight = calobj.highlight + festReader.getString("highlight") + Constant.separatorStr;
                calobj.festivalIcon = calobj.festivalIcon + festivalIcon + Constant.separatorStr;

                calobj.eFestival = calobj.eFestival + eFestival + Constant.separatorStr;
                calobj.festival = calobj.festival + lFestival + Constant.separatorStr;
            } else {
                calobj.isHoliday = calobj.isHoliday + festReader.getString("is_holiday");
                calobj.highlight = calobj.highlight + festReader.getString("highlight");
                calobj.festivalIcon = calobj.festivalIcon + festivalIcon;

                calobj.eFestival = calobj.eFestival + eFestival;
                calobj.festival = calobj.festival + lFestival;

            }


        } else {
            calobj.eFestival = "";
            calobj.festival = "";
            calobj.festivalIcon = "";
        }

    }
    if (festTmpCnt > 0) {
        festivalBucket.add(calobj);
    }
    */

        return res.getStringArray(R.array.l_festival_arr);


    }

    public String[] getMonthRes(String key1, Resources res) {

        String key = "cal" + key1;
        switch (Integer.parseInt(key1)) {

            case 201601:
                return res.getStringArray(R.array.cal201601);
            case 201602:
                return res.getStringArray(R.array.cal201602);
            case 201603:
                return res.getStringArray(R.array.cal201603);
            case 201604:
                return res.getStringArray(R.array.cal201604);
            case 201605:
                return res.getStringArray(R.array.cal201605);
            case 201606:
                return res.getStringArray(R.array.cal201606);
            case 201607:
                return res.getStringArray(R.array.cal201607);
            case 201608:
                return res.getStringArray(R.array.cal201608);
            case 201609:
                return res.getStringArray(R.array.cal201609);
            case 201610:
                return res.getStringArray(R.array.cal201610);
            case 201611:
                return res.getStringArray(R.array.cal201611);
            case 201612:
                return res.getStringArray(R.array.cal201612);
            case 201701:
                return res.getStringArray(R.array.cal201701);
            case 201702:
                return res.getStringArray(R.array.cal201702);
            case 201703:
                return res.getStringArray(R.array.cal201703);
            case 201704:
                return res.getStringArray(R.array.cal201704);
            case 201705:
                return res.getStringArray(R.array.cal201705);
            case 201706:
                return res.getStringArray(R.array.cal201706);
            case 201707:
                return res.getStringArray(R.array.cal201707);
            case 201708:
                return res.getStringArray(R.array.cal201708);
            case 201709:
                return res.getStringArray(R.array.cal201709);
            case 201710:
                return res.getStringArray(R.array.cal201710);
            case 201711:
                return res.getStringArray(R.array.cal201711);
            case 201712:
                return res.getStringArray(R.array.cal201712);
            case 201801:
                return res.getStringArray(R.array.cal201801);
            case 201802:
                return res.getStringArray(R.array.cal201802);
            case 201803:
                return res.getStringArray(R.array.cal201803);
            case 201804:
                return res.getStringArray(R.array.cal201804);
            case 201805:
                return res.getStringArray(R.array.cal201805);
            case 201806:
                return res.getStringArray(R.array.cal201806);
            case 201807:
                return res.getStringArray(R.array.cal201807);
            case 201808:
                return res.getStringArray(R.array.cal201808);
            case 201809:
                return res.getStringArray(R.array.cal201809);
            case 201810:
                return res.getStringArray(R.array.cal201810);
            case 201811:
                return res.getStringArray(R.array.cal201811);
            case 201812:
                return res.getStringArray(R.array.cal201812);


        }
        return null;
    }

    public String getDayNo(String dayNo, Resources res) {


        String word = "";
        for (int i = 0; i < dayNo.length(); i++) {
            int digit = Integer.parseInt("" + dayNo.charAt(i));
            word += CalendarWeatherApp.l_number[digit];

        }
        return word;
    }

    public String getMonthEng(int month, Resources res) {
        return CalendarWeatherApp.e_month_arr[month];
    }

    public String getMonth(int month, Resources res) {
        return CalendarWeatherApp.l_month_arr[month];

    }


    public String getDayWordLocal(int dayOfWeek, Resources res) {

        return CalendarWeatherApp.bara_arr[dayOfWeek - 1];
    }

    public String getDayWord(int dayOfWeek, Resources res) {

        return CalendarWeatherApp.l_bara_arr[dayOfWeek - 1];

    }

    public String getDayWordEng(int dayOfWeek, Resources res) {
        return CalendarWeatherApp.l_bara_arr[dayOfWeek - 1];

    }


    public List<Address> getAddressFromPosition(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
//        geocoder = new Geocoder(mContext, Locale.getDefault());
        geocoder = new Geocoder(mContext, Locale.ENGLISH);

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        return addresses;
    }

    public static String getDateCurrentTimeZoneForMoon(String timeZoneStr, String datetime) {
        try {
            if (datetime.endsWith(" UT")) {
                datetime = datetime.substring(0, datetime.length() - 3);

            }

            java.text.DateFormat formatter1;
            formatter1 = new SimpleDateFormat("yyyy/MM/d HH:mm:s");
            Date date = (Date) formatter1.parse(datetime);
            Timestamp timeStampDate = new Timestamp(date.getTime());

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneStr));
            TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
            calendar.setTimeInMillis(timeStampDate.getTime());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd:hh:mm a");
            Date currenTimeZone = (Date) calendar.getTime();
            String dttime = sdf.format(currenTimeZone);


            return dttime;
        } catch (Exception e) {
        }
        return "";
    }

    public static boolean isTimeAhead(String datetime) {
        try {
            Calendar cal = Calendar.getInstance();
            datetime = datetime + "/" + cal.get(Calendar.YEAR);

            java.text.DateFormat formatter1;
            formatter1 = new SimpleDateFormat("M/d/yyyy", Locale.US);
            Date date = (Date) formatter1.parse(datetime);
            long timeStamp = date.getTime() + 24 * 60 * 60 * 1000;
            Timestamp timeStampDate = new Timestamp(timeStamp);
            TimeZone tz = TimeZone.getTimeZone("GMT");
            Calendar calendar = Calendar.getInstance(tz, Locale.US);
            long timeStamp1 = calendar.getTimeInMillis();
            calendar.setTimeInMillis(timeStampDate.getTime());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            Date currenTimeZone = (Date) calendar.getTime();
            // long timeStamp1 = currenTimeZone.getTime();

            if (timeStamp1 < timeStamp) {

                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static String getDateFromString(String datetime) {
        try {

            String[] date1 = datetime.split("/");
            String monthStr = Utility.getInstance(mContext).getMonthEng(Integer.parseInt(date1[0]) - 1, res);
            return monthStr + "-" + date1[1];
            /*
            String format1 = "MMMM-dd";

            java.text.DateFormat formatter1;
            formatter1 = new SimpleDateFormat("M/d/yyyy", Locale.US);
            Date date = (Date) formatter1.parse(datetime);
            Timestamp timeStampDate = new Timestamp(date.getTime());
            TimeZone tz = TimeZone.getTimeZone("GMT");
            Calendar calendar = Calendar.getInstance(tz, Locale.US);

            calendar.setTimeInMillis(timeStampDate.getTime());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat(format1);
            Date currenTimeZone = (Date) calendar.getTime();
            String dttime = sdf.format(currenTimeZone);


            return dttime;
            */
        } catch (Exception e) {
        }
        return "";
    }

    public static String getDateCurrentTimeZone(String timeZoneStr, String datetime) {
        try {
            if (datetime.endsWith(" UT")) {
                datetime = datetime.substring(0, datetime.length() - 3);

            }

            java.text.DateFormat formatter1;
            formatter1 = new SimpleDateFormat("yyyy/MM/d HH:mm:s");
            Date date = (Date) formatter1.parse(datetime);
            Timestamp timeStampDate = new Timestamp(date.getTime());

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(timeZoneStr));
            TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
            calendar.setTimeInMillis(timeStampDate.getTime());
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            Date currenTimeZone = (Date) calendar.getTime();
            String dttime = sdf.format(currenTimeZone);


            return dttime;
        } catch (Exception e) {
        }
        return "";
    }


    public String getDateTimeForecastGrid(String time1) {
        long time = Long.parseLong(time1) * 1000L;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        // final String format = Settings.System.getString(mContext.getContentResolver(), Settings.System.DATE_FORMAT);

        String date = DateFormat.format("EEEE", cal).toString();
        return date;
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public String getDateTime(String time1) {
        long time = Long.parseLong(time1) * 1000L;
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        // final String format = Settings.System.getString(mContext.getContentResolver(), Settings.System.DATE_FORMAT);

        String date = DateFormat.format("dd/MM/yyyy hh:mm a", cal).toString();
        return date;
    }

    public boolean isLocEnabled() {
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            return false;
        } else {
            return true;
        }

    }


    public static class tag {
        public String link, imageurl;
        public boolean isShowing = false;
    }


    public static class MoonDetails {
        public String moonRise, moonSet, eMoonRise, eMoonSet, age, distance, title, date, moonEl, moonAz;
        public int isRiseFirst, moonImage;

    }

    public static class SunDetails {
        public String aSunRise, aSunSet, nSunRise, nSunSet, cSunRise, cSunSet;

        public String eSunRise, eSunSet, sunRise, sunSet, sunTransit, eSunTransit, sunDist, sunTransitElev, date, sunEl, sunAz;
    }

    public ArrayList<SunDetails> getSunDetails(Calendar calendar1) {
        try {
            mPref.load();
            double lat = Double.parseDouble(mPref.getLatitude());
            double lng = Double.parseDouble(mPref.getLongitude());
            ArrayList<SunDetails> md = new ArrayList<>();
            String timeZoneStr = TimeZone.getDefault().getID();

            md.clear();
            for (int i = 0; i < calendar1.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 1);


                calendar.add(Calendar.DATE, i);
                int hr = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                int sec = calendar.get(Calendar.SECOND);
                int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH), h = calendar.get(Calendar.HOUR), m = calendar.get(Calendar.MINUTE), s = calendar.get(Calendar.SECOND);

                double obsLon = lng * SunMoonCalculator.DEG_TO_RAD, obsLat = lat * SunMoonCalculator.DEG_TO_RAD;
                SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, hr, min, sec, obsLon, obsLat);
                smc.calcSunAndMoon();


                String sunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
                String sunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet));
                String sunTransit = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunTransit));


                double sunDist = smc.sunDist * SunMoonCalculator.AU;
                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.CEILING);


                SunDetails mdObj = new SunDetails();
                mdObj.sunAz = df.format(smc.sunAz * SunMoonCalculator.RAD_TO_DEG);
                mdObj.sunEl = df.format(smc.sunEl * SunMoonCalculator.RAD_TO_DEG);
                mdObj.sunRise = sunRise;
                mdObj.sunSet = sunSet;
                mdObj.sunTransit = sunTransit;
                mdObj.sunTransitElev = df.format(smc.sunTransitElev * SunMoonCalculator.RAD_TO_DEG);
                ;
                mdObj.sunDist = df.format(sunDist);
                mdObj.date = android.text.format.DateFormat.format("dd/MM/yyyy", calendar).toString();

                smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_ASTRONOMICAL);
                smc.calcSunAndMoon();
                String aSunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
                String aSunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet));
                mdObj.aSunRise = aSunRise;
                mdObj.aSunSet = aSunSet;

                smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_NAUTICAL);
                smc.calcSunAndMoon();
                String nSunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
                String nSunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet));
                mdObj.nSunRise = nSunRise;
                mdObj.nSunSet = nSunSet;

                smc.setTwilight(SunMoonCalculator.TWILIGHT.TWILIGHT_CIVIL);
                smc.calcSunAndMoon();
                String cSunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
                String cSunSet = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunSet));
                mdObj.cSunRise = cSunRise;
                mdObj.cSunSet = cSunSet;


                md.add(i, mdObj);
            }
            return md;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<MoonDetails> getMoonDetails(Calendar calendar1) {
        try {
            mPref.load();
            double lat = Double.parseDouble(mPref.getLatitude());
            double lng = Double.parseDouble(mPref.getLongitude());
            ArrayList<MoonDetails> md = new ArrayList<>();
            String timeZoneStr = TimeZone.getDefault().getID();
            String dateStr;
            md.clear();

            //  Calendar calendar=Calendar.getInstance();
            // calendar.set(calendar1.get(Calendar.YEAR),calendar1.get(Calendar.MONTH),1);

            for (int i = 0; i < calendar1.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), 1);

                calendar.add(Calendar.DATE, i);
                int year = calendar.get(Calendar.YEAR), month = calendar.get(Calendar.MONTH), day = calendar.get(Calendar.DAY_OF_MONTH), h = calendar.get(Calendar.HOUR), m = calendar.get(Calendar.MINUTE), s = calendar.get(Calendar.SECOND);
                int monthX = calendar.get(Calendar.MONTH) + 1;
                int dayX = calendar.get(Calendar.DAY_OF_MONTH);

                double obsLon = lng * SunMoonCalculator.DEG_TO_RAD, obsLat = lat * SunMoonCalculator.DEG_TO_RAD;
                SunMoonCalculator smc = new SunMoonCalculator(year, month + 1, day, 12, 0, 0, obsLon, obsLat);
                smc.calcSunAndMoon();


                String moonRise = Utility.getInstance(mContext).getDateCurrentTimeZoneForMoon(timeZoneStr, SunMoonCalculator.getDateAsString(smc.moonRise));
                String moonSet = Utility.getInstance(mContext).getDateCurrentTimeZoneForMoon(timeZoneStr, SunMoonCalculator.getDateAsString(smc.moonSet));


                String tmp1 = SunMoonCalculator.getDateAsString(smc.moonRise);
                String tmp2 = SunMoonCalculator.getDateAsString(smc.moonSet);
                double moonRiseT = 0;
                double moonSetT = 0;
                if (tmp1.endsWith(" UT")) {
                    tmp1 = tmp1.substring(0, tmp1.length() - 3);
                    java.text.DateFormat formatter1;
                    formatter1 = new SimpleDateFormat("yyyy/MM/d h:m:s");
                    Date date = (Date) formatter1.parse(tmp1);
                    Timestamp timeStampDate = new Timestamp(date.getTime());
                    moonRiseT = timeStampDate.getTime();
                }
                if (tmp2.endsWith(" UT")) {
                    tmp2 = tmp2.substring(0, tmp2.length() - 3);
                    java.text.DateFormat formatter1;
                    formatter1 = new SimpleDateFormat("yyyy/MM/d h:m:s");
                    Date date = (Date) formatter1.parse(tmp2);
                    Timestamp timeStampDate = new Timestamp(date.getTime());
                    moonSetT = timeStampDate.getTime();
                }


                //double moonAge =Math.round(smc.moonAge);
                double moonAge = smc.moonAge;

                double moonDistance = smc.moonDist * SunMoonCalculator.AU;
                String moonTitle = "";
                if (moonAge >= 0 && moonAge < 1) {
                    moonTitle = "New moon";
                } else if (moonAge >= 1 && moonAge < 7) {
                    moonTitle = "Waxing crescent";
                } else if (moonAge >= 7 && moonAge < 8) {
                    moonTitle = "First quarter";
                } else if (moonAge >= 8 && moonAge < 15) {
                    moonTitle = "Waxing gibbous";
                } else if (moonAge >= 15 && moonAge < 16) {
                    moonTitle = "Full moon";
                } else if (moonAge >= 16 && moonAge < 21) {
                    moonTitle = "Waning gibbous";
                } else if (moonAge >= 21 && moonAge < 22) {
                    moonTitle = "Last quarter";
                } else if (moonAge >= 22) {
                    moonTitle = "Waning crescent";
                }
                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.CEILING);


                MoonDetails mdObj = new MoonDetails();
                mdObj.moonAz = df.format(smc.moonAz * SunMoonCalculator.RAD_TO_DEG);
                mdObj.moonEl = df.format(smc.moonEl * SunMoonCalculator.RAD_TO_DEG);
                mdObj.moonRise = moonRise;
                mdObj.moonSet = moonSet;
                mdObj.age = df.format(moonAge);
                mdObj.title = moonTitle;
                mdObj.distance = df.format(moonDistance);
                mdObj.date = android.text.format.DateFormat.format("dd/MM/yyyy", calendar).toString();
                mdObj.moonImage = getMoonImage(moonAge);

                if (moonRiseT > moonSetT) {
                    mdObj.isRiseFirst = 0;
                } else {
                    mdObj.isRiseFirst = 1;
                }

                md.add(i, mdObj);
            }
            return md;


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static int getMoonImage(double moonAge) {
        return 1;

    }

    public SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String password = "MyFamilyTheGreat"; // maximum size 16,24,32 etc
        SecretKey secret = null;
        return secret = new SecretKeySpec(password.getBytes(), "AES");
    }

    // String base64 = Base64.encode("data".getBytes(), Base64.DEFAULT);
    // byte[] data = Base64.decode(base64, Base64.DEFAULT);
    // String text = new String(data, "UTF-8");


    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

   /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
    /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");

        return decryptString;
    }

    public String decryptMsg(byte[] cipherText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {
        SecretKey secret = generateKey();
    /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");

        return decryptString;
    }

    /*
        public String quote() {
            ArrayList<String> hm = new ArrayList<>();
    //StringBuilder sb=new StringBuilder();
            try {
                // InputStream fis = asset.open("quote.txt");
                InputStream fis = mContext.getResources().openRawResource(R.raw.bquote);

                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String line;

                int cnt = 1;
                String temp = "";
                SecretKey secret = generateKey();
                while ((line = br.readLine()) != null) {

                    byte[] encryptedStr = encryptMsg(line, secret);
                    Log.e("encryptedStr", ++cnt + ":encryptedStr:" + encryptedStr);
                    String decryptedStr = decryptMsg(encryptedStr, secret);
                    Log.e("encryptedStr", ":encryptedStr:" + decryptedStr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidParameterSpecException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }

            Random rand = new Random();
            int index = rand.nextInt(hm.size() - 1);

            return hm.get(index);

        }
    */
    public void writeFile(String strToWrite) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/QUOTEFILE.txt");
        try {
            FileOutputStream fos = new FileOutputStream(file, true);
            fos.write(strToWrite.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
