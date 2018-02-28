package com.iexamcenter.odiacalendar.news;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.sqlite.SqliteHelper;
import com.iexamcenter.odiacalendar.utility.Connectivity;
import com.iexamcenter.odiacalendar.utility.Constant;
import com.iexamcenter.odiacalendar.utility.PrefManager;
import com.iexamcenter.odiacalendar.utility.Utility;

/**
 * Created by skyfishjy on 10/31/14.
 */
public class MyListCursorAdapter extends CursorRecyclerViewAdapter<MyListCursorAdapter.ViewHolder> {
    private Context mContext;

    PrefManager mPref;
    boolean isConnected = false, isMobileData = false;
    int colr;

    public MyListCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
        isMobileData = Utility.getInstance(mContext).isConnectedMobile();
        mPref = PrefManager.getInstance(context);
        mPref.load();
        isConnected = Connectivity.isConnected(context);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDomain;
        ImageView imageView;
        ImageView btnYoutube_player;
        CardView main;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.title);
            imageView = (ImageView) view.findViewById(R.id.image);
            main = (CardView) view.findViewById(R.id.main);
            tvDomain = (TextView) view.findViewById(R.id.domain);
            btnYoutube_player = (ImageView) view.findViewById(R.id.btnYoutube_player);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_newsitem, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    public static class mydata {
        int row_id;
        int news_type;
        String link, title, image, videoId;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, Cursor cursor) {

        colr = Utility.getInstance(mContext).getPageColors()[Constant.PAGE_UPDATES];

        String title = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_TITLE));
        String IMAGE_URL = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_IMAGE));
        String link = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_LINK));
        int row_id = cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry._ID));
        String category = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_CAT));
        int rssType = cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_TYPE));
        String pubTimeStamp = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_PUB_TIMESTAMP));
        String videoId = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_VIDEO_ID));
        String pubDate = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_PUB_DATE));

        String channel = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.NewsEntry.KEY_NEWS_CHANNEL));
        mydata md = new mydata();
        md.link = link;
        md.news_type = rssType;
        md.row_id = row_id;
        md.title = title;
        md.image = IMAGE_URL;
        md.videoId = videoId;

        if (mPref.isPullImg() && isMobileData  && !Connectivity.isConnectedFast(mContext)) {
            viewHolder.imageView.setBackgroundColor(colr);
           // viewHolder.imageView.setImageURI(null);
        } else {

            Log.e("DOWNLOAD","IMAGE_URL:"+IMAGE_URL);
            try {
                if (IMAGE_URL != null && !IMAGE_URL.isEmpty()) {
                    Glide.with(mContext)
                            .load(IMAGE_URL)
                            .into(viewHolder.imageView);

                } else {
                    viewHolder.imageView.setBackgroundColor(colr);
                    //  viewHolder.imageView.setDefaultImageResId(R.drawable.ic_no_photo);
                }
            } catch (Exception e) {
                viewHolder.imageView.setBackgroundColor(colr);
                // viewHolder.imageView.setDefaultImageResId(R.drawable.ic_no_photo);
            }

        }

        String timestamp;
        if (rssType == 1)
            timestamp = Utility.getInstance(mContext).getTimeAgo(Long.parseLong(pubTimeStamp) * 1000);
        else
            timestamp = Utility.getInstance(mContext).getMyDateTimeFormat(pubDate);

        // Populate fields with extracted properties

        viewHolder.itemView.setTag(md);
        viewHolder.tvTitle.setText(title);
        viewHolder.tvDomain.setText(category + ", " + channel + " " + timestamp);
        // viewHolder.tvDomain.setText(channel + "-" +pubDate+ "-" + timestamp);

        viewHolder.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.tvTitle.setMaxLines(2);
        if (rssType == 2) {
            viewHolder.btnYoutube_player.setVisibility(View.VISIBLE);
        } else {
            viewHolder.btnYoutube_player.setVisibility(View.GONE);

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (Connectivity.isConnected(mContext)) {
                mydata md = (mydata) v.getTag();
                   /* if (md.news_type == 1) {
                        Intent intent = new Intent(mContext, DetailsActivity.class);
                        intent.putExtra("NEWS_LINK", md.link);
                        mContext.startActivity(intent);
                    } else {

                        if (RssMediaPlayerActivity.active) {
                            ((RssMediaPlayerActivity) mContext).playVideo(md.videoId, md.title, md.title, true);
                        } else {

                            Intent intent = new Intent(mContext, RssMediaPlayerActivity.class);
                            intent.putExtra("ROW_ID", md.row_id);
                            intent.putExtra("VIDEO_ID", md.videoId);
                            intent.putExtra("CURR_PAGE", MainActivity.currPage);
                            intent.putExtra("TITLE", md.title);
                            mContext.startActivity(intent);
                        }

                    }*/


            }
        });


    }


}
