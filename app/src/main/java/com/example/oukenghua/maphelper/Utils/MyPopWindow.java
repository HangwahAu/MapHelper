package com.example.oukenghua.maphelper.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.oukenghua.maphelper.R;

/**
 * Created by oukenghua on 2018/4/14.
 */

public class MyPopWindow extends PopupWindow {

    public MyPopWindow(Activity activity, Bitmap bitmap,String serial){

        //加载布局
        View contentView = LayoutInflater.from(activity).inflate(R.layout.pop_qcode,null);

        //获取宽高
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();

        //设置popupwindow的view
        this.setContentView(contentView);

        //设置popupwindow的弹窗宽高
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //设置弹窗可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);  //当点击外围的时候隐藏PopupWindow

        //刷新状态
        this.update();

        //设置弹窗背景为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        backgroundAlpha(activity, 0.5f);//0.0-1.0
        this.setOnDismissListener(() -> {
            //弹窗取消 恢复
            backgroundAlpha(activity, 1f);
        });

        //设置弹窗动画效果
        this.setAnimationStyle(R.style.Widget_AppCompat_PopupWindow);

        //加载二维码
        ImageView imageView = contentView.findViewById(R.id.qcode_image);
        imageView.setImageBitmap(bitmap);
        TextView textView = contentView.findViewById(R.id.qcode_serial);
        textView.setText("序列号:"+serial);

    }

    public void showPopupWindow(View parent){
        if(!this.isShowing()){
            this.showAtLocation(parent, Gravity.CENTER,0,0);
        }else {
            dismiss();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

}
