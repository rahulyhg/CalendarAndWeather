package com.iexamcenter.odiacalendar.month;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iexamcenter.odiacalendar.CalendarWeatherApp;
import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.utility.Constant;
import com.iexamcenter.odiacalendar.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by sasikanta on 11/14/2017.
 * CalendarPagerFragment
 */

public class CalendarPagerFragment extends Fragment {
    public static String ARG_POSITION = "CURRENT_MONTH";
    Resources res;
    ViewGroup rootView;
    int dateSize;
    int num;
    int festivalGap;
    ArrayList<calendardata> festivalBucket;
    MainActivity activity;
    LinearLayout daysContainer, week1Container, week2Container, week3Container, week4Container, week5Container, week6Container, festivalContainer;
    int displayMonth;
    String[] dayVal, festiValArr;
    TextView month;
    LinearLayout.LayoutParams param, titleParam, festivalParam;
    FrameLayout.LayoutParams flParamCenter, flParamTopCenter, flParamBottomCenter, flParamStartTop, flParamStartBottom;
    long startTime, endTime;
    int x = 0;
    // String[] l_bara_arr, l_masa_arr, l_month_arr, l_ebara_arr;
    //String[] l_festival_arr;
    ArrayList<Integer> festivalImages = new ArrayList<>();
    int displayYearInt, displayMonthInt;
    int currYearInt, currMonthInt, currDayInt;

    public static CalendarPagerFragment newInstance() {


        return new CalendarPagerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        startTime = System.currentTimeMillis();
        rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_calendar_sub_page, container, false);
        setRetainInstance(false);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        int viewHeight = Utility.getInstance(activity).dpToPx(70);
        int iconDim = Utility.getInstance(activity).dpToPx(40);
        festivalGap = Utility.getInstance(activity).dpToPx(10);
        displayMonth = bundle.getInt(CalendarPagerFragment.ARG_POSITION);

