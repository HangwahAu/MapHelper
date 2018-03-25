package com.example.oukenghua.maphelper.Utils;

import android.widget.Toast;

import com.example.oukenghua.maphelper.MyApplication;

/**
 * Created by oukenghua on 2018/3/24.
 */

/**
 * Toast工具类
 */
public class ToastUtil {

    public static void setShortToast(String msg){
        Toast.makeText(MyApplication.getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public static void setLongToast(String msg){
        Toast.makeText(MyApplication.getContext(),msg,Toast.LENGTH_LONG).show();
    }

}
