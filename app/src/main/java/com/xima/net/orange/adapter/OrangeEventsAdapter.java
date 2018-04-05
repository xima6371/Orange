package com.xima.net.orange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xima.net.orange.R;
import com.xima.net.orange.bean.OrangeEvent;

import java.util.List;

/**
 * *                 (c) Copyright 2018/4/5 by xima
 * *                          All Rights Reserved
 */
public class OrangeEventsAdapter extends RecyclerView.Adapter<OrangeEventsAdapter.ViewHolder> {
    private Context mContext;
    private List<OrangeEvent> mEvents;

    public OrangeEventsAdapter(Context context, List<OrangeEvent> events) {
        mContext = context;
        mEvents = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(mContext).inflate(R.layout.recycleview_item, parent, false);

        return new ViewHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private ImageView mIvPicBg, mIvPic;
        private TextView mTvEvent, mTvStartTime;

        public ViewHolder(View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.cv_event);
            mIvPicBg = itemView.findViewById(R.id.iv_pic_bg);
            mIvPic = itemView.findViewById(R.id.iv_pic);
            mTvEvent = itemView.findViewById(R.id.tv_item_event);
            mTvStartTime = itemView.findViewById(R.id.tv_item_start_time);
        }
    }
}
