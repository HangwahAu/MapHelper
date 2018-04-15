package com.example.oukenghua.maphelper.Activity;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.LitePalDao.Community;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.Constant;
import com.example.oukenghua.maphelper.Utils.HttpClientUtils;
import com.example.oukenghua.maphelper.Utils.MyPopWindow;
import com.example.oukenghua.maphelper.Utils.QCodeUtil;
import com.example.oukenghua.maphelper.Utils.ScreenshotsUtil;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddressActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.community_image)
    ImageView communityImage;
    @BindView(R.id.address_security)
    TextView addressSecurity;
    @BindView(R.id.address_policemen)
    TextView addressPolicemen;
    @BindView(R.id.my_status)
    View myStatus;
    @BindView(R.id.btn_first)
    TextView btnFirst;
    @BindView(R.id.btn_second)
    TextView btnSecond;
    @BindView(R.id.top_address_tv)
    TextView topAddressTv;
    @BindView(R.id.bottom_address_tv)
    TextView bottomAddressTv;

    private String address, security, policemen, serial_number, parent_id = "";
    private int address_level = -1;
    private boolean isScan = false;


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
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        takeLinearStatusBar(myStatus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.whitearrow);
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    @Override
    public void loadData() {
        address_level = getIntent().getIntExtra(Constant.ADDRESS_LEVEL, -1);
        if (address_level == 1) {//小区
            btnFirst.setText("进入小区");
            btnSecond.setVisibility(View.GONE);
        } else if (address_level == 2) {//建筑物
            btnFirst.setText("进入建筑");
        } else if (address_level == 3) {
            btnFirst.setVisibility(View.GONE);
        }
        //扫描后跳转的话
        isScan = getIntent().getBooleanExtra("isScan",false);
        if(isScan){
            serial_number = getIntent().getStringExtra(Constant.SERIAL_NUMBER);
            HttpClientUtils.getHttpUrl(Constant.SERVER_HTTP).getCommunityBySerial(serial_number)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(community1 -> {

                        if(community1 != null){
                            address = community1.getAddress();
                            security = community1.getSecurity();
                            policemen = community1.getPolicemen();
                            topAddressTv.setText(address);
                            bottomAddressTv.setText(address);
                            addressSecurity.setText("公安管辖：" + security);
                            addressPolicemen.setText(policemen);
                        }

                    }, throwable -> {

                    });
        }else {
            //查找过来的
            Community community = getIntent().getParcelableExtra(Constant.ADDRESS_DATA);
            address = community.getAddress();
            serial_number = community.getSerial();
            security = community.getSecurity();
            policemen = community.getPolicemen();
            topAddressTv.setText(address);
            bottomAddressTv.setText(address);
            addressSecurity.setText("公安管辖：" + security);
            addressPolicemen.setText(policemen);
        }
    }

    @OnClick({R.id.icon_navigation, R.id.address_qcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon_navigation:
                break;
            case R.id.address_qcode:
                Bitmap bitmap = QCodeUtil.encodeAsBitmap(serial_number);  //根据序列号生成二维码
                MyPopWindow popWindow = new MyPopWindow(AddressActivity.this,bitmap,serial_number);
                popWindow.showPopupWindow(new View(AddressActivity.this));
                break;
        }
    }
}
