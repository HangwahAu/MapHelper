package com.example.oukenghua.maphelper.Utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by oukenghua on 2018/1/10.
 */


/**
 * Recyclerview的item间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    public SpaceItemDecoration(int space){
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildPosition(view) != -1)
            outRect.top = space;
    }
}
