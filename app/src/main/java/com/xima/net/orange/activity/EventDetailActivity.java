package com.xima.net.orange.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xima.net.orange.R;
import com.xima.net.orange.bean.OrangeEvent;
import com.xima.net.orange.fragment.DatePickerFragment;
import com.xima.net.orange.utils.DateUtils;
import com.xima.net.orange.utils.LogUtils;
import com.xima.net.orange.utils.SharedPreferencesUtils;
import com.xima.net.orange.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.xima.net.orange.activity.MainActivity.REQUEST_CODE_PICK_PHOTO;
import static com.xima.net.orange.adapter.OrangeEventsAdapter.EVENT_JSON;
import static com.xima.net.orange.adapter.OrangeEventsAdapter.EVENT_POSITION;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_ANNIVERSARY;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_BIRTHDAY;
import static com.xima.net.orange.bean.OrangeEvent.TYPE_COUNTDOWN;

public class EventDetailActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, DatePickerFragment.OnDateSetListener {

    public static final String PHOTO_URI = "photo_uri";
    public static final String EVENT_ACTION = "action_event";
    public static final String EVENT_ACTION_ADD = "action_event_add";
    public static final String EVENT_ACTION_MODIFY = "action_event_MODIFY";

    private ImageButton mIbSave, mIbDelete;
    private ImageView mIvEventPic;
    private RadioGroup mRgType;
    private RadioButton mRbAnniversary, mRbBirthday, mRbCountdown;
    private Switch mSwitchTop;
    private LinearLayout mLLDate;
    private TextView mTvEventDate;
    private EditText mEtEventTitle;

    private List<OrangeEvent> mEvents;
    private DatePickerFragment datePickerFragment;

    private int eventPosition;
    private String eventAction = "";

    private String eventPhotoUri = "";
    private String eventTitle = "";
    private int eventType = 0;
    private String eventTime = "";
    private boolean eventIsTop = false;

    private int eventYear, eventMonth, eventDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBarTransparent();
        setContentView(R.layout.activity_event_detail);

