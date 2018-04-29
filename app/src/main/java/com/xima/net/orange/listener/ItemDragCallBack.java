package com.xima.net.orange.listener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.google.gson.Gson;
import com.xima.net.orange.adapter.OrangeEventsAdapter;
import com.xima.net.orange.bean.OrangeEvent;
import com.xima.net.orange.utils.LogUtils;
import com.xima.net.orange.utils.SharedPreferencesUtils;

import java.util.Collections;
import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;
import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.END;
import static android.support.v7.widget.helper.ItemTouchHelper.START;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

public class ItemDragCallBack extends ItemTouchHelper.Callback {
    private Context mContext;
    private RecyclerView mRecyclerView;

    public ItemDragCallBack(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
    }


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        return makeMovementFlags(DOWN | UP, END | START);

    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        LogUtils.i("pos", fromPosition + ":" + toPosition);
        OrangeEventsAdapter adapter = (OrangeEventsAdapter) recyclerView.getAdapter();
        List<OrangeEvent> mEvents = adapter.getEvents();
//        Collections.swap(mEvent, fromPosition, toPosition);
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {

                Collections.swap(mEvents, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mEvents, i, i - 1);
            }
        }
        adapter.notifyItemMoved(fromPosition, toPosition);
        SharedPreferencesUtils.saveStringToSP(mContext, SharedPreferencesUtils.KEY_EVENTS, new Gson().toJson(mEvents));
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        OrangeEventsAdapter adapter = (OrangeEventsAdapter) mRecyclerView.getAdapter();
        List<OrangeEvent> mEvents = adapter.getEvents();
        mEvents.remove(viewHolder.getAdapterPosition());
        adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        SharedPreferencesUtils.saveStringToSP(mContext, SharedPreferencesUtils.KEY_EVENTS, new Gson().toJson(mEvents));
    }
}
