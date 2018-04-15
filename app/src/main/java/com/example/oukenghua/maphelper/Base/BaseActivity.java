package com.example.oukenghua.maphelper.Base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.BarUtils;

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
        //加载布局后执行该方法初始化类
        init(savedInstanceState);
        //加载数据
        loadData();
    }

    public abstract int getLayoutID();

    public abstract void init(Bundle savedInstanceState);

    public abstract void loadData();

    /**
     * 顶替系统状态栏达到沉浸式效果
     *
     * @param topBar
     */
    protected void takeLinearStatusBar(View topBar) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                topBar.getLayoutParams();
        layoutParams.height = BarUtils.getStatusBarHeight();
        topBar.setLayoutParams(layoutParams);
    }

    /**
     * 顶替系统状态栏达到沉浸式效果
     *
     * @param topBar
     */
    protected void takeRelativeStatusBar(View topBar) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                topBar.getLayoutParams();
        layoutParams.height = BarUtils.getStatusBarHeight();
        topBar.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }
}
