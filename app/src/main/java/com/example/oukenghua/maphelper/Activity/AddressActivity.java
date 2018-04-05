package com.example.oukenghua.maphelper.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.R;

import butterknife.BindView;

public class AddressActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_address;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.whitearrow);

    }

    @Override
    public void loadData() {

    }
}
