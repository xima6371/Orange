package com.xima.net.orange.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;
import com.xima.net.orange.R;
import com.xima.net.orange.bean.Event;
import com.xima.net.orange.fragment.DatePickerFragment;
import com.xima.net.orange.listener.OnDateSetListener;
import com.xima.net.orange.utils.Constant;
import com.xima.net.orange.utils.DateUtils;
import com.xima.net.orange.utils.ToastUtils;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.xima.net.orange.utils.Constant.EVENT_ACTION_ADD;
import static com.xima.net.orange.utils.Constant.EVENT_ACTION_MODIFY;
import static com.xima.net.orange.utils.Constant.REQUEST_CODE_PICK_PHOTO;
import static com.xima.net.orange.utils.Constant.REQUEST_CODE_WRITE_PERMISSIONS;

public class EventDetailActivity extends BaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener, OnDateSetListener {

    private ImageButton mIbBack, mIbDelete, mIbPhoto, mIbSave;
    private ImageView mIvPhoto;
    private RadioGroup mRgType;
    private Switch mSwitchTop;
    private TextView mTvTime;
    private EditText mEtTitle;

    private LinearLayout mLLDate;

    private DatePickerFragment mDatePicker;

    private String action;
    private String path;
    private String title;
    private String time;
    private Date date;

    private boolean top = false;
    private boolean dateChanged = false;

    private int type = 0;
    private int eventYear, eventMonth, eventDay;

    private long hash = -1;
    private long lastHash = -1;

    @Override
    protected void beforeSetContentView() {
        super.beforeSetContentView();
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_event_detail;
    }

    @Override
    protected void initView() {
        mIbBack = findViewById(R.id.ib_back);
        mIbDelete = findViewById(R.id.ib_delete);
        mIbSave = findViewById(R.id.ib_save);
        mIbPhoto = findViewById(R.id.ib_photo);
        mIvPhoto = findViewById(R.id.iv_photo);

        mRgType = findViewById(R.id.rg_choose_type);

        mSwitchTop = findViewById(R.id.switch_isTop);
        mLLDate = findViewById(R.id.ll_date);
        mTvTime = findViewById(R.id.tv_event_time);
        mEtTitle = findViewById(R.id.et_event_title);

        mDatePicker = new DatePickerFragment();
    }

    @Override
    protected void initData() {

        Intent intent = getIntent();
        if (intent != null) {
            action = intent.getStringExtra(Constant.EVENT_ACTION);
            hash = intent.getLongExtra(Constant.EVENT_ACTION_MODIFY, -1);
            lastHash = intent.getLongExtra(Constant.EVENT_LAST_TOP, -1);

            switch (action) {
                case EVENT_ACTION_ADD:
                    //配置当前年月日
                    getCurDate();

                    break;

                case EVENT_ACTION_MODIFY:
                    List<Event> events = LitePal.where("hash = ?", String.valueOf(hash))
                            .find(Event.class);
                    Event event = events.get(0);
                    if (hash == -1) {
                        ToastUtils.error(this, "未能从数据库中找到该事件");
                    } else {
                        path = event.getPath();
                        type = event.getType();
                        title = event.getTitle();
                        top = event.isTop();

                        mIbDelete.setVisibility(View.VISIBLE);
                        mSwitchTop.setChecked(top);
                        mTvTime.setText(event.getDateDes());
                        mEtTitle.setText(title);

                        switch (type) {
                            case Constant.EVENT_TYPE_ANNIVERSARY:
                                mRgType.check(R.id.rb_anniversary);
                                break;
                            case Constant.EVENT_TYPE_BIRTHDAY:
                                mRgType.check(R.id.rb_birthday);
                                break;
                            case Constant.EVENT_TYPE_COUNTDOWN:
                                mRgType.check(R.id.rb_countdown);
                                break;
                            default:
                                break;
                        }
                    }

                    break;

                default:
                    break;
            }

            Glide.with(this).load(path).into(mIvPhoto);

        } else {
            ToastUtils.error(this, "未知错误..请重启应用");
        }

    }

