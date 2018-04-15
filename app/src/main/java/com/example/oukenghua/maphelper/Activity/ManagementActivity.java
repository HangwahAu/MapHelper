package com.example.oukenghua.maphelper.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.Constant;
import com.example.oukenghua.maphelper.Utils.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ManagementActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_management;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public void loadData() {

    }

    @OnClick({R.id.btn_search, R.id.btn_add, R.id.btn_exit})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_search:
                intent.setClass(ManagementActivity.this,CommunityActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_add:
                intent.setClass(ManagementActivity.this,AddAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_exit:
                AlertDialog dialog = new AlertDialog.Builder(ManagementActivity.this)
                        .setTitle("是否确认退出当前登录？")
                        .setPositiveButton("确定", (dialogInterface, i) -> {
                            finish();
                            SharedPreferencesUtil.getInstance().remove(Constant.USER_NAME);
                        }).setNegativeButton("取消", (dialogInterface, i) -> {

                        }).create();
                dialog.show();
                break;
        }
    }
}
