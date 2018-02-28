package com.iexamcenter.odiacalendar.horoscope;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.iexamcenter.odiacalendar.CalendarWeatherApp;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.utility.PrefManager;
import com.iexamcenter.odiacalendar.utility.Utility;

import java.util.ArrayList;

public class HoroscopeListAdapter extends RecyclerView.Adapter<HoroscopeListAdapter.ItemViewHolder> {
    Resources res;
    Context mContext;
    ArrayList<HoroscopeFrag.horoscopeItem> mItems;
    String[] mrasi_kundali_arr, men_rasi_kundali_arr, mlrasi_kundali_arr;
    private int lastPosition = -1;
    PrefManager mPref;

    public HoroscopeListAdapter(Context context, ArrayList<HoroscopeFrag.horoscopeItem> pglist, String[] rasi_kundali_arr, String[] en_rasi_kundali_arr, String[] lrasi_kundali_arr) {
        res = context.getResources();
        mContext = context;
        mItems = pglist;
        mrasi_kundali_arr = rasi_kundali_arr;
        men_rasi_kundali_arr = en_rasi_kundali_arr;
        mlrasi_kundali_arr = lrasi_kundali_arr;
        mPref=PrefManager.getInstance(mContext);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_rashiphalalist;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    public int getDrawable(int position) {

        switch (position) {
            case 0:
                return R.drawable.ic_hsaries;
            case 1:
                return R.drawable.ic_hstaurus;

            case 2:
                return R.drawable.ic_hsgemini;

            case 3:
                return R.drawable.ic_hscancer;

            case 4:
                return R.drawable.ic_hsleo;

            case 5:
                return R.drawable.ic_hsvirgo;

            case 6:
                return R.drawable.ic_hslibra;

            case 7:
                return R.drawable.ic_hsscorpio;

            case 8:
                return R.drawable.ic_hssagittarius;

            case 9:
                return R.drawable.ic_hscapricorn;
            case 10:
                return R.drawable.ic_hsaquarius;
            case 11:
                return R.drawable.ic_hspisces;

            default:
                return R.drawable.ic_horoscope;

        }
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final HoroscopeFrag.horoscopeItem obj = mItems.get(position);
        String rashi = mrasi_kundali_arr[position];
        String lrashi = mlrasi_kundali_arr[position];
        String eRashi = men_rasi_kundali_arr[position];

        if (mPref.getMyLanguage().contains("en")) {
            holder.title1.setText(rashi + ", "+obj.title);
        }else {
            CalendarWeatherApp.getInstance().setTextView(holder.title1,  rashi + "(" + lrashi + "), "+obj.title , 1);

        }

      //  holder.title1.setCompoundDrawablesWithIntrinsicBounds(getDrawable(position), 0, 0, 0);
        holder.title2.setText(obj.desc);
        lastPosition = Utility.getInstance(mContext).setAnimation(holder.container, position, lastPosition);
        int px = (int) Utility.getInstance(mContext).dpToPx(0);
        //  int px82= (int)Utility.getInstance(mContext).dpToPx(20);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if (position == (mItems.size() - 1)) {
            int px1 = (int) Utility.getInstance(mContext).dpToPx(80);
            buttonLayoutParams.setMargins(px, px, px, px1);
            holder.container.setLayoutParams(buttonLayoutParams);
        } else {
            buttonLayoutParams.setMargins(px, px, px, px);
            holder.container.setLayoutParams(buttonLayoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onViewDetachedFromWindow(HoroscopeListAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.container.clearAnimation();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title1, title2;

        public LinearLayout container;

        public ItemViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);

            title1 = (TextView) itemView.findViewById(R.id.title);
            title2 = (TextView) itemView.findViewById(R.id.desc);

        }


    }
}
