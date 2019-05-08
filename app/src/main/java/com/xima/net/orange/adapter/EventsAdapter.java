package com.xima.net.orange.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xima.net.orange.R;
import com.xima.net.orange.activity.EventDetailActivity;
import com.xima.net.orange.bean.Event;
import com.xima.net.orange.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * *                 (c) Copyright 2018/4/5 by xima
 * *                          All Rights Reserved
 */
public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Event> mEvents;

    public EventsAdapter(Context context) {
        this.mContext = context;
        this.mEvents = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == Constant.RECYCLER_TYPE_EMPTY) {
            view = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_empty, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.recycle_item_event, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventsAdapter.ViewHolder) {
            final Event event = mEvents.get(position);
            initData((ViewHolder) holder, event);
        }
    }

    public long getLastTop() {
        for (Event e : mEvents) {
            if (e.isTop())
                return e.getHash();
        }

        return -1;
    }

    @Override
    public int getItemCount() {
        if (mEvents == null || mEvents.size() == 0) {
            return 1;
        }
        return mEvents.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mEvents == null || mEvents.size() == 0) {
            return Constant.RECYCLER_TYPE_EMPTY;
        }
        return Constant.RECYCLER_TYPE_EVENT;
    }

    public void addAll(List<Event> events) {
        mEvents.clear();
        mEvents.addAll(events);
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    private void initData(@NonNull final ViewHolder holder, final Event event) {

        String dateDes;
        switch (event.getType()) {
            case Constant.EVENT_TYPE_ANNIVERSARY:
                dateDes = "纪念日:" + event.getDateDes();
                break;
            case Constant.EVENT_TYPE_BIRTHDAY:
                dateDes = "生日:" + event.getDateDes();
                break;
            case Constant.EVENT_TYPE_COUNTDOWN:
                dateDes = "倒数日:" + event.getDateDes();
                break;
            default:
                dateDes = "";
                break;
        }

        holder.mTvStartTime.setText(dateDes);
        holder.mTvEvent.setText(event.getEventDes(true));


        String path = event.getPath();
        if (path != null && !path.isEmpty() && !path.equals(Constant.EVENT_DEFAULT_PHOTO_PATH)) {
            Glide.with(mContext).load(path).into(holder.mIvPic);
            Glide.with(mContext).load(path).into(holder.mIvPicBg);
        }


        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra(Constant.EVENT_ACTION, Constant.EVENT_ACTION_MODIFY);

                //event的pos传入
                intent.putExtra(Constant.EVENT_ACTION_MODIFY, event.getHash());
                intent.putExtra(Constant.EVENT_LAST_TOP, getLastTop());

                mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());

            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private CardView mCardView;
        private ImageView mIvPicBg, mIvPic;
        private TextView mTvEvent, mTvStartTime;

        ViewHolder(View itemView) {
            super(itemView);

            mCardView = itemView.findViewById(R.id.cv_event);
            mIvPicBg = itemView.findViewById(R.id.iv_pic_bg);
            mIvPic = itemView.findViewById(R.id.iv_pic);
            mTvEvent = itemView.findViewById(R.id.tv_item_event);
            mTvStartTime = itemView.findViewById(R.id.tv_item_start_time);
        }
    }
}
