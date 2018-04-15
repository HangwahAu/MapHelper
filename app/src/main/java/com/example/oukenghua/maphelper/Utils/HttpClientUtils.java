package com.example.oukenghua.maphelper.Utils;

import android.util.Log;

import com.example.oukenghua.maphelper.Interface.ApiStore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by oukenghua on 2018/4/7.
 */

public class HttpClientUtils {

    private static HttpClientUtils clientData;

    public HttpClientUtils(){}

    private static HttpClientUtils getInstance(){
        if(clientData == null){
            synchronized (HttpClientUtils.class){
                if (clientData == null){
                    clientData = new HttpClientUtils();
                }
            }
        }
        return clientData;
    }

    private static Map<String, Retrofit> retroList = new HashMap<>(); //记录baseUrl和retrofit
    private static Retrofit mRetro = null;

    /**
     *请求地址
     * @param http
     * @return
     */
    private static Retrofit getHttpClient(String http){

        if(!onCheckUrl(http)){
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message ->
                    Log.e("Tag","message--->" + message)).setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder okhttpClient = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS);
            mRetro = new Retrofit.Builder().addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory
                            (RxJava2CallAdapterFactory.create()).baseUrl(http).client
                            (okhttpClient.build()).build();
            retroList.put(http,mRetro);
        }
        return mRetro;
    }

    public static ApiStore getHttpUrl(String http){
        return getHttpClient(http).create(ApiStore.class);
    }

    private static boolean onCheckUrl(String http){
        for(String url : retroList.keySet()){
            if(url.equals(http)){
                mRetro = retroList.get(url);
                return true;
            }
        }
        return false;
    }

}
