package com.xima.net.orange.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaychan.viewlib.NumberRunningTextView;
import com.xima.net.orange.R;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mFabAdd;
    private RecyclerView mRvEvent;
    private AppBarLayout mAppBarLayout;

    private TextView mTvTitle, mTvEvent, mTvStartTime;
    private NumberRunningTextView mTvDays;

    private ImageView mIvEventBg;
    private LinearLayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();//绑定视图
    }

    private void initView() {
        mFabAdd = findViewById(R.id.fab_add);
        mRvEvent = findViewById(R.id.rv_event);
        mAppBarLayout = findViewById(R.id.appbar_layout);

        mTvTitle = findViewById(R.id.tv_title);
        mTvStartTime = findViewById(R.id.tv_start_time);
        mTvEvent = findViewById(R.id.tv_event);
        mTvDays = findViewById(R.id.tv_days);

        mIvEventBg = findViewById(R.id.iv_bg);

        mLayoutManager = new LinearLayoutManager(this);
        mRvEvent.setLayoutManager(mLayoutManager);

    }
}
