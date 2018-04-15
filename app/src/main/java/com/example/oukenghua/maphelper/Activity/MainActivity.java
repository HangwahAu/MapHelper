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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.Constant;
import com.example.oukenghua.maphelper.Utils.SharedPreferencesUtil;
import com.example.oukenghua.maphelper.Utils.ToastUtil;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gyf.barlibrary.ImmersionBar;

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
        //沉浸式
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void loadData() {

    }

    @OnClick({R.id.scanning, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.scanning:
                //Android系统版本6.0或以上需要动态获取权限
                if (Build.VERSION.SDK_INT > 22) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        //没有获取权限则先获取权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_OK);
                    } else {
                        //如已获取权限则继续
                        toScanningView();
                    }
                }
                break;
            case R.id.btn_add:
                Intent intent = new Intent();
                if(SharedPreferencesUtil.getInstance().getString(Constant.USER_NAME) == null){
                    intent.setClass(MainActivity.this, LoginActivity.class);
                }else {
                    intent.setClass(MainActivity.this, ManagementActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result;
        if(data != null){
            if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == ScanActivity.SPOT_SUCCESS) {
                result = data.getStringExtra("data");
                resultTv.setText(result);
                toAddressActivity(result);

            } else {
                IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                result = intentResult.getContents();
                resultTv.setText(result);
                toAddressActivity(result);
            }
        }
    }

    private void toAddressActivity(String result){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AddressActivity.class);
        intent.putExtra(Constant.SERIAL_NUMBER, result);
        intent.putExtra("isScan",true);
        if(result.startsWith("A")){
            intent.putExtra(Constant.ADDRESS_LEVEL,1);
        }else if(result.startsWith("B")){
            intent.putExtra(Constant.ADDRESS_LEVEL,2);
        }else if (result.startsWith("C")){
            intent.putExtra(Constant.ADDRESS_LEVEL,3);
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toScanningView();
                } else {
                    ToastUtil.setShortToast("请手动打开相机权限");
                }
                break;
            default:
                break;
        }
    }

    private void toScanningView(){
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
