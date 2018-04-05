package com.example.oukenghua.maphelper.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.oukenghua.maphelper.R;
import com.journeyapps.barcodescanner.ViewfinderView;

/**
 * Created by oukenghua on 2018/4/4.
 */

public class MyViewFinder extends ViewfinderView {

    public MyViewFinder(Context context, AttributeSet attrs){
        super(context,attrs);
        /*--------添加的内容初始化--------*/
        customInit(context);
    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (framingRect == null || previewFramingRect == null) {
            return;
        }

        final Rect frame = framingRect;
        final Rect previewFrame = previewFramingRect;

        //画自己的样式
        customDraw(frame,canvas);

        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {
            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY,
                    frame.left - POINT_SIZE,
                    frame.top - POINT_SIZE,
                    frame.right + POINT_SIZE,
                    frame.bottom + POINT_SIZE);
        }
    }

    /*------------------------自定义方法和属性------------------------*/
    //画边框相关属性
    private Paint mLinePaint;  //边框画笔
    private final int mLineColor = Color.GREEN;  //边框的颜色

    //滑动条相关属性
    private Bitmap mLineBm;  //滑动条图片
    private RectF mLineReact;  //滑动条区域
    private final int mStepSize = 12;  //滑动条每次滑动的速度
    private final int mLineHeight = 30; //滑动条的高度
    private boolean isBottom = false;  //判断滑动条是否滑到底

    /**
     * 该方法用于构造方法中初始化上述属性
     */
    private void customInit(Context context){
        //初始化滑动条画笔
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(20);
        mLinePaint.setColor(mLineColor);
        //初始化滑动条
        mLineBm = BitmapFactory.decodeResource(getResources(), R.drawable.lan);
    }



    /**
     * 该方法在onDraw中调用，用来画增加的东西
     */
    private void customDraw(Rect frame,Canvas canvas){
        drawSlipLine(frame,canvas);
        drawEdge(frame,canvas);
    }

    /**
     * 画移动的短线
     */
    private void drawSlipLine(Rect frame,Canvas canvas){
        if(mLineReact == null){
            //滑动区域为空
            mLineReact = new RectF(frame.left+5,frame.top,frame.right-5,frame.top+mLineHeight);
        }
        if (isBottom){
            //到底
            mLineReact.set(frame.left+5,frame.top,frame.right-5,frame.top+mLineHeight);
        }
        mLineReact.offset(0,mStepSize);  //每次滑动的距离
        canvas.drawBitmap(mLineBm,null,mLineReact,null);

        isBottom = mLineReact.bottom + mStepSize > frame.bottom;
    }

    /**
     * 画边框四个角
     */
    private void drawEdge(Rect frame,Canvas canvas){
        canvas.drawRect(frame.left-10,frame.top,frame.left,frame.top+50,mLinePaint);
        canvas.drawRect(frame.left-10,frame.top-10,frame.left+50,frame.top,mLinePaint);
        canvas.drawRect(frame.right-50,frame.top-10,frame.right+10,frame.top,mLinePaint);
        canvas.drawRect(frame.right,frame.top,frame.right+10,frame.top+50,mLinePaint);

        canvas.drawRect(frame.left-10,frame.bottom-50,frame.left,frame.bottom,mLinePaint);
        canvas.drawRect(frame.left-10,frame.bottom,frame.left+50,frame.bottom+10,mLinePaint);
        canvas.drawRect(frame.right-50,frame.bottom,frame.right,frame.bottom+10,mLinePaint);
        canvas.drawRect(frame.right,frame.bottom-50,frame.right+10,frame.bottom+10,mLinePaint);
    }

    /**
     * 将sp值转换成px值，保证文字大小不变
     */
    private int sp2px(float spValue){
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*scale+0.5f);
    }

}
