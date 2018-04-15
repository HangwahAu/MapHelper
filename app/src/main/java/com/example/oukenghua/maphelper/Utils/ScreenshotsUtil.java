package com.example.oukenghua.maphelper.Utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;

import com.example.oukenghua.maphelper.MyApplication;

import java.io.File;
import java.io.OutputStream;

/**
 * Created by oukenghua on 2018/4/14.
 */

public class ScreenshotsUtil {

    public static Handler mDialogHandler = new Handler();

    public static void saveBmpToGallery(String picName, Activity context) {

        /**
         * 保存截图
         */
        String dirName = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "jwd";
        File fileDir = new File(dirName);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        // 系统时间
        long mImageTime = System.currentTimeMillis();
        long dateSeconds = mImageTime / 1000;

        // 文件名
        String mImageFileName = picName;
        // 文件路径
        String mImageFilePath = dirName + File.separator + mImageFileName;

        File file = new File(mImageFilePath);
        if (file.exists()) {
            ToastUtil.setShortToast("二维码已存在");
            mDialogHandler.sendEmptyMessageDelayed(1, 1000);
            return;
        }

        //获取截屏
        View view = context.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        int mImageWidth = bitmap.getWidth();
        int mImageHeight = bitmap.getHeight();

        // 保存截屏到系统MediaStore
        ContentValues values = new ContentValues();
        ContentResolver resolver = context.getContentResolver();
        values.put(MediaStore.Images.ImageColumns.DATA, mImageFilePath);
        values.put(MediaStore.Images.ImageColumns.TITLE, mImageFileName);
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, mImageFileName);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, mImageTime);
        values.put(MediaStore.Images.ImageColumns.DATE_ADDED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.ImageColumns.WIDTH, mImageWidth);
        values.put(MediaStore.Images.ImageColumns.HEIGHT, mImageHeight);
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
        try {
            OutputStream out = resolver.openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);// bitmap转换成输出流，写入文件
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // update file size in the database
        values.clear();
        values.put(MediaStore.Images.ImageColumns.SIZE,
                new File(mImageFilePath).length());
        resolver.update(uri, values, null, null);
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);
        ToastUtil.setShortToast("二维码保存成功");
        mDialogHandler.sendEmptyMessageDelayed(1, 1000);

    }

}
