package com.iexamcenter.odiacalendar.utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.iexamcenter.odiacalendar.R;

import java.util.ArrayList;

public class MonthPopupListAdapter extends RecyclerView.Adapter<MonthPopupListAdapter.ItemViewHolder> {
    Context mContext;
    ArrayList<String> mItems;
    int mType;
   // PrefManager mPref;
MenuDialog md;
    int mres;
    public MonthPopupListAdapter(Context context, ArrayList<String> pglist, MenuDialog d, int res) {

        mContext = context;
        mItems = pglist;
        md=d;
        mres=res;
      //  mPref=PrefManager.getInstance(mContext);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = mres;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final String obj = mItems.get(position);
        //        holder.festL.setText(obj.festival);
        String[] tmp = obj.split(Constant.separatorStr, 2);
         if(tmp[0].contains("X")){
            tmp[0]="0";
        //    holder.title.setBackgroundResource(R.color.main);
        }else  if(tmp[0].contains("Y")){
             tmp[0]="500";
             //    holder.title.setBackgroundResource(R.color.main);
         }else{
          //  holder.title.setBackgroundResource(R.color.next_back);
        }
        holder.title.setTag(Integer.parseInt(tmp[0]));

        holder.title.setText(tmp[1]);
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int key=(int)view.getTag();

                md.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView title;


        public ItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);

        }


    }
}