        flParamCenter = new FrameLayout.LayoutParams(iconDim,
                iconDim
                , Gravity.CENTER
        );
        titleParam = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , 1f
        );

        dateSize = Utility.getInstance(activity).dpToPx(8);
        param = new LinearLayout.LayoutParams(0,
                viewHeight
                , 1f
        );
        festivalParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , 1f
        );
        flParamStartTop = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.START | Gravity.TOP
        );
        flParamTopCenter = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.TOP | Gravity.END
        );
        flParamStartBottom = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.START | Gravity.BOTTOM
        );
        flParamBottomCenter = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                , Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL
        );
        num = bundle != null ? bundle.getInt(ARG_POSITION, 0) : 0;
        month = rootView.findViewById(R.id.month);
        daysContainer = rootView.findViewById(R.id.daysContainer);
        week1Container = rootView.findViewById(R.id.week1Container);
        week2Container = rootView.findViewById(R.id.week2Container);
        week3Container = rootView.findViewById(R.id.week3Container);
        week4Container = rootView.findViewById(R.id.week4Container);
        week5Container = rootView.findViewById(R.id.week5Container);
        week6Container = rootView.findViewById(R.id.week6Container);
        festivalContainer = rootView.findViewById(R.id.festivalContainer);
        res = getResources();

        //    l_month_arr = getResources().getStringArray(R.array.l_month_arr);
        //l_masa_arr = getResources().getStringArray(R.array.l_masa_arr);
        //  l_bara_arr = getResources().getStringArray(R.array.l_bara_arr);
        // l_ebara_arr = getResources().getStringArray(R.array.e_bara_arr);


        festivalBucket = new ArrayList<>();
        //   res.getStringArray(R.array.l_festival_arr);
        //  l_festival_arr = getResources().getStringArray(R.array.l_festival_arr);
        festiValArr = Utility.getInstance(activity).getFestivalList(res);
        addTitle();
        int dayOfMonth, dayOfWeekToday, dayOfLastMonth, dayOfCurrMonth, dayOfWeek1st;
        Calendar cal = Calendar.getInstance();
        currMonthInt = cal.get(Calendar.MONTH);
        currYearInt = cal.get(Calendar.YEAR);
        currDayInt = cal.get(Calendar.DAY_OF_MONTH);

        // First day of week is Sunday==1 for US locale
        //  firstDayOfWeek = cal.getFirstDayOfWeek();
        Log.e("DISPLAY", displayMonth + "DISPLAY:" + cal.getTime());
        cal.add(Calendar.MONTH, displayMonth);
        displayMonthInt = cal.get(Calendar.MONTH);
        displayYearInt = cal.get(Calendar.YEAR);

        //month.setText("MONTH:" + cal.getTime());
        Log.e("DISPLAY", displayMonth + "DISPLAY:" + cal.getTime());
        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        dayOfCurrMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayOfWeekToday = cal.get(Calendar.DAY_OF_WEEK);

        cal.set(Calendar.DAY_OF_MONTH, 1);
        dayOfWeek1st = cal.get(Calendar.DAY_OF_WEEK);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        cal.add(Calendar.MONTH, -1);
        dayOfLastMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.MONTH, 1);


        dayVal = Utility.getInstance(activity).getMonthRes(getKey(cal, -1), getResources());
        if (dayVal == null) {
            dayVal = getDayValJson(cal, -1);
        }
        int index = 0;
        for (int i = dayOfWeek1st - 2; i >= 0; i--) {
            int day = (dayOfLastMonth - i);
            FrameLayout fl = getLayout();
            FrameLayout fll = setDate(index, fl, day);
            setLocalDate(index, fll, getLocalDate(day, -1));
            fl.setAlpha(0.2f);
            index++;

        }
        dayVal = Utility.getInstance(activity).getMonthRes(getKey(cal, 0), getResources());
        if (dayVal == null) {
            dayVal = getDayValJson(cal, 0);
        }
        System.out.print("DATEE:" + currDayInt + "--" + currMonthInt + "--" + displayMonth + "--" + currYearInt + "--" + displayYearInt);
        for (int i = 1; i <= dayOfCurrMonth; i++) {
            festivalImages.clear();
            FrameLayout fl = getLayout();
            String localData = getLocalDate(i, 0);

            if (festivalImages.size() > 0)
                viewWithFestivalImage(index, fl, i, localData);
            else
                viewWithOutFestivalImage(index, fl, i, localData);
            index++;


        }
        dayVal = Utility.getInstance(activity).getMonthRes(getKey(cal, 1), getResources());
        if (dayVal == null) {
            dayVal = getDayValJson(cal, 1);
        }
        int dayMax = 42;
        if ((dayOfCurrMonth + dayOfWeek1st) <= 36)
            dayMax = 35;
        for (int k = 1, i = index; i < dayMax; i++) {

            FrameLayout fl = getLayout();
            FrameLayout fll = setDate(index, fl, k);
            setLocalDate(index, fll, getLocalDate(k, 1));
            fl.setAlpha(0.2f);
            k++;
            index++;
        }

        endTime = System.currentTimeMillis();
        Log.e("TIMEDIFF", "TIMEDIFF:" + (endTime - startTime));
        return rootView;
    }

    private FrameLayout setDate(int index, FrameLayout fl, int day) {
        TextView tv = new TextView(activity);
        // tv.setBackground(getResources().getDrawable(R.drawable.border));
        tv.setLayoutParams(flParamCenter);
        tv.setText("" + day);
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(dateSize);
        fl.addView(tv);
        return fl;

      /*  if (index < 7) {
            week1Container.addView(fl);
        } else if (index < 14) {
            week2Container.addView(fl);
        } else if (index < 21) {
            week3Container.addView(fl);
        } else if (index < 28) {
            week4Container.addView(fl);

        } else if (index < 35) {
            week5Container.addView(fl);

        } else if (index < 42) {
            week6Container.addView(fl);

        }*/

    }

    private void viewWithFestivalImage(int index, FrameLayout fl, int day, String localDay) {

        ImageView iv = new ImageView(activity);
        //tv.setBackground(getResources().getDrawable(R.drawable.border));

        final AnimationDrawable animation = new AnimationDrawable();


        for (Integer imgRes : festivalImages) {
            Log.e("festivalIcon", "festivalIcon::" + imgRes);
            animation.addFrame(activity.getResources().getDrawable(imgRes), 2000);

        }

        animation.setOneShot(false);
        iv.setBackground(animation);
        animation.start();
        iv.setLayoutParams(flParamCenter);
        fl.addView(iv);


        TextView tv1 = new TextView(activity);
        tv1.setLayoutParams(flParamTopCenter);
        tv1.setText("" + day);
        tv1.setGravity(Gravity.START | Gravity.TOP);

        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        fl.addView(tv1);

        TextView tv3 = new TextView(activity);
        //tv.setBackground(getResources().getDrawable(R.drawable.border));
        tv3.setLayoutParams(flParamStartBottom);
        tv3.setText("" + localDay);
        tv3.setAlpha(0.5f);
        // tv3.setGravity(Gravity.TOP | Gravity.END);
        fl.addView(tv3);
        addChild(index, fl);

        if ((day == currDayInt) && (currMonthInt == (displayMonth + 1)) && (currYearInt == displayYearInt)) {
            fl.setBackgroundResource(R.color.colorPrimaryDark);
           // fl.setAlpha(0.5f);
            tv1.setTextColor(Color.WHITE);
            tv3.setTextColor(Color.WHITE);
        }


    }

    private void viewWithOutFestivalImage(int index, FrameLayout fl, int day, String localDay) {


        TextView tv1 = new TextView(activity);
        //tv.setBackground(getResources().getDrawable(R.drawable.border));
        tv1.setLayoutParams(flParamCenter);
        tv1.setText("" + day);
        tv1.setGravity(Gravity.CENTER);
        tv1.setTypeface(Typeface.DEFAULT_BOLD);
        tv1.setTextSize(dateSize);
        fl.addView(tv1);

        TextView tv3 = new TextView(activity);
        //tv.setBackground(getResources().getDrawable(R.drawable.border));
        tv3.setLayoutParams(flParamStartBottom);
        tv3.setText("" + localDay);
        tv3.setAlpha(0.7f);
        //tv3.setGravity(Gravity.TOP | Gravity.END);
        fl.addView(tv3);
        addChild(index, fl);
        if ((day == currDayInt) && (currMonthInt == (displayMonth + 1)) && (currYearInt == displayYearInt)) {
            fl.setBackgroundResource(R.color.colorPrimaryDark);
            //fl.setAlpha(0.5f);
            tv1.setTextColor(Color.WHITE);
            tv3.setTextColor(Color.WHITE);
        }
    }

    private void addFestival(String str, int type) {
        TextView tv1 = new TextView(activity);
        //tv.setBackground(getResources().getDrawable(R.drawable.border));
        tv1.setLayoutParams(festivalParam);
        tv1.setText("" + str);
        tv1.setTextColor(res.getColor(R.color.carbon));
        if (type == 1) {
            tv1.setPadding(0, festivalGap, 0, 0);
            tv1.setTypeface(Typeface.DEFAULT_BOLD);
        }
        //tv1.setGravity(Gravity.CENTER);
        //tv1.setTypeface(Typeface.DEFAULT_BOLD);
        // tv1.setTextSize(dateSize);
        festivalContainer.addView(tv1);
    }

    private void addChild(int index, FrameLayout fl) {
        if (index < 7) {
            week1Container.addView(fl);
        } else if (index < 14) {
            week2Container.addView(fl);
        } else if (index < 21) {
            week3Container.addView(fl);
        } else if (index < 28) {
            week4Container.addView(fl);

        } else if (index < 35) {
            week5Container.addView(fl);

        } else if (index < 42) {
            week6Container.addView(fl);

        }
    }

    private void setLocalDate(int index, FrameLayout fl, String day) {
        TextView tv = new TextView(activity);
        //tv.setBackground(getResources().getDrawable(R.drawable.border));
        tv.setLayoutParams(flParamStartBottom);
        tv.setText("" + day);

        tv.setGravity(Gravity.END | Gravity.TOP);
        tv.setAlpha(0.5f);
        fl.addView(tv);
        addChild(index, fl);


    }

    private String getKey(Calendar cal, int type) {
        String monthKey;
        if (type == -1) {
            cal.add(Calendar.MONTH, -1);
            monthKey = "" + (cal.get(Calendar.MONTH) + 1);
            if (Integer.parseInt(monthKey) < 10) {
                monthKey = "0" + monthKey;
            }
            cal.add(Calendar.MONTH, 1);
            return "" + cal.get(Calendar.YEAR) + "" + monthKey;
        } else if (type == 1) {
            cal.add(Calendar.MONTH, 1);
            monthKey = "" + (cal.get(Calendar.MONTH) + 1);
            if (Integer.parseInt(monthKey) < 10) {
                monthKey = "0" + monthKey;
            }
            cal.add(Calendar.MONTH, -1);
            return "" + cal.get(Calendar.YEAR) + "" + monthKey;
        } else {
            monthKey = "" + (cal.get(Calendar.MONTH) + 1);
            if (Integer.parseInt(monthKey) < 10) {
                monthKey = "0" + monthKey;
            }
            return "" + cal.get(Calendar.YEAR) + "" + monthKey;
        }

    }

    private String getLocalDate(int day, int type) {


        try {

            String obj = dayVal[day - 1];
            JSONObject reader = new JSONObject(obj);
            String lDt = reader.getString("ldt");

            String localDate = Utility.getInstance(activity).getDayNo(lDt, res);

            System.out.println("localDate:" + localDate);
            String tithiCode = reader.getString("tithi");
            String is_sknt = reader.getString("is_sknt");
            String festCodeStr = reader.getString(CalendarWeatherApp.myLang + "_fc");
            String pakshacode = reader.getString("paksha");
            String lmonthcode = reader.getString("lmonth");
            String[] festCodeArr = festCodeStr.split("\\|");
            int len_code_fest = festCodeArr.length;
            if (type != 0) {
                //type 0= current month
                return localDate;
            }
            if (festCodeArr.length > 0 && !festCodeArr[0].equalsIgnoreCase("0")) {

                // String eDayStr = Utility.getInstance(activity).getDayWordEng(cal.get(Calendar.DAY_OF_WEEK), activity.getResources());
                String hinduMonth = CalendarWeatherApp.l_masa_arr[Integer.parseInt(lmonthcode)];
                Calendar cal1 = Calendar.getInstance();
                cal1.set(displayYearInt, displayMonthInt, day);
                String eDayStr = CalendarWeatherApp.e_bara_full_arr[cal1.get(Calendar.DAY_OF_WEEK) - 1];

                String dayWord = CalendarWeatherApp.l_bara_arr[cal1.get(Calendar.DAY_OF_WEEK) - 1];
                String month = CalendarWeatherApp.e_month_arr[displayMonthInt];


                String header1 = eDayStr+" "+day + "-" + month  +"( " + hinduMonth + "- " + localDate + "," + dayWord + " )";
                addFestival("" + header1, 1);


            }

            int festTmpCnt = 0;


            if (tithiCode.trim().startsWith("0") || tithiCode.trim().startsWith("0,") || tithiCode.trim().endsWith(",0,") || tithiCode.trim().endsWith(",0")) {

                festivalImages.add(R.drawable.ic_moon_00);
            }
            if (tithiCode.trim().contains("15")) {

                festivalImages.add(R.drawable.moon1);
            }
            if (tithiCode.trim().contains("11") && pakshacode.trim().contains("0")) {

                festivalImages.add(R.drawable.ic_moon_11);
            }
            if (tithiCode.trim().contains("11") && pakshacode.trim().contains("1")) {

                festivalImages.add(R.drawable.ic_moon_26);
            }
            if (is_sknt.trim().contains("1")) {

                festivalImages.add(R.drawable.ic_sankranti);
            }


            calendardata calobj = new calendardata();
            for (String myFestCode : festCodeArr) {

                int festCode = Integer.parseInt(myFestCode);
                if (festCode > 0) {


                    festTmpCnt = 1;
                    String festivalJsonStr = CalendarWeatherApp.l_festival_arr[festCode - 1];
                    JSONObject festReader = new JSONObject(festivalJsonStr);
                    String eFestival = festReader.getString("festival");
                    String lFestival = festReader.getString("festivalL");
                    String festivalIcon = festReader.getString("icon");


                    addFestival(eFestival, 2);
                    addFestival(lFestival, 3);

                    if (len_code_fest > 1) {
                        calobj.festivalIcon = calobj.festivalIcon + festivalIcon + Constant.separatorStr;
                        if (!festivalIcon.contains("NAME")) {
                            Integer id = getResources().getIdentifier(festivalIcon.toString(), "drawable", activity.getPackageName());
                            festivalImages.add(id);
                        }
                        calobj.eFestival = calobj.eFestival + eFestival + Constant.separatorStr;
                        calobj.festival = calobj.festival + lFestival + Constant.separatorStr;
                    } else {
                        calobj.festivalIcon = calobj.festivalIcon + festivalIcon;
                        if (!festivalIcon.contains("NAME")) {
                            Integer id = getResources().getIdentifier(festivalIcon.toString(), "drawable", activity.getPackageName());
                            festivalImages.add(id);
                        }
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


            return localDate;//+ ":" + tithi + ":" + is_sknt + ":" + or_fc;
        } catch (Exception e) {

            System.out.println("localDate:" + e.getMessage());
        }
        System.out.println("localDate:::;");
        return "-";
    }

    public static class calendardata {
        String eFestival, festival, festivalIcon;
    }

    private String[] getDayValJson(Calendar cal1, int type) {
        int maxDays, dayYear;
        Boolean isLeapYear;
        int dayOfMonth = cal1.get(Calendar.DAY_OF_MONTH);
        cal1.set(Calendar.DAY_OF_MONTH, 1);
        GregorianCalendar cal = new GregorianCalendar();
        maxDays = cal1.getActualMaximum(Calendar.DAY_OF_MONTH);
        isLeapYear = cal.isLeapYear(cal1.get(Calendar.YEAR));
        dayYear = cal1.get(Calendar.DAY_OF_YEAR) - 1;
        cal1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dayVal = new String[maxDays];
        String day_convert;
        if (isLeapYear) {
            day_convert = getResources().getString(R.string.day_convert_leap);
        } else {
            day_convert = getResources().getString(R.string.day_convert);
        }
        //  String[] day_convertArr = day_convert.split(Constant.separatorStr);
        String[] day_convertArr = day_convert.split(",");
        // String fccode = mPref.getMyLanguage() + "_fc";
        String fccode = "en_fc";
        for (int i = 0; i < maxDays; i++) {
            String lDay = day_convertArr[dayYear + i].trim();
            dayVal[i] = "{\"" + fccode + "\":\"0\",\"dt\":\"" + (i + 1) + "\",\"ldt\":\"" + lDay + "\",\"is_sknt\":\"0\",\"tithi\":\"99\",\"moonsign\":\"99\",\"sunsign\":\"99\",\"paksha\":\"99\",\"nakshetra\":\"99\",\"lmonth\":\"99\"}";
        }
        return dayVal;
    }

    private void addTitle() {
        for (int i = 0; i < 7; i++) {
            TextView tv = new TextView(activity);
            tv.setLayoutParams(titleParam);
            String day = "";
            switch (i) {
                case 0:
                    day = "SUN";
                    break;
                case 1:
                    day = "MON";
                    break;
                case 2:
                    day = "TUE";
                    break;
                case 3:
                    day = "WED";
                    break;
                case 4:
                    day = "THU";
                    break;
                case 5:
                    day = "FRI";
                    break;
                case 6:
                    day = "SAT";
                    break;


            }
            tv.setText(day);
            tv.setBackground(getResources().getDrawable(R.drawable.border));
            tv.setGravity(Gravity.CENTER);
            daysContainer.addView(tv);
        }

    }

    private void addDayTitle(int index, String day) {
        TextView tv = new TextView(activity);
        tv.setBackground(getResources().getDrawable(R.drawable.border));
        tv.setLayoutParams(titleParam);
        tv.setText(day);

        if (index < 7) {
            week1Container.addView(tv);
        } else if (index < 14) {
            week2Container.addView(tv);
        } else if (index < 21) {
            week3Container.addView(tv);
        } else if (index < 28) {
            week4Container.addView(tv);

        } else if (index < 35) {
            week5Container.addView(tv);

        } else if (index < 42) {
            week6Container.addView(tv);

        }

    }

    public FrameLayout getLayout() {
        FrameLayout fl = new FrameLayout(activity);
        fl.setLayoutParams(param);
        fl.setBackground(getResources().getDrawable(R.drawable.border));
        return fl;

    }

    protected void setUp(View view) {


    }
}
