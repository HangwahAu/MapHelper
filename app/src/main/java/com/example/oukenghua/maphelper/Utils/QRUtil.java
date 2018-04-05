package com.example.oukenghua.maphelper.Utils;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * Created by oukenghua on 2018/4/4.
 */

public class QRUtil {

    private static final int QR_BM_SIZE = 400;
    //private static final int QR_BM_LOGO_SIZE = QR_BM_SIZE / 3;

    public static Bitmap createQRBitmap(String context,int bm_size){
        return createQRBitmap(context,bm_size,null,2);
    }

    /**
     * 生成二维码
     */
    public static Bitmap createQRBitmap(String context,int bm_size,Bitmap logo,int edgeMargin){
        return null;
    }

    /**
     * 识别bitmap中的二维码信息
     */
    public static Result spotQRCode(Bitmap bitmap)throws FormatException, ChecksumException, NotFoundException{

        if(bitmap == null || bitmap.isRecycled())
            return null;
        Result result = null;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] data = new int[width*height];

        bitmap.getPixels(data,0,width,0,0,width,height);
        RGBLuminanceSource source = new RGBLuminanceSource(width,height,data);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

        QRCodeReader reader = new QRCodeReader();
        //result中包含了扫描到的信息，调用result.getText()可以获取到文本信息
        result = reader.decode(binaryBitmap);
        bitmap.recycle();
        return result;
    }
}
