package com.qinlong275.android.cniaoplay.data.http;



import android.app.Application;

import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.IndexBean;
import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.bean.RequestBean.LoginRequestBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Ivan on 2016/12/30.
 */

public interface ApiService {


    public static final String BASE_URL = "http://112.124.22.238:8081/course_api/cniaoplay/";

    @GET("featured2")
    public Observable<BaseBean<PageBean<AppInfo>>> getApps(@Query("p") String jsonParam);


    @POST("login")
    public Observable<BaseBean> login(@Body LoginRequestBean bean);



    @FormUrlEncoded // FormBody
    @POST("login")
    public   void login2(@Field("phone") String phone);

    @GET("index")
    public Observable<BaseBean<IndexBean>> index();

    @GET("toplist")
    public  Observable<BaseBean<PageBean<AppInfo>>> topList(@Query("page") int page);

    @GET("game")
    public  Observable<BaseBean<PageBean<AppInfo>>> games(@Query("page") int page);
}
