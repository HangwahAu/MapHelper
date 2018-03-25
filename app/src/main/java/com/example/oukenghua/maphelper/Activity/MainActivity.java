package com.example.oukenghua.maphelper.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.ToastUtil;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ToastUtil.setShortToast("欢迎使用");
    }

    @Override
    public void loadData() {

    }
}
