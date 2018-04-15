package com.xima.net.orange.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chaychan.viewlib.NumberRunningTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xima.net.orange.R;
import com.xima.net.orange.adapter.OrangeEventsAdapter;
import com.xima.net.orange.bean.OrangeEvent;
import com.xima.net.orange.utils.DateUtils;
import com.xima.net.orange.utils.SharedPreferencesUtils;
import com.xima.net.orange.utils.SwitchUtils;
import com.xima.net.orange.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.xima.net.orange.activity.EventDetailActivity.EVENT_ACTION;
import static com.xima.net.orange.activity.EventDetailActivity.EVENT_ACTION_ADD;
import static com.xima.net.orange.activity.EventDetailActivity.PHOTO_URI;
import static com.xima.net.orange.adapter.OrangeEventsAdapter.DEBUG_TAG;
import static com.xima.net.orange.adapter.OrangeEventsAdapter.EVENT_JSON;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton mFabAdd;
    private RecyclerView mRvEvent;
    private AppBarLayout mAppBarLayout;

    private TextView mTvTitle, mTvEvent, mTvStartTime,mTvDays;
    private LinearLayout mLLInfo;

    private ImageView mIvEventBg;

    private LinearLayoutManager mLayoutManager;


    private List<OrangeEvent> mEvents;
    private OrangeEvent topEvent;
    private OrangeEventsAdapter mAdapter;

    public static final int REQUEST_CODE_WRITE_PERMISSIONS = 1000;
    public static final int REQUEST_CODE_PICK_PHOTO = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarTransparent();
        setContentView(R.layout.activity_main);

        initView();//绑定视图
        initData();
        initEvent();
    }

    private void setBarTransparent() {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorTransparent, null));
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
        mAdapter.setEvents(mEvents);
        mAdapter.notifyDataSetChanged();
    }


    private void initView() {
        mFabAdd = findViewById(R.id.fab_add);
        mRvEvent = findViewById(R.id.rv_event);
        mAppBarLayout = findViewById(R.id.appbar_layout);

        mTvTitle = findViewById(R.id.tv_title);
        mTvStartTime = findViewById(R.id.tv_start_time);
        mTvEvent = findViewById(R.id.tv_event);
        mTvDays = findViewById(R.id.tv_days);
        mLLInfo = findViewById(R.id.ll_info);

        mIvEventBg = findViewById(R.id.iv_bg);

    }

    private void initData() {

        mEvents = getEventList();

        if (mEvents == null || mEvents.size() == 0) {
            topEventTips("快来添加你的事件吧");
            mEvents = new ArrayList<>();
        } else {

            for (OrangeEvent e : mEvents)
                if (e.isTop())
                    topEvent = e;


            if (topEvent != null) {
                //设置不同大小的text 比如1000天  1000是48sp,天是24sp;
                int days = DateUtils.getIntervals(topEvent.getDate());
                String dayText = days + " 天";
                AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(36);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(dayText);
                spannableStringBuilder.setSpan(sizeSpan,dayText.length()-2,dayText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                String[] eventMsg = SwitchUtils.getEventMsg(topEvent);

                mTvDays.setText(spannableStringBuilder);
                mTvEvent.setText(eventMsg[0]);

                Glide.with(this).load(topEvent.getPicturePath()).into(mIvEventBg);
                mTvTitle.setText(topEvent.getTitle());
                mTvStartTime.setText(topEvent.getStartTime());


                mTvStartTime.setVisibility(View.VISIBLE);
                mTvDays.setVisibility(View.VISIBLE);
            } else {
                topEventTips("快来添加你的顶置事件吧");
            }
        }
    }

    private List<OrangeEvent> getEventList() {
        String eventJson = SharedPreferencesUtils.getStringFromSP(getApplicationContext(), SharedPreferencesUtils.KEY_EVENTS, "");
        return new Gson().fromJson(eventJson, new TypeToken<List<OrangeEvent>>() {
        }.getType());
    }

    private void initEvent() {

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new OrangeEventsAdapter(this, mEvents);
        mRvEvent.setLayoutManager(mLayoutManager);
        mRvEvent.setAdapter(mAdapter);

        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_PERMISSIONS);
                } else {
                    startActivityForResult(EventDetailActivity.newIntent(), REQUEST_CODE_PICK_PHOTO);
                }
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                float infoAlpha = 1 - (Math.abs(verticalOffset) * 1.0f / scrollRange);
                mLLInfo.setAlpha(infoAlpha);
                mTvEvent.setAlpha(1 - infoAlpha);
            }
        });
    }

    private void topEventTips(String tips) {
        mTvTitle.setText(tips);
        mTvEvent.setText(tips);
        mTvStartTime.setVisibility(View.GONE);
        mTvDays.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(EventDetailActivity.newIntent(), REQUEST_CODE_PICK_PHOTO);
                } else {
                    ToastUtils.error(this, "您取消了获权,无法选取图片");
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_PICK_PHOTO:
                if (data != null) {
                    String photoUri = data.getDataString();
                    Intent intent = new Intent(this, EventDetailActivity.class);
                    intent.putExtra(EVENT_ACTION, EVENT_ACTION_ADD);
                    intent.putExtra(PHOTO_URI, photoUri);
                    startActivity(intent);

                }
                break;
        }
    }
}
