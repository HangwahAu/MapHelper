package com.example.oukenghua.maphelper.Utils;

import android.os.Handler;

import com.dd.morphingbutton.IProgress;

import java.util.Random;

/**
 * Created by oukenghua on 2018/4/14.
 */

public class ProgressGenerator {

    public interface OnCompleteListener {

        void onComplete();
    }

    private OnCompleteListener mListener;
    private int mProgress;

    public ProgressGenerator(OnCompleteListener listener) {
        mListener = listener;
    }

    public void start(final IProgress button, int duration) {
        mProgress = 0;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgress += 5;
                button.setProgress(mProgress);
                if (mProgress < 100) {
                    handler.postDelayed(this, generateDelay());
                } else {
                    mListener.onComplete();
                }
            }
        }, duration);
    }

    public void start(final IProgress button) {
        start(button, 500);
    }

    private Random random = new Random();

    private int generateDelay() {
        return random.nextInt(100);
    }

}