        initView();
        initData();
        initEvent();

    }

    private void initView() {

        mIbDelete = findViewById(R.id.ib_delete);
        mIbSave = findViewById(R.id.ib_save);

        mIvEventPic = findViewById(R.id.iv_event_pic);

        mRgType = findViewById(R.id.rg_choose_type);
        mRbAnniversary = findViewById(R.id.rb_anniversary);
        mRbBirthday = findViewById(R.id.rb_birthday);
        mRbCountdown = findViewById(R.id.rb_countdown);

        mSwitchTop = findViewById(R.id.switch_isTop);

        mLLDate = findViewById(R.id.ll_date);
        mTvEventDate = findViewById(R.id.tv_event_date);

        mEtEventTitle = findViewById(R.id.et_event_title);

        datePickerFragment = new DatePickerFragment();
    }

    private void initData() {

        Intent intent = getIntent();
        eventAction = intent.getStringExtra(EVENT_ACTION);
        eventPosition = intent.getIntExtra(EVENT_POSITION, -1000);

        mEvents = SharedPreferencesUtils.getListFromSp(this, SharedPreferencesUtils.KEY_EVENTS, "");

        switch (eventAction) {
            case EVENT_ACTION_ADD:
                eventPhotoUri = intent.getStringExtra(PHOTO_URI);

                break;

            case EVENT_ACTION_MODIFY:

                mIbDelete.setVisibility(View.VISIBLE);

                Gson gson = new Gson();
                OrangeEvent orangeEvent = gson.fromJson(intent.getStringExtra(EVENT_JSON), new TypeToken<OrangeEvent>() {
                }.getType());

                eventPhotoUri = orangeEvent.getPicturePath();
                eventType = orangeEvent.getType();
                eventIsTop = orangeEvent.isTop();
                eventTime = orangeEvent.getStartTime();
                eventTitle = orangeEvent.getTitle();

                int[] day = DateUtils.getYearMonthDay(orangeEvent.getDate());
                eventYear = day[0];
                eventMonth = day[1];
                eventDay = day[2];

                mSwitchTop.setChecked(eventIsTop);
                mTvEventDate.setText(eventTime);
                mEtEventTitle.setText(eventTitle);

                break;

            default:
                break;
        }

        Glide.with(this).load(eventPhotoUri).into(mIvEventPic);

        switch (eventType) {
            case TYPE_ANNIVERSARY:
                mRgType.check(R.id.rb_anniversary);
                eventType = TYPE_ANNIVERSARY;
                break;
            case TYPE_BIRTHDAY:
                mRgType.check(R.id.rb_birthday);
                eventType = TYPE_BIRTHDAY;
                break;
            case TYPE_COUNTDOWN:
                mRgType.check(R.id.rb_countdown);
                eventType = TYPE_COUNTDOWN;
                break;
            default:
                break;
        }

    }

    private void initEvent() {

        //不弹出输入法
        mEtEventTitle.setSelection(eventTitle.length());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mIbDelete.setOnClickListener(this);
        mIbSave.setOnClickListener(this);
        mIvEventPic.setOnClickListener(this);
        mRgType.setOnCheckedChangeListener(this);
        mLLDate.setOnClickListener(this);
        datePickerFragment.setListener(this);

        mSwitchTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                eventIsTop = isChecked;
            }
        });
    }

    public void setBarTransparent() {
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

    /**
     * @return 返回一个获取指定image uri的信使
     */
    public static Intent newIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_delete:
                deleteEvent();
                break;

            case R.id.ib_save:
                saveEvent();
                break;

            case R.id.iv_event_pic:
                startActivityForResult(newIntent(), REQUEST_CODE_PICK_PHOTO);
                break;

            case R.id.ll_date:
                showDateDialog();

                break;

            default:
                break;
        }

    }

    private void showDateDialog() {
        datePickerFragment.show(getSupportFragmentManager(), "dialog");
//        //获取当前时间的年月日
//        int date[] = DateUtils.getYearMonthDay(new Date());
//        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, 0, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                eventYear = year;
//                eventMonth = month + 1;
//                eventDay = dayOfMonth;
//                eventTime = eventYear + "-" + eventMonth + "-" + eventDay;
//                mTvEventDate.setText(eventTime);
//            }
//        }, date[0], date[1], date[2]);
//
//        datePickerDialog.show();
    }

    private void deleteEvent() {
        if (mEvents.size() - 1 >= eventPosition) {
            mEvents.remove(eventPosition);
            saveEventDataToSP();
        } else {
            ToastUtils.error(this, "未知错误,请返回主界面重来~");
        }
    }

    private void saveEvent() {

        eventTitle = mEtEventTitle.getText().toString();

        if (!checkEventIsEmpty()) {

            eventTime = eventYear + "-" + eventMonth + "-" + eventDay;

            switch (eventAction) {
                case EVENT_ACTION_ADD:

                    if (mEvents == null)
                        mEvents = new ArrayList<>();

                    OrangeEvent orangeEvent = new OrangeEvent(eventTitle, eventType, eventPhotoUri, eventIsTop);
                    orangeEvent.setStartTime(eventTime);
                    orangeEvent.setDate(DateUtils.getDate(eventYear, eventMonth, eventDay));
                    mEvents.add(orangeEvent);

                    break;

                case EVENT_ACTION_MODIFY:
                    if (mEvents != null && mEvents.size() != 0 && eventPosition >= 0) {
                        OrangeEvent e = mEvents.get(eventPosition);

                        e.setPicturePath(eventPhotoUri);
                        e.setTitle(eventTitle);
                        e.setType(eventType);
                        e.setStartTime(eventTime);
                        e.setDate(DateUtils.getDate(eventYear, eventMonth, eventDay));
                        e.setTop(eventIsTop);
                    }

                    break;

                default:
                    break;
            }
            saveEventDataToSP();

        }
    }

    private void saveEventDataToSP() {
        SharedPreferencesUtils.saveStringToSP(getApplicationContext(), SharedPreferencesUtils.KEY_EVENTS, new Gson().toJson(mEvents));
        finish();
    }

    private boolean checkEventIsEmpty() {
        if (eventTitle.equals("") || eventTime.equals("") || eventType == 0) {
            ToastUtils.error(this, "请检查下是否填写完整呢!(事件,类型和日期都不能空白噢)");
            return true;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {

            case R.id.rb_anniversary:
                eventType = TYPE_ANNIVERSARY;
                break;
            case R.id.rb_birthday:
                eventType = TYPE_BIRTHDAY;
                break;
            case R.id.rb_countdown:
                eventType = TYPE_COUNTDOWN;
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_PHOTO)
            if (data != null) {
                eventPhotoUri = data.getDataString();
                Glide.with(this).load(eventPhotoUri).into(mIvEventPic);
            }
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        eventYear = year;
        eventMonth = month + 1;
        eventDay = dayOfMonth;
        LogUtils.i("month", eventMonth + "");
        eventTime = eventYear + "-" + eventMonth + "-" + eventDay;
        mTvEventDate.setText(eventTime);
    }
}
