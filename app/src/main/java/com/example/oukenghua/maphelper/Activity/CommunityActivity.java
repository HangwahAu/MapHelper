package com.example.oukenghua.maphelper.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.oukenghua.maphelper.Adapter.CommunityAdapter;
import com.example.oukenghua.maphelper.Base.BaseActivity;
import com.example.oukenghua.maphelper.LitePalDao.Community;
import com.example.oukenghua.maphelper.R;
import com.example.oukenghua.maphelper.Utils.Constant;
import com.example.oukenghua.maphelper.Utils.HttpClientUtils;
import com.example.oukenghua.maphelper.Utils.SharedPreferencesUtil;
import com.example.oukenghua.maphelper.Utils.SpaceItemDecoration;
import com.example.oukenghua.maphelper.Utils.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CommunityActivity extends BaseActivity {

    @BindView(R.id.my_status)
    View myStatus;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_community)
    RecyclerView recyclerCommunity;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private boolean flag = false;
    private CommunityAdapter communityAdapter;
    private List<Community> communityList = new ArrayList<>();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_community;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        takeLinearStatusBar(myStatus);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());
        if (SharedPreferencesUtil.getInstance().getString(Constant.USER_NAME) != null) {
            //判断当前是否为登录状态
            flag = true;
        }
        recyclerCommunity.setLayoutManager(new LinearLayoutManager(CommunityActivity.this));
        recyclerCommunity.addItemDecoration(new SpaceItemDecoration(40));
        communityAdapter = new CommunityAdapter(R.layout.item_details_rv, communityList, flag);
        recyclerCommunity.setAdapter(communityAdapter);
        communityAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Intent intent = new Intent();
            switch (view.getId()) {
                case R.id.item_selected:
                    //跳到子级
                    intent.setClass(CommunityActivity.this,BuildingActivity.class);
                    intent.putExtra(Constant.SERIAL_NUMBER,communityList.get(position).getSerial());
                    intent.putExtra(Constant.ADDRESS,communityList.get(position).getAddress());
                    startActivity(intent);
                    break;
                case R.id.item_look:
                    //查看详细信息
                    intent.setClass(CommunityActivity.this, AddressActivity.class);
                    intent.putExtra(Constant.ADDRESS_DATA, communityList.get(position));
                    intent.putExtra(Constant.ADDRESS_LEVEL,1);
                    startActivity(intent);
                    break;
                case R.id.item_edit:
                    //跳到编辑界面
                    intent.setClass(CommunityActivity.this, AddAddressActivity.class);
                    intent.putExtra(Constant.ADDRESS_DATA, communityList.get(position));
                    intent.putExtra("isEdit", true);
                    startActivityForResult(intent, Constant.EDIT_ADDRESS);
                    break;
                case R.id.item_delete:
                    //删除
                    AlertDialog dialog = new AlertDialog.Builder(CommunityActivity.this)
                            .setTitle("是否确认删除当前信息？")
                            .setPositiveButton("确定", (dialogInterface, i) -> {
                                deleteCommunity(communityList.get(position).getSerial());
                            }).setNegativeButton("取消", (dialogInterface, i) -> {
                            }).create();
                    dialog.show();

                    break;
            }
        });
        refreshLayout.setOnRefreshListener(refreshLayout -> {
            communityList.clear();
            LoadDataFromServer();
            if(!flag){
                handler.postDelayed(() -> {
                    //先删除原有的
                    DataSupport.deleteAll(Community.class);
                    //再更新数据库
                    for (int i = 0; i < communityList.size(); i++) {
                        communityList.get(i).save();
                    }
                }, 2000);
            }
            refreshLayout.finishRefresh(3000);
            ToastUtil.setShortToast("更新成功");
        });
    }

    @Override
    public void loadData() {
        communityList.clear();
        if (flag) {
            //有登录的话
            LoadDataFromServer();
        } else {
            //没有登录就从本地数据库查询数据
            List<Community> communities = DataSupport.findAll(Community.class);
            if (communities.size() == 0) {//如果本地数据库没有数据
                //从服务器取数据
                LoadDataFromServer();
                //存进本地数据库 延缓两秒等待数据请求完毕
                handler.postDelayed(() -> {
                    for (int i = 0; i < communityList.size(); i++) {
                        communityList.get(i).save();
                    }
                }, 2000);
            } else if (communities.size() > 0) {
                //如果本地数据库有数据 则加载
                communityList.addAll(communities);
                communityAdapter.notifyDataSetChanged();
            }
        }

    }

    //从服务器获取数据
    private void LoadDataFromServer() {
        HttpClientUtils.getHttpUrl(Constant.SERVER_HTTP).getCommunityList()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(community -> {

                    if (community.size() != 0 && community != null) {
                        communityList.addAll(community);
                    }
                    communityAdapter.notifyDataSetChanged();

                }, throwable -> {

                });
    }

    private void deleteCommunity(String serial) {
        HttpClientUtils.getHttpUrl(Constant.SERVER_HTTP).deleteCommunity(serial)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(responseBody -> {

                    String string = responseBody.string();
                    JSONObject object = new JSONObject(string);
                    int code = object.getInt("code");
                    if (code == 1) {
                        ToastUtil.setShortToast("删除成功");
                        //管理员修改地址 本地数据库不需要做出改变 因为管理员不需要用到本地数据库
                        //DataSupport.deleteAll(Community.class,"serial = ?",serial);
                        loadData();
                    } else {
                        ToastUtil.setShortToast("删除失败");
                    }

                }, throwable -> {

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case Constant.EDIT_ADDRESS:
                    loadData();
            }
        }
    }
}
