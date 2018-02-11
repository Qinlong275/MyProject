package com.qinlong275.android.cniaoplay.di.module;

import android.app.Application;

import com.google.gson.Gson;
import com.qinlong275.android.cniaoplay.AppAplication;
import com.qinlong275.android.cniaoplay.common.http.CommonParamsInterceptor;
import com.qinlong275.android.cniaoplay.common.rx.RxErrorHandler;
import com.qinlong275.android.cniaoplay.data.http.ApiService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 秦龙 on 2018/2/6.
 */

@Module
public class HttpModule {

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(Application application,Gson gson){
        // log用拦截器
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // 开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 如果使用到HTTPS，我们需要创建SSLSocketFactory，并设置到client
//        SSLSocketFactory sslSocketFactory = null;

        return new OkHttpClient.Builder()
                // HeadInterceptor实现了Interceptor，用来往Request Header添加一些业务相关数据，如APP版本，token信息
//                .addInterceptor(new HeadInterceptor())
                .addInterceptor(logging)
                .addInterceptor(new CommonParamsInterceptor(gson,application))
                // 连接超时时间设置
                .connectTimeout(10, TimeUnit.SECONDS)
                // 读取超时时间设置
                .readTimeout(10, TimeUnit.SECONDS)

                .build();


    }


    @Singleton
    @Provides
    public Retrofit provideRetrofit(OkHttpClient okHttpClient){


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient);


        return builder.build();

    }


    @Provides
    @Singleton
    public ApiService provideApiService(Retrofit retrofit){
        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    public RxErrorHandler provideErrorHandler(Application application){
        return new RxErrorHandler(application);
    }
}