    @Override
    protected void initEvent() {

        //不弹出输入法
        if (title != null) {
            mEtTitle.setSelection(title.length());
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mIbBack.setOnClickListener(this);
        mIbDelete.setOnClickListener(this);
        mIbPhoto.setOnClickListener(this);
        mIbSave.setOnClickListener(this);

        mIvPhoto.setOnClickListener(this);
        mRgType.setOnCheckedChangeListener(this);
        mLLDate.setOnClickListener(this);
        mDatePicker.setListener(this);

        mSwitchTop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                top = isChecked;
            }
        });
    }

    private void getCurDate() {
        int[] ymd = DateUtils.getYMD();
        initYMD(ymd);
    }

    //初始化年月日,并在TextView中显示
    private void initYMD(int[] ymd) {
        eventYear = ymd[0];
        eventMonth = ymd[1];
        eventDay = ymd[2];
        date = new Date();
        time = eventYear + "-" + (eventMonth + 1) + "-" + eventDay;//month准确显示为1-12月
        mTvTime.setText(time);
    }

    /**
     * @return 返回一个获取指定image uri的信使
     */
    private static Intent newIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        return intent;
    }

    private void selectPhoto() {
        if (ContextCompat.checkSelfPermission(EventDetailActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(EventDetailActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_PERMISSIONS);
        } else {
            startActivityForResult(EventDetailActivity.newIntent(), REQUEST_CODE_PICK_PHOTO);
        }
    }

    private void showDateDialog() {
        mDatePicker.show(getSupportFragmentManager(), "dialog");
    }

    private void deleteEvent() {
        if (hash != -1) {
            LitePal.delete(Event.class, hash);
        }
        exit();
    }

    private void exit() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void save() {
        //获取标题
        title = mEtTitle.getText().toString();

        if (!isEventEmpty()) {
            //若此事件顶置,则将上次的顶置事件取消顶置
            resetTopEvent();

            switch (action) {
                case EVENT_ACTION_ADD:
                    saveEventAdd();

                    break;

                case EVENT_ACTION_MODIFY:
                    saveEventModify();

                    break;

                default:
                    break;
            }
            exit();
        }
    }

    private void saveEventModify() {
        Event event = new Event();
        event.setPath(path);
        event.setType(type);
        event.setTitle(title);
        event.setTop(top);
        if (dateChanged) {
            event.setDate(date);
        }
        event.updateAll("hash = ?", String.valueOf(hash));
    }

    private void saveEventAdd() {
        Event event = new Event();
        event.setPath(path);
        event.setType(type);
        event.setTitle(title);
        event.setTop(top);
        event.setDate(date);
        event.setHash(System.currentTimeMillis());
        event.save();
    }

    private void resetTopEvent() {
        if (top) {
            if (lastHash != -1 && lastHash != hash) {
                Event e = new Event();
                e.setToDefault("top");
                e.updateAll();
            }
        }
    }

    private boolean isEventEmpty() {
        //判断标题,类型,时间是否为空
        if (title.isEmpty() || type == 0) {
            ToastUtils.error(this, "请检查下是否填写完整呢!(事件,类型和日期都不能空白噢)");
            return true;
        }
        return false;
    }

    @Override
    public void onDateSet(int year, int month, int dayOfMonth) {
        dateChanged = true;
        //更新日期
        eventYear = year;
        eventMonth = month;
        eventDay = dayOfMonth;
        //在TextView中显示

        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        date = c.getTime();
        time = eventYear + "-" + (eventMonth + 1) + "-" + eventDay;
        mTvTime.setText(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ib_back:
                exit();
                break;

            case R.id.ib_delete:
                deleteEvent();
                break;

            case R.id.ib_photo:
                selectPhoto();
                break;

            case R.id.iv_photo:
                selectPhoto();
                break;

            case R.id.ib_save:
                save();
                break;

            case R.id.ll_date:
                showDateDialog();
                break;

            default:
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_anniversary:
                type = Constant.EVENT_TYPE_ANNIVERSARY;
                break;
            case R.id.rb_birthday:
                type = Constant.EVENT_TYPE_BIRTHDAY;
                break;
            case R.id.rb_countdown:
                type = Constant.EVENT_TYPE_COUNTDOWN;
                break;

            default:
                break;
        }
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
        if (requestCode == REQUEST_CODE_PICK_PHOTO)
            if (data != null) {
                String path = null;
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        }
                        cursor.close();
                    }

                    this.path = path;
                    Glide.with(this).load(this.path).into(mIvPhoto);

                } else {

                    ToastUtils.error(this, "获取图片出错");
                }
            }
    }


}
