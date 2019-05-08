package com.xima.net.orange.helper;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.xima.net.orange.adapter.EventsAdapter;
import com.xima.net.orange.bean.Event;
import com.xima.net.orange.listener.OnSwipeListener;
import com.xima.net.orange.utils.Constant;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.LEFT;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

public class ItemDragHelper extends ItemTouchHelper.Callback {

    private RecyclerView mRecyclerView;
    private OnSwipeListener mSwipeListener;

    public ItemDragHelper(RecyclerView recyclerView) {

        mRecyclerView = recyclerView;
    }

    public void addListener(OnSwipeListener onSwipeListener) {
        this.mSwipeListener = onSwipeListener;
    }

    public void removeListener() {
        this.mSwipeListener = null;
        this.mRecyclerView = null;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder.getItemViewType() == Constant.RECYCLER_TYPE_EMPTY) {
            return makeMovementFlags(0, 0);
        } else {
            return makeMovementFlags(DOWN | UP, LEFT);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();

        EventsAdapter adapter = (EventsAdapter) recyclerView.getAdapter();
        List<Event> mEvents = adapter.getEvents();
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

        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        EventsAdapter adapter = (EventsAdapter) mRecyclerView.getAdapter();

        int pos = viewHolder.getAdapterPosition();
        List<Event> mEvents = adapter.getEvents();
        Event e = mEvents.get(pos);

        LitePal.deleteAll(Event.class, "hash = ?", String.valueOf(e.getHash()));

        mEvents.remove(pos);
        adapter.notifyItemRemoved(pos);

        if (mSwipeListener != null) {
            mSwipeListener.onSwiped(e.isTop());
        }
    }
}
