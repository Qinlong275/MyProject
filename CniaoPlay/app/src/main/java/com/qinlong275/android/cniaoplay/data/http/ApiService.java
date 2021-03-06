package com.qinlong275.android.cniaoplay.data.http;



import android.app.Application;

import com.qinlong275.android.cniaoplay.bean.AppInfo;
import com.qinlong275.android.cniaoplay.bean.BaseBean;
import com.qinlong275.android.cniaoplay.bean.Category;
import com.qinlong275.android.cniaoplay.bean.IndexBean;
import com.qinlong275.android.cniaoplay.bean.LoginBean;
import com.qinlong275.android.cniaoplay.bean.PageBean;
import com.qinlong275.android.cniaoplay.bean.RequestBean.LoginRequestBean;
import com.qinlong275.android.cniaoplay.bean.SearchResult;
import com.qinlong275.android.cniaoplay.bean.Subject;
import com.qinlong275.android.cniaoplay.bean.SubjectDetail;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Ivan on 2016/12/30.
 */

public interface ApiService {


    public static final String BASE_URL = "http://112.124.22.238:8081/course_api/cniaoplay/";

    @GET("featured2")
    public Observable<BaseBean<PageBean<AppInfo>>> getApps(@Query("p") String jsonParam);


    @POST("login")
    public Observable<BaseBean<LoginBean>> login(@Body LoginRequestBean bean);



    @FormUrlEncoded // FormBody
    @POST("login")
    public  void login2(@Field("phone") String phone);

    @GET("index")
    public Observable<BaseBean<IndexBean>> index();

    @GET("toplist")
    public  Observable<BaseBean<PageBean<AppInfo>>> topList(@Query("page") int page);

    @GET("game")
    public  Observable<BaseBean<PageBean<AppInfo>>> games(@Query("page") int page);

    @GET("category")
    Observable<BaseBean<List<Category>>> getCategories();

    @GET("category/featured/{categoryid}")
    Observable<BaseBean<PageBean<AppInfo>>> getFeaturedAppsByCategory(@Path("categoryid") int categoryid, @Query("page") int page);

    @GET("category/toplist/{categoryid}")
    Observable<BaseBean<PageBean<AppInfo>>> getTopListAppsByCategory(@Path("categoryid") int categoryid,@Query("page") int page);

    @GET("category/newlist/{categoryid}")
    Observable<BaseBean<PageBean<AppInfo>>> getNewListAppsByCategory(@Path("categoryid") int categoryid,@Query("page") int page);

    @GET("app/{id}")
    Observable<BaseBean<AppInfo>> getAppDetail(@Path("id") int id);

    @GET("apps/updateinfo")
    Observable<BaseBean<List<AppInfo>>> getAppsUpdateinfo(@Query("packageName") String packageName,@Query("versionCode") String versionCode);

    @GET("app/hot")
    Observable<BaseBean<PageBean<AppInfo>>> getHotApps(@Query("page") int page);

    @GET("subject/hot")
    Observable<BaseBean<PageBean<Subject>>> subjects(@Query("page") int page);

    @GET("subject/{id}")
    Observable<BaseBean<SubjectDetail>> subjectDetail(@Path("id") int id);


    @GET("search/suggest")
    Observable<BaseBean<List<String>>> searchSuggest(@Query("keyword") String keyword);


    @GET("search")
    Observable<BaseBean<SearchResult>> search(@Query("keyword") String keyword);

}
