package com.iexamcenter.odiacalendar.thisday;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.iexamcenter.odiacalendar.MainActivity;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.quote.WebviewWhatTodayDialog;
import com.iexamcenter.odiacalendar.utility.Connectivity;
import com.iexamcenter.odiacalendar.utility.Utility;

import java.util.ArrayList;

public class OnThisDayAdapter extends RecyclerView.Adapter<OnThisDayAdapter.ItemViewHolder> {
    Resources res;
    MainActivity mContext;
    ArrayList<OnThisDayFragment.horoscopeItem> mItems;
    private int lastPosition = -1;
    int mPage=0;

    public OnThisDayAdapter(MainActivity context, ArrayList<OnThisDayFragment.horoscopeItem> pglist, int page) {
        res = context.getResources();
        mContext = context;
        mItems = pglist;

        mPage=page;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_whattodaylist;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final OnThisDayFragment.horoscopeItem obj = mItems.get(position);


        holder.title2.setText(obj.title);
        int drawable = R.drawable.ic_menu_event;
        holder.title1.setText(position + 1 + " : " + obj.date);

        holder.more.setTag(obj.wiki);
        holder.more.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && obj.wiki!=null && !obj.wiki.isEmpty()) {
            holder.more.setVisibility(View.VISIBLE);
        }

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = (String) view.getTag();


                if (!Connectivity.isConnected(mContext)) {
                    Utility.getInstance(mContext).newToast("Please switch on internet connection.");
                    return;
                }

                Bundle args = new Bundle();

                args.putString("url", url);
                args.putInt("page", mPage);


                FragmentTransaction ft0 = mContext.getFragmentManager().beginTransaction();
                Fragment prev0 = mContext.getFragmentManager().findFragmentByTag("webview1");
                if (prev0 != null) {
                    ft0.remove(prev0);
                }
                final DialogFragment webViewDialog = WebviewWhatTodayDialog.newInstance(mContext);
                webViewDialog.setCancelable(true);
                webViewDialog.setArguments(args);
                webViewDialog.show(ft0, "webview1");


            }
        });
        if (obj.type.contains("event"))
            drawable = R.drawable.ic_menu_event;
        else if (obj.type.contains("birth"))
            drawable = R.drawable.ic_menu_birthdays;
        else if (obj.type.contains("death"))
            drawable = R.drawable.ic_menu_deaths;
        else if (obj.type.contains("wed"))
            drawable = R.drawable.ic_menu_weeding;
        holder.title1.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);

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
    public void onViewDetachedFromWindow(OnThisDayAdapter.ItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.container.clearAnimation();

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title1, title2, more;

        public LinearLayout container;

        public ItemViewHolder(View itemView) {
            super(itemView);
            container = (LinearLayout) itemView.findViewById(R.id.container);

            title1 = (TextView) itemView.findViewById(R.id.title);
            title2 = (TextView) itemView.findViewById(R.id.desc);
            more = (TextView) itemView.findViewById(R.id.more);

        }


    }
}
