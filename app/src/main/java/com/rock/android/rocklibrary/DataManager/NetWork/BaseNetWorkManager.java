package com.rock.android.rocklibrary.DataManager.NetWork;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rock on 16/3/22.
 */
public class BaseNetWorkManager {

    public static final String BASE_URL = "https://api.douban.com/v2/movie/";

    private static final int DEFAULT_TIMEOUT = 30;

    private Retrofit retrofit;

    //构造方法私有
    private BaseNetWorkManager() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }

    private static class SingletonHolder{
        private static final BaseNetWorkManager INSTANCE = new BaseNetWorkManager();
    }

    //获取单例
    public static BaseNetWorkManager getInstance(){
        return SingletonHolder.INSTANCE;
    }
}
