package com.example.oukenghua.maphelper.Interface;

import com.example.oukenghua.maphelper.LitePalDao.Building;
import com.example.oukenghua.maphelper.LitePalDao.Community;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by oukenghua on 2018/4/14.
 */

public interface ApiStore {

    /**
     * 获取小区列表
     * @return
     */
    @GET("community")
    Observable<List<Community>> getCommunityList();


    /**
     * 删除小区
     * @param serial
     * @return
     */
    @FormUrlEncoded
    @POST("community/delete/serial")
    Observable<ResponseBody> deleteCommunity(@Field("serial_number")String serial);


    /**
     * 编辑小区
     * @param serial
     * @param address
     * @param security
     * @param policemen
     * @param parent
     * @return
     */
    @FormUrlEncoded
    @POST("community/edit")
    Observable<ResponseBody> editCommunity(@Field("serial_number")String serial,@Field("address")String address,
                                           @Field("security")String security,@Field("policemen")String policemen,@Field("parent_id")String parent);


    /**
     * 添加小区
     * @param serial
     * @param address
     * @param security
     * @param policemen
     * @param parent
     * @return
     */
    @FormUrlEncoded
    @POST("community/add")
    Observable<ResponseBody> addCommunity(@Field("serial_number")String serial,@Field("address")String address,
                                           @Field("security")String security,@Field("policemen")String policemen,@Field("parent_id")String parent);


    /**
     * 查找小区
     * @param serial
     * @return
     */
    @FormUrlEncoded
    @POST("community/query/serial")
    Observable<Community> getCommunityBySerial(@Field("serial_number")String serial);


    /**
     * 根据parent_id查询建筑物
     * @param parent
     * @return
     */
    @FormUrlEncoded
    @POST("building/query/parent")
    Observable<List<Building>> getBuildingByParent(@Field("parent_id")String parent);
}
