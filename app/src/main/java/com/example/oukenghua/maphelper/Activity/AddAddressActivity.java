package com.example.oukenghua.maphelper.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.LitePalDao.Building;
import com.example.oukenghua.maphelper.LitePalDao.Community;
import com.example.oukenghua.maphelper.MyApplication;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.Constant;
import com.example.oukenghua.maphelper.Utils.HttpClientUtils;
import com.example.oukenghua.maphelper.Utils.QCodeUtil;
import com.example.oukenghua.maphelper.Utils.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

import static com.example.oukenghua.maphelper.Utils.ScreenshotsUtil.saveBmpToGallery;

public class AddAddressActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.address_id)
    EditText addressId;
    @BindView(R.id.address_details)
    EditText addressDetails;
    @BindView(R.id.address_security)
    EditText addressSecurity;
    @BindView(R.id.address_policemen)
    EditText addressPolicemen;
    @BindView(R.id.qcode_image)
    ImageView qcodeImage;
    @BindView(R.id.address_parent_id)
    EditText addressParentId;
    @BindView(R.id.my_status)
    View myStatus;
    @BindView(R.id.address_title)
    TextView addressTitle;
    @BindView(R.id.btn_qcode)
    Button btnQcode;

    private String address, security, policemen, serial_number, parent_id = "";
    private boolean isEdit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_add_address;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        takeLinearStatusBar(myStatus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            AlertDialog dialog = new AlertDialog.Builder(AddAddressActivity.this)
                    .setTitle("是否不保存更改内容？")
                    .setMessage("直接退出则无法更新内容")
                    .setPositiveButton("是", (dialogInterface, i) -> finish())
                    .setNegativeButton("否", (dialogInterface, i) -> {

                    }).create();
            dialog.show();
        });
        isEdit = getIntent().getBooleanExtra("isEdit",false);
        qcodeImage.setOnLongClickListener(view -> {
            //6.0后的运行时权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 没有权限，申请权限。
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }else{
                // 有权限了，去放肆吧。
                saveBmpToGallery(addressId.getText().toString(),this);
            }
            return true;
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 权限被用户同意，可以去放肆了。
                    saveBmpToGallery(addressId.getText().toString(),this);
                } else {
                    // 权限被用户拒绝了，洗洗睡吧。
                    ToastUtil.setShortToast("请手动打开权限");
                }
                return;
            }
        }
    }

    @Override
    public void loadData() {
        if(isEdit){
            Community community = getIntent().getParcelableExtra(Constant.ADDRESS_DATA);
            addressTitle.setText("编辑地址");
            addressId.setText(community.getSerial());
            addressId.setEnabled(false);
            addressDetails.setText(community.getAddress());
            addressPolicemen.setText(community.getPolicemen());
            addressSecurity.setText(community.getSecurity());
            addressParentId.setText(community.getParent());
            btnQcode.setEnabled(true);
        }
    }

    @OnClick({R.id.address_save, R.id.btn_qcode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.address_save:
                initAddAddress();
                break;
            case R.id.btn_qcode:
                Bitmap bitmap = QCodeUtil.encodeAsBitmap(addressId.getText().toString());  //根据序列号生成二维码
                qcodeImage.setImageBitmap(bitmap);
                break;
        }
    }

    private void initAddAddress() {

        address = addressDetails.getText().toString();
        security = addressSecurity.getText().toString();
        policemen = addressPolicemen.getText().toString();
        serial_number = addressId.getText().toString();
        parent_id = addressParentId.getText().toString();

        if (address.length() > 0 && security.length() > 0 && policemen.length() > 0 && !serial_number.equals("") && !parent_id.equals("")) {

            if(parent_id.startsWith("A") || parent_id.startsWith("B") || parent_id.equals("0")){//符合条件的parent_id
                if(isEdit){
                    //修改地址的情况
                    HttpClientUtils.getHttpUrl(Constant.SERVER_HTTP).editCommunity(serial_number,address,security,policemen,parent_id)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(responseBody -> {
                                String string = responseBody.string();
                                JSONObject object = new JSONObject(string);
                                int code = object.getInt("code");
                                if(code == 1){
                                    //修改成功后带返回值返回
                                    ToastUtil.setShortToast("修改成功");
                                    setResult(Constant.EDIT_ADDRESS,new Intent());
                                    finish();
                                }else {
                                    ToastUtil.setShortToast("修改失败");
                                }
                            }, throwable -> {

                            });

                }else {
                    //添加地址的情况
                    if(serial_number.startsWith("A")){
                        //小区的情况
                        if(parent_id.startsWith("0")){
                            HttpClientUtils.getHttpUrl(Constant.SERVER_HTTP).addCommunity(serial_number,address,security,policemen,parent_id)
                                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(responseBody -> {
                                        String string = responseBody.string();
                                        JSONObject object = new JSONObject(string);
                                        int code = object.getInt("code");
                                        if(code == 1){
                                            //添加成功后可以生成二维码门牌
                                            btnQcode.setEnabled(true);
                                            ToastUtil.setShortToast("添加成功");
//                                    addressDetails.setText("");
//                                    addressSecurity.setText("");
//                                    addressPolicemen.setText("");
//                                    addressId.setText("");
//                                    addressParentId.setText("");
                                        }else {
                                            ToastUtil.setShortToast("添加失败");
                                        }
                                    }, throwable -> {

                                    });
                        }else {
                            ToastUtil.setShortToast("输入序列号有误");
                        }
                    }else if(serial_number.startsWith("B")){

                    }else if (serial_number.startsWith("C")){

                    }else {
                        ToastUtil.setShortToast("序列号输入错误");
                    }
                }
            }else {
                ToastUtil.setShortToast("输入的父序列号不存在");
            }
        } else {
            ToastUtil.setShortToast("必须填写完整信息");
        }

    }

}
