package com.example.oukenghua.maphelper.Utils;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Created by oukenghua on 2018/4/14.
 */

public class QCodeUtil {

    public static Bitmap encodeAsBitmap(String str){


        Bitmap bitmap = null;
        BitMatrix bitMatrix = null;

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try{

            bitMatrix = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

            bitmap = barcodeEncoder.createBitmap(bitMatrix);

        }catch (WriterException e){
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            return null;
        }

        return bitmap;

    }

}
