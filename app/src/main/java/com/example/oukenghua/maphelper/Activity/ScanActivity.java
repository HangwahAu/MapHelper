package com.example.oukenghua.maphelper.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.QRSpotHelper;
import com.example.oukenghua.maphelper.Utils.ToastUtil;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oukenghua on 2018/4/3.
 */

public class ScanActivity extends CaptureActivity implements DecoratedBarcodeView.TorchListener {

    public static final int SPOT_SUCCESS = 200;
    @BindView(R.id.zxing_barcode_scanner)
    DecoratedBarcodeView mBarcodeScannerView;
    @BindView(R.id.tv_flashlight)
    TextView tvFlashlight;
    @BindView(R.id.btn_switch_light)
    LinearLayout mSwitchLightView;
    @BindView(R.id.btn_open_album)
    LinearLayout mOpenAlbumView;
    @BindView(R.id.progress)
    ContentLoadingProgressBar mProgressBar;

    private CaptureManager mCapture;

    private boolean isLightOn;

    private QRSpotHelper mQrSpotHelper;  //从相册选取图片并识别二维码

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        initView();
        initCaptureManager(savedInstanceState);
        initListener();

    }

    /**
     * 重要方法
     */
    private void initCaptureManager(Bundle savedInstanceState) {
        //初始化配置扫码界面
        mCapture = new CaptureManager(this, mBarcodeScannerView);
        //intent中携带了通过IntentIntegrator设置的参数
        mCapture.initializeFromIntent(getIntent(), savedInstanceState);
        mCapture.decode();
    }

    private void initView() {
        if (!hasFlash()) { //如果该手机没有闪光灯功能,隐藏
            mSwitchLightView.setVisibility(View.GONE);
        }
        mProgressBar.setVisibility(View.GONE);
    }

    private void initListener() {
        mBarcodeScannerView.setTorchListener(this);

        mSwitchLightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLightOn) {
                    mBarcodeScannerView.setTorchOff();
                    tvFlashlight.setText("打开闪光灯");
                } else {
                    mBarcodeScannerView.setTorchOn();
                    tvFlashlight.setText("关闭闪光灯");
                }
            }
        });

        mOpenAlbumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mQrSpotHelper == null) {
                    mQrSpotHelper = new QRSpotHelper(ScanActivity.this, mOnSpotCallBack);
                }
                mQrSpotHelper.spotFromAlbum();
            }
        });
    }

    @Override
    public void onTorchOn() {
        isLightOn = true;
    }

    @Override
    public void onTorchOff() {
        isLightOn = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCapture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCapture.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCapture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mCapture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mCapture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mBarcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    //判断是否有闪光灯功能
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mQrSpotHelper != null) {
            mQrSpotHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    //照片识别的监听
    private QRSpotHelper.OnSpotCallBack mOnSpotCallBack = new QRSpotHelper.OnSpotCallBack() {
        @Override
        public void onSpotStart() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSpotSuccess(Result result) {
            //识别成功后将返回的结果传递给上层Activity
            mProgressBar.setVisibility(View.GONE);
            String data = result.getText();
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(SPOT_SUCCESS, intent);
            finish();
        }

        @Override
        public void onSpotError() {
            mProgressBar.setVisibility(View.GONE);
            ToastUtil.setShortToast("未发现二维码");
        }
    };
}
