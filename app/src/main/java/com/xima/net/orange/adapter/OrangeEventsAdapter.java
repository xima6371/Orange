package com.xima.net.orange.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xima.net.orange.R;
import com.xima.net.orange.activity.EventDetailActivity;
import com.xima.net.orange.bean.OrangeEvent;
import com.xima.net.orange.utils.DateUtils;
import com.xima.net.orange.utils.SwitchUtils;

import java.util.List;

import static com.xima.net.orange.activity.EventDetailActivity.EVENT_ACTION;
import static com.xima.net.orange.activity.EventDetailActivity.EVENT_ACTION_MODIFY;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_ANNIVERSARY;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_BIRTHDAY;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_COUNTDOWN;

/**
 * *                 (c) Copyright 2018/4/5 by xima
 * *                          All Rights Reserved
 */
public class OrangeEventsAdapter extends RecyclerView.Adapter<OrangeEventsAdapter.ViewHolder> {
    public static final String DEBUG_TAG = "debug";
    private Context mContext;
    private List<OrangeEvent> mEvents;

    public static final String EVENT_JSON = "event_json";
    public static final String EVENT_POSITION = "event_position";

    public OrangeEventsAdapter(Context context, List<OrangeEvent> events) {
        mContext = context;
        mEvents = events;
    }

    public void setEvents(List<OrangeEvent> events) {
        mEvents = events;
    }

    public List<OrangeEvent> getEvents() {
        return mEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemVIew = LayoutInflater.from(mContext).inflate(R.layout.recycleview_item, parent, false);

        return new ViewHolder(itemVIew);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final OrangeEvent event = mEvents.get(position);
        initData(holder, event,position);
    }

    private void initData(@NonNull final ViewHolder holder, final OrangeEvent event, final int position) {

        String[] description = SwitchUtils.getEventMsg(event);

        String eventDescription = description[0];
        String dateDescription = description[1];
        String photoUri = event.getPicturePath();

        holder.mTvStartTime.setText(dateDescription);
        holder.mTvEvent.setText(eventDescription);

        RequestOptions options = new RequestOptions().error(R.drawable.nopic);
        Glide.with(mContext).load(photoUri).apply(options).into(holder.mIvPic);
        Glide.with(mContext).load(photoUri).apply(options).into(holder.mIvPicBg);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(EVENT_JSON, event.toString());
                intent.putExtra(EVENT_ACTION, EVENT_ACTION_MODIFY);
                intent.putExtra(EVENT_POSITION,position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, holder.mIvPic,"image").toBundle());
                }else {
                    mContext.startActivity(intent);
                }
            }
        });
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
