package com.iexamcenter.odiacalendar.news;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iexamcenter.odiacalendar.R;
import com.iexamcenter.odiacalendar.omg.MantraFragment;

import java.util.ArrayList;

public class YoutubeListAdapter extends RecyclerView.Adapter<YoutubeListAdapter.ItemViewHolder> {
    Context mContext;
    ArrayList<MantraFragment.YoutubeList> mItems;

    public YoutubeListAdapter(Context context, ArrayList<MantraFragment.YoutubeList> pglist) {

        mContext = context;
        mItems = pglist;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutRes;

        layoutRes = R.layout.inflate_videoitem;

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);

        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final MantraFragment.YoutubeList obj = mItems.get(position);
        holder.tvTitle.setText((position + 1) + ")" + obj.lTitle);
        holder.youtube.setTag(obj);
        String IMAGE_URL = "https://i1.ytimg.com/vi/" + obj.vKey + "/hqdefault.jpg";
        Glide.with(mContext).load(IMAGE_URL).into(holder.youTubeThumbnailView);
        // https://i1.ytimg.com/vi/2R8EwjM3WWY/hqdefault.jpg
      //  holder.youTubeThumbnailView.setTag(obj);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public ImageView youTubeThumbnailView;
        public CardView youtube;
        public ImageView playButton;
        public RelativeLayout relativeLayoutOverYouTubeThumbnailView;


        public ItemViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.title1);
            youTubeThumbnailView = (ImageView) view.findViewById(R.id.youtube_thumbnail);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) view.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youtube = (CardView) view.findViewById(R.id.youtube);
            playButton = (ImageView) view.findViewById(R.id.btnYoutube_player);

        }


    }
}
