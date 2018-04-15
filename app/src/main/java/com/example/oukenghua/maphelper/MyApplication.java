package com.example.oukenghua.maphelper;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.example.oukenghua.maphelper.Utils.SharedPreferencesUtil;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

/**
 * Created by oukenghua on 2018/3/24.
 */

public class MyApplication extends LitePalApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(this);
        Utils.init(this);
        SharedPreferencesUtil.init(getApplicationContext(), getPackageName() + "_preference", Context.MODE_PRIVATE);
    }

    public static Context getContext(){
        return context;
    }
}
