package com.iexamcenter.odiacalendar.utility;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.month.CalendarMainFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

/*
import com.stepone.venue.VenueListFragment;
*/


public class MenuDialog extends DialogFragment {

    private MainActivity mContext;
    static int mType;
    static ArrayList<String> mMonthPopupArr;
    static float mY;
    Dialog dialog;
    static int mCurrPage = 1;
    TextView title;
    String monthVal,yearVal;
    static CalendarMainFragment mcalendarMainFragment;
    public static MenuDialog newInstance(ArrayList<String> monthPopupArr, int type, float y, int currPage, CalendarMainFragment calendarMainFragment) {
        MenuDialog f = new MenuDialog();
        mMonthPopupArr = monthPopupArr;
        mcalendarMainFragment=calendarMainFragment;
        mType = type;
        mY = y;
        mCurrPage = currPage;
        return f;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = (MainActivity) getActivity();
        LayoutInflater inflater = mContext.getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_menu, null);

        String[] displayValues1 = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


        LinearLayout nextPrevCont = (LinearLayout) root.findViewById(R.id.nextPrevCont);
        Collections.reverse(mMonthPopupArr);
        title= root.findViewById(R.id.title);

        int mPageColor = Utility.getInstance(mContext).getPageColors()[mCurrPage];
        nextPrevCont.setBackgroundColor(mPageColor);
        // String[] displayValues1=new String[100];
        Calendar cal = Calendar.getInstance();
        int currMonth = cal.get(Calendar.MONTH);
        int currYear = cal.get(Calendar.YEAR);
        int min = currYear - 100;
        int max = currYear + 100;
        String[] displayValYear = new String[201];
        monthVal=displayValues1[currMonth];
        yearVal=""+currYear;
        title.setText(yearVal+"/"+monthVal);
        final HashMap<Integer, Integer> hmy = new HashMap<>();
        if (mType == 1) {
            for (int i = max, j = 0; i >= min; i--) {
                displayValYear[j] = "" + i;
                hmy.put(j + min, i);
                j++;
            }
        } else {
            for (int i = min, j = 0; i <= max; i++) {
                displayValYear[j] = "" + i;
                hmy.put(j + min, i);
                j++;
            }
        }


        final NumberPicker yearPicker = (NumberPicker) root.findViewById(R.id.yearPicker);
        yearPicker.setMaxValue(max);
        yearPicker.setMinValue(min);
        yearPicker.setWrapSelectorWheel(true);
        yearPicker.setValue(currYear);
        yearPicker.setDisplayedValues(displayValYear);

        yearPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                 yearVal=""+newVal;

                title.setText(yearVal+"/"+monthVal);
            }
        });

        final NumberPicker monthPicker = (NumberPicker) root.findViewById(R.id.monthPicker);
        monthPicker.setMaxValue(11);
        monthPicker.setMinValue(0);
        monthPicker.setWrapSelectorWheel(true);
        monthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                monthVal=displayValues1[newVal];
                title.setText(yearVal+"/"+monthVal);
            }
        });


            monthPicker.setDisplayedValues(displayValues1);
            monthPicker.setValue(currMonth);







        Button filter = (Button) root.findViewById(R.id.filter);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = yearPicker.getValue();
                year = hmy.get(year);

                int month = monthPicker.getValue();
                if (mType == 1) {
                    month = 11 - month;
                }

                int currYear = Calendar.getInstance().get(Calendar.YEAR);
                int currMonth = Calendar.getInstance().get(Calendar.MONTH);

                Calendar myCal = Calendar.getInstance();
                myCal.set(Calendar.YEAR, year);
                myCal.set(Calendar.MONTH, month);
                myCal.set(Calendar.DAY_OF_MONTH, 1);

                Calendar currCal1 = Calendar.getInstance();
                currCal1.set(Calendar.DAY_OF_MONTH, 1);
                int key = 0;
                //  int key=Integer.parseInt(""+((myCal1.getTimeInMillis() - myCal.getTimeInMillis())/(1000*60*60*24*30)));
                if (currCal1.getTimeInMillis() > myCal.getTimeInMillis()) {
                    if ((currMonth < month) && (currYear > year)) {
                        key = (Math.abs(year - currYear)) * 12 - Math.abs(month - currMonth);
                    } else {
                        key = Math.abs(year - currYear) * 12 + Math.abs(month - currMonth);
                    }
                    key = key * (-1);

                } else {
                    if ((currMonth > month) && (currYear < year)) {
                        key = (Math.abs(year - currYear)) * 12 - (Math.abs(month - currMonth));

                    } else {
                        key = Math.abs(year - currYear) * 12 + Math.abs(month - currMonth);
                    }


                }

                mcalendarMainFragment.changeCalendar(key);
                Toast.makeText(mContext,"KEY:"+key,Toast.LENGTH_LONG).show();

                MenuDialog.this.dismiss();
            }
        });

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();
       // int width = getResources().getDimensionPixelSize(R.dimen.dp100);
        dialog.setCanceledOnTouchOutside(true);
        try {
        //    wlp.width = width;
        } catch (Exception e) {
            e.printStackTrace();
        }
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //wlp.dimAmount = 0.5f;
        wlp.dimAmount = 0f;
        TypedValue tv = new TypedValue();
        // if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
       // wlp.x = 1;// getResources().getDimensionPixelSize(R.dimen.dp10);
      //  wlp.y = (int) mY;//getStatusBarHeight() + 2*getActionBarHeight();//getResources().getDimensionPixelSize(R.dimen.dp12);


            wlp.gravity = Gravity.CENTER ;

        window.setAttributes(wlp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(root);
        dialog.show();


        return dialog;

    }

}
