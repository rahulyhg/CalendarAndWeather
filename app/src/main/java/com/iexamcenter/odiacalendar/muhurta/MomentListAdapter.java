package com.iexamcenter.odiacalendar.muhurta;

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

public class MomentListAdapter extends RecyclerView.Adapter<MomentListAdapter.ItemViewHolder> {
    Resources res;
    Context mContext;
    ArrayList<MomentFragment.Moment> mItems;
    int mType;
    String[] monthArr;
    String[] baraArr;
    private int lastPosition = -1;
    PrefManager mPref;

    public MomentListAdapter(Context context, ArrayList<MomentFragment.Moment> pglist) {
        res = context.getResources();
        mContext = context;
        mItems = pglist;
        mPref = PrefManager.getInstance(mContext);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_momentlist;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final MomentFragment.Moment obj = mItems.get(position);
        String timeDesc = "";
        if (obj.type == 0) {
            timeDesc = "Auspicious";
            holder.title1.setTextColor(mContext.getResources().getColor(R.color.auspicious));
        } else if (obj.type == 1) {

            timeDesc = "Inauspicious";
            holder.title1.setTextColor(mContext.getResources().getColor(R.color.inauspicious));

        } else if (obj.type == 2) {
            holder.title1.setTextColor(mContext.getResources().getColor(R.color.veryauspicious));
            timeDesc = "Very auspicious";

        }
        holder.title1.setText((position + 1) + ":-  " + timeDesc + " time (" + obj.eTime + ")");
        //  holder.title2.setText(obj.name+"("+obj.lName+")");
        holder.title3.setText(obj.eName);
        if (mPref.getMyLanguage().contains("en")) {
            holder.title2.setText(obj.lName);
        } else {
            CalendarWeatherApp.getInstance().setTextView(holder.title2, obj.name + "(" + obj.lName + ")", 1);

        }
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
    public void onViewDetachedFromWindow(MomentListAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.container.clearAnimation();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title1, title2, title3;

        public LinearLayout container;

        public ItemViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);

            title1 = (TextView) itemView.findViewById(R.id.date);
            title2 = (TextView) itemView.findViewById(R.id.festL);
            title3 = (TextView) itemView.findViewById(R.id.festE);

        }


    }
}
