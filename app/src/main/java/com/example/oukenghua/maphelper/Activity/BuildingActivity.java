package com.example.oukenghua.maphelper.Activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.oukenghua.maphelper.Adapter.BuildingAdapter;
import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.LitePalDao.Building;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.Constant;
import com.example.oukenghua.maphelper.Utils.HttpClientUtils;
import com.example.oukenghua.maphelper.Utils.SharedPreferencesUtil;
import com.example.oukenghua.maphelper.Utils.SpaceItemDecoration;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BuildingActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.building_address)
    TextView buildingAddress;
    @BindView(R.id.recycler_building)
    RecyclerView recyclerBuilding;
    @BindView(R.id.my_status)
    View myStatus;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String serial_number,address = null;
    private List<Building> buildingList = new ArrayList<>();
    private BuildingAdapter buildingAdapter;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_building;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        takeLinearStatusBar(myStatus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
        if (SharedPreferencesUtil.getInstance().getString(Constant.USER_NAME) != null) {
            //判断当前是否为登录状态
            flag = true;
        }
        serial_number = getIntent().getStringExtra(Constant.SERIAL_NUMBER);  //获取父级的序列号
        address = getIntent().getStringExtra(Constant.ADDRESS);
        buildingAddress.setText(address);
        recyclerBuilding.setLayoutManager(new LinearLayoutManager(BuildingActivity.this));
        recyclerBuilding.addItemDecoration(new SpaceItemDecoration(40));
        buildingAdapter = new BuildingAdapter(R.layout.item_details_rv,buildingList,flag);
        recyclerBuilding.setAdapter(buildingAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(3000);
            }
        });
    }

    @Override
    public void loadData() {
        HttpClientUtils.getHttpUrl(Constant.SERVER_HTTP).getBuildingByParent(serial_number)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(building -> {

                    if(building)

                }, throwable -> {

                });
    }
}
