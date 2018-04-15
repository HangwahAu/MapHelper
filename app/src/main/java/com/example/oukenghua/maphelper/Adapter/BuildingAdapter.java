package com.example.oukenghua.maphelper.Adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.oukenghua.maphelper.LitePalDao.Building;
import com.example.oukenghua.maphelper.LitePalDao.Community;
import com.example.oukenghua.maphelper.R;

import java.util.List;

/**
 * Created by oukenghua on 2018/4/15.
 */

public class BuildingAdapter extends BaseQuickAdapter<Building,BaseViewHolder>{

    private boolean flag = false;

    public BuildingAdapter(int resId, List<Building> list, boolean flag){
        super(resId,list);
        this.flag = flag;
    }

    @Override
    protected void convert(BaseViewHolder helper, Building item) {
        helper.setText(R.id.item_serial,"序列号:"+item.getSerial());
        helper.setText(R.id.item_address,"地址:"+item.getAddress());
        helper.addOnClickListener(R.id.item_selected);
        helper.addOnClickListener(R.id.item_look);
        if(!flag){//没有登录时隐藏删除和编辑按钮
            helper.setVisible(R.id.item_delete,false);
            helper.setVisible(R.id.item_edit,false);
        }else {
            helper.setVisible(R.id.item_delete,true);
            helper.setVisible(R.id.item_edit,true);
            helper.addOnClickListener(R.id.item_delete);
            helper.addOnClickListener(R.id.item_edit);
        }
    }

}
