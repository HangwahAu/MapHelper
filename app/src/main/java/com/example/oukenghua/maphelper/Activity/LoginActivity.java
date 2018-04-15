package com.example.oukenghua.maphelper.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.dd.morphingbutton.MorphingButton;
import com.dd.morphingbutton.impl.IndeterminateProgressButton;
import com.dd.morphingbutton.impl.LinearProgressButton;
import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.Constant;
import com.example.oukenghua.maphelper.Utils.ProgressGenerator;
import com.example.oukenghua.maphelper.Utils.SharedPreferencesUtil;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.account_et)
    EditText accountEt;
    @BindView(R.id.password_et)
    EditText passwordEt;
    @BindView(R.id.btn_login)
    LinearProgressButton btnLogin;

    private int flag = 1;

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
        btnLogin.setOnClickListener(view -> onButtonClick(btnLogin));
        morphToSquare(btnLogin, 0);
    }

    @Override
    public void loadData() {

    }

    private void onButtonClick(MorphingButton button){
        String account = accountEt.getText().toString();
        String password = passwordEt.getText().toString();
        if(flag == 0){
            flag ++;
            morphToSquare(button,integer(R.integer.mb_animation));
        }else if(flag == 1){
            if(account.equals("oukenghua") && password.equals("123456")){
                simulateProgress(btnLogin,true);
                SharedPreferencesUtil.getInstance().putString(Constant.USER_NAME,account);
                accountEt.setText("");
                passwordEt.setText("");
                flag = 2;
            }else {
                simulateProgress(btnLogin,false);
                flag = 0;
            }
        }else if(flag == 2){
            Intent intent = new Intent(LoginActivity.this,ManagementActivity.class);
            startActivity(intent);
            morphToSquare(button,integer(R.integer.mb_animation));
            flag = 1;
        }
    }

    private void simulateProgress(@NonNull final LinearProgressButton button,boolean isSuccess) {
        int progressColor = getResources().getColor(R.color.mb_purple);
        int color = getResources().getColor(R.color.mb_gray);
        int progressCornerRadius = dimen(R.dimen.mb_corner_radius_4);
        int width = dimen(R.dimen.mb_width_200);
        int height = dimen(R.dimen.mb_height_8);
        int duration = integer(R.integer.mb_animation);

        ProgressGenerator generator = new ProgressGenerator(() -> {
            if(isSuccess){
                morphToSuccess(button);
            }else {
                morphToFailure(button);
            }
            button.unblockTouch();
        });
        button.blockTouch(); // prevent user from clicking while button_orange is in progress
        button.morphToProgress(color, progressColor, progressCornerRadius, width, height, duration);
        generator.start(button);
    }

    private void morphToSquare(MorphingButton btnMorph, int duration) {
        MorphingButton.Params square = MorphingButton.Params.create()
                .duration(duration)
                .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                .width(dimen(R.dimen.mb_width_300))
                .height(dimen(R.dimen.mb_height_50))
                .color(getResources().getColor(R.color.mb_blue))
                .colorPressed(getResources().getColor(R.color.mb_blue_dark))
                .text("登录");
        btnMorph.morph(square);
    }

    private void morphToSuccess(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_width_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(getResources().getColor(R.color.mb_green))
                .colorPressed(getResources().getColor(R.color.mb_green_dark))
                .icon(R.drawable.ic_done);
        btnMorph.morph(circle);
    }

    private void morphToFailure(final MorphingButton btnMorph) {
        MorphingButton.Params circle = MorphingButton.Params.create()
                .duration(integer(R.integer.mb_animation))
                .cornerRadius(dimen(R.dimen.mb_height_56))
                .width(dimen(R.dimen.mb_width_56))
                .height(dimen(R.dimen.mb_height_56))
                .color(getResources().getColor(R.color.mb_red))
                .colorPressed(getResources().getColor(R.color.mb_red_dark))
                .icon(R.drawable.ic_lock);
        btnMorph.morph(circle);
    }
}
