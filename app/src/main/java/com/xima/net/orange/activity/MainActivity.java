package com.xima.net.orange.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.xima.net.orange.R;
import com.xima.net.orange.adapter.EventsAdapter;
import com.xima.net.orange.bean.Event;
import com.xima.net.orange.helper.ItemDragHelper;
import com.xima.net.orange.helper.RecyclerDragHelper;
import com.xima.net.orange.listener.OnSwipeListener;
import com.xima.net.orange.utils.Constant;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class MainActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {

    private AppBarLayout mAppBarLayout;
    private LinearLayout mLLInfo;

    private TextView mTvTitle, mTvEventDes, mTvStartTime, mTvDays, mTvShowDays;
    private ImageView mIvTopBg;
    private FloatingActionButton mFabAdd;
    private RecyclerView mRecyclerView;

    private List<Event> mEvents;

    private EventsAdapter mAdapter;
    private RecyclerDragHelper mRecyclerDragHelper;

    private ItemDragHelper dragListener;

    @Override
    protected void beforeSetContentView() {
        super.beforeSetContentView();
        ImmersionBar.with(this)
                .init();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mLLInfo = findViewById(R.id.ll_info);
        mAppBarLayout = findViewById(R.id.appbar_layout);

        mTvTitle = findViewById(R.id.tv_title);
        mTvStartTime = findViewById(R.id.tv_start_time);
        mTvDays = findViewById(R.id.tv_days);
        mTvShowDays = findViewById(R.id.tv_show_day);
        mTvEventDes = findViewById(R.id.tv_event_des);
        mIvTopBg = findViewById(R.id.iv_top_bg);
        mFabAdd = findViewById(R.id.fab_add);
        mRecyclerView = findViewById(R.id.rv_event);
    }

    @Override
    protected void initData() {
        //配置RecyclerView的数据
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new EventsAdapter(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //开启数据库
        Connector.getDatabase();
        //异步查询所有数据
        LitePal.findAllAsync(Event.class).listen(new FindMultiCallback<Event>() {
            @Override
            public void onFinish(List<Event> list) {
                mAdapter.addAll(list);
                mAdapter.notifyItemRangeChanged(0, list.size());
                mEvents = list;
                initTopEvent();
            }
        });
    }

    @Override
    protected void initEvent() {
        //配置拖拽排序监听器
        dragListener = new ItemDragHelper(mRecyclerView);
        dragListener.addListener(new OnSwipeListener() {
            @Override
            public void onSwiped(boolean isTop) {
                if (isTop) {
                    mEvents = mAdapter.getEvents();
                    initTopEvent();
                }
            }
        });
        mRecyclerDragHelper = new RecyclerDragHelper(dragListener);
        mRecyclerDragHelper.attachToRecyclerView(mRecyclerView);

        //配置点击事件监听器
        mFabAdd.setOnClickListener(this);
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    private void initTopEvent() {

        if (mEvents == null || mEvents.size() == 0) {
            topEventTips("快来添加你的事件吧");
        } else {
            //获取顶置的事件
            Event topEvent = getTopEvent();
            if (topEvent != null) {

                //计时天数
                mTvDays.setText(topEvent.getIntervals());

                //事件描述,上滑时显示
                mTvEventDes.setText(topEvent.getEventDes(false));

                //加载图片
                String path = topEvent.getPath();

                if (path != null && !path.isEmpty() && !path.equals(Constant.EVENT_DEFAULT_PHOTO_PATH)) {
                    Glide.with(this).load(path).into(mIvTopBg);
                } else {
                    mIvTopBg.setImageBitmap(null);
                }

                mTvTitle.setText(topEvent.getTitle());
                mTvStartTime.setText(topEvent.getDateDes());

                mTvShowDays.setVisibility(View.VISIBLE);
                mTvStartTime.setVisibility(View.VISIBLE);
                mTvDays.setVisibility(View.VISIBLE);
            } else {
                topEventTips("快来添加你的顶置事件吧");
            }
        }

    }

    private Event getTopEvent() {
        for (Event e : mEvents)
            if (e.isTop())
                return e;

        return null;
    }

    private void topEventTips(String tips) {
        mTvTitle.setText(tips);
        mTvEventDes.setText(tips);
        mTvShowDays.setVisibility(View.GONE);
        mIvTopBg.setImageBitmap(null);
        mTvStartTime.setVisibility(View.GONE);
        mTvDays.setVisibility(View.GONE);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int scrollRange = appBarLayout.getTotalScrollRange();
        float infoAlpha = 1 - (Math.abs(verticalOffset) * 1.0f / scrollRange);
        mLLInfo.setAlpha(infoAlpha);
        mTvEventDes.setAlpha(1 - infoAlpha);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add) {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra(Constant.EVENT_ACTION, Constant.EVENT_ACTION_ADD);
            intent.putExtra(Constant.EVENT_LAST_TOP, mAdapter.getLastTop());
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAppBarLayout.removeOnOffsetChangedListener(this);
        dragListener.removeListener();
        mRecyclerDragHelper = null;
    }

}
