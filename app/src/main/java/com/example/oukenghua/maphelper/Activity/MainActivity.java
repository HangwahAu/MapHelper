package com.example.oukenghua.maphelper.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.ToastUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final int CAMERA_OK = 100;
    @BindView(R.id.result_tv)
    TextView resultTv;

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

    }

    @Override
    public void loadData() {

    }

    @OnClick(R.id.scanning)
    public void onViewClicked() {
        //Android系统版本6.0或以上需要动态获取权限
        if (Build.VERSION.SDK_INT > 22) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //没有获取权限则先获取权限
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_OK);
            } else {
                //如已获取权限则继续
                new IntentIntegrator(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)  //所有类型的码
                        .setPrompt("将二维码/条码放入框内，即可自动扫描")  //底部提示语
                        .setCameraId(0)  //0为后置 1为前置摄像头
                        .setBeepEnabled(false)  //扫描成功后不发出beep
                        .setTimeout(10000)  //10秒没操作将自动退出
                        .setCaptureActivity(ScanActivity.class)  //扫描的Activity
                        .initiateScan();  //初始化
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result;
        if(requestCode == IntentIntegrator.REQUEST_CODE && resultCode == ScanActivity.SPOT_SUCCESS){
            result = data.getStringExtra("data");
            resultTv.setText(result);

        }else{
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            result = intentResult.getContents();
            resultTv.setText(result);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    ToastUtil.setShortToast("请手动打开相机权限");
                }
                break;
            default:
                break;
        }
    }
}
