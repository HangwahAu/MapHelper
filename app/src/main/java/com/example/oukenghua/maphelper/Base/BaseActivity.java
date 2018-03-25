package com.example.oukenghua.maphelper.Base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不要默认的标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置布局文件
        setContentView(getLayoutID());
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
    }
}
