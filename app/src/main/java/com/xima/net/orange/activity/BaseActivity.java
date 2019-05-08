package com.xima.net.orange.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResID();

    protected abstract void initView();

    protected final String TAG = "tag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeSetContentView();
        setContentView(getLayoutResID());
        initView();
        initData();
        initEvent();
    }

    protected void initEvent() {
    }

    protected void initData() {
    }

    protected void beforeSetContentView() {
    }


}
