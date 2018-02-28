package com.iexamcenter.odiacalendar.muhurta;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.utility.PrefManager;
import com.iexamcenter.odiacalendar.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MomentFragment extends Fragment {
    TextView txt1;
    //ArrayList<TodayFragment.todayData> tdList = new ArrayList<>();

    PrefManager mPref;
    String lang;
    Resources res;
    ArrayList<Moment> hl;
    String[] hrmin;


    static private MainActivity mContext;
    public static MomentFragment newInstance() {

        return new MomentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (MainActivity) getActivity();
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        try {
            Bundle arg = getArguments();

            Integer[] colors = Utility.getInstance(mContext).getPageColors();
            rootView.setBackgroundColor(colors[arg.getInt("position")]);


            mPref = PrefManager.getInstance(mContext);
            lang = mPref.getMyLanguage();
            txt1 = (TextView) rootView.findViewById(R.id.txt1);
            res = mContext.getResources();
            hl = new ArrayList<>();
            Calendar cal1 = Calendar.getInstance();
            long currSec = cal1.getTimeInMillis();
            hrmin = new String[2];
            TextView my_location=rootView.findViewById(R.id.my_location);
            my_location.setVisibility(View.VISIBLE);

            if (mPref.getLatitude() != null && mPref.getLatitude() != null) {
            /*     String strLoc="All Muhurta time calculated based on your location set in Weather & Sun Moon page: "+mPref.getAreaAdmin();
                my_location.setText(strLoc);
                my_location.setSelected(true);
                String timeZoneStr = TimeZone.getDefault().getID();
                double lat = Double.parseDouble(mPref.getLatitude());
                double lng = Double.parseDouble(mPref.getLongitude());
                double obsLon = lng * SunMoonCalculator.DEG_TO_RAD, obsLat = lat * SunMoonCalculator.DEG_TO_RAD;
                String sunRise;
                SunMoonCalculator smc = null;
                try {
                    smc = new SunMoonCalculator(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH) + 1, cal1.get(Calendar.DAY_OF_MONTH), 12, 0, 0, obsLon, obsLat);
                    smc.calcSunAndMoon();
                    sunRise = Utility.getInstance(mContext).getDateCurrentTimeZone(timeZoneStr, SunMoonCalculator.getDateAsString(smc.sunRise));
                    hrmin = sunRise.split(" ", 2)[0].split(":");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            */} else {
                hrmin[0] = "6";
                hrmin[1] = "0";
                String strLoc="Seems you have not set your current place yet. We are assuming Sunrise time is 6:00 am";
                my_location.setText(strLoc);
                my_location.setSelected(true);


            }
            String[] l_muhurtas_arr = res.getStringArray(R.array.l_muhurtas_arr);
            int pos = 0;
            for (int i = 0; i < 30; i++) {
                Calendar cal = Calendar.getInstance();
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), Integer.parseInt(hrmin[0]), Integer.parseInt(hrmin[1]));
                int dayW = cal.get(Calendar.DAY_OF_WEEK);
                cal.add(Calendar.MINUTE, i * 48);
                int h1 = cal.get(Calendar.HOUR_OF_DAY);
                int m1 = cal.get(Calendar.MINUTE);
                cal.add(Calendar.MINUTE, 48);
                int h2 = cal.get(Calendar.HOUR_OF_DAY);
                int m2 = cal.get(Calendar.MINUTE);

                String jsonStr = l_muhurtas_arr[i];


                JSONObject festReader = null;
                try {
                    festReader = new JSONObject(jsonStr);
                    int type = festReader.getInt("type");
                    String name = festReader.getString("name");
                    String lname = festReader.getString("lname");
                    String ename = festReader.getString("ename");

                    Moment m = new Moment();
                    m.type = type;
                    if ((dayW == 2 || dayW == 6) && i == 7) {
                        m.type = 0;
                    } else if (i == 7) {
                        m.type = 1;
                    }
                    if (dayW == 1 && i == 13) {
                        m.type = 0;
                    } else if (i == 6) {
                        m.type = 1;
                    }
                    m.name = name;
                    m.eName = ename;
                    m.lName = lname;
                    String H1, H2, M1, M2;
                    if (h1 < 10)
                        H1 = "0" + h1;
                    else
                        H1 = "" + h1;
                    if (h2 < 10)
                        H2 = "0" + h2;
                    else
                        H2 = "" + h2;
                    if (m1 < 10)
                        M1 = "0" + m1;
                    else
                        M1 = "" + m1;
                    if (m2 < 10)
                        M2 = "0" + m2;
                    else
                        M2 = "" + m2;
                    m.eTime = H1 + ":" + M1 + "-" + H2 + ":" + M2;


                    Calendar cal3 = Calendar.getInstance();
                    cal3.set(cal3.get(Calendar.YEAR), cal3.get(Calendar.MONTH), cal3.get(Calendar.DAY_OF_MONTH), h1, m1);
                    long secStart = cal3.getTimeInMillis();
                    cal3.set(cal3.get(Calendar.YEAR), cal3.get(Calendar.MONTH), cal3.get(Calendar.DAY_OF_MONTH), h2, m2);
                    long secEnd = cal3.getTimeInMillis();


                    if (currSec <= secEnd && currSec >= secStart) {
                        System.out.println("currHrcurrHr:XXXXX:" + m.lName + ":XX:" + m.type + ":XX:" + m.eTime);
                        pos = i;
                        // break;
                        // hl.add(m);
                    }

                    hl.add(m);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            RecyclerView momentListView = (RecyclerView) rootView.findViewById(R.id.holidayList);
            MomentListAdapter mMomentListAdapter = new MomentListAdapter(mContext, hl);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            momentListView.setLayoutManager(llm);
            momentListView.setHasFixedSize(false);
            momentListView.setAdapter(mMomentListAdapter);
            momentListView.scrollToPosition(pos);

        } catch (Exception e) {
            String errorFeedBack = "MOMENTFRAG-" + e.getMessage() ;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_moment, null);

        return rootView;

    }


    public static class Moment {
        public String name, eName, lName, eTime;
        public int type;
    }

}

