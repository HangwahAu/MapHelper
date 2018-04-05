package com.example.oukenghua.maphelper.Base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by oukenghua on 2018/3/24.
 */

/**
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder bind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不要默认的标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置布局文件
        setContentView(getLayoutID());
        //ButterKnife绑定
        bind = ButterKnife.bind(this);
        //沉浸式
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //加载布局后执行该方法初始化类
        init(savedInstanceState);
        //加载数据
        loadData();
    }

    public abstract int getLayoutID();

    public abstract void init(Bundle savedInstanceState);

    public abstract void loadData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
