package com.example.oukenghua.maphelper.Activity;

import android.os.Bundle;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.map)
    MapView map;
    private AMap aMap;

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
        //创建地图
        ButterKnife.bind(this);
        map.onCreate(savedInstanceState);
        if (aMap == null)
            aMap = map.getMap();
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void onDestroy() {
        map.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //保存当前地图状态
        map.onSaveInstanceState(outState);
    }


}
