package com.qinlong275.android.cniaoplay.common.http;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qinlong275.android.cniaoplay.common.Constant;
import com.qinlong275.android.cniaoplay.common.util.ACache;
import com.qinlong275.android.cniaoplay.common.util.DensityUtil;
import com.qinlong275.android.cniaoplay.common.util.DeviceUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by 秦龙 on 2018/2/10.
 */

public class CommonParamsInterceptor implements Interceptor{

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private Gson mGson;

    private Context mContext;

    public CommonParamsInterceptor(Gson gson,Context context) {
        mGson = gson;
        mContext=context;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();// 112.124.22.238:8081/course_api/cniaoplay/featured?p={'page':0}
        try {


            String method=request.method();

            HashMap<String,Object> commonParamsMap=new HashMap<>();

            commonParamsMap.put(Constant.IMEI, DeviceUtils.getIMEI(mContext));
            commonParamsMap.put(Constant.MODEL,DeviceUtils.getModel());
            commonParamsMap.put(Constant.LANGUAGE,DeviceUtils.getLanguage());
            commonParamsMap.put(Constant.os,DeviceUtils.getBuildVersionIncremental());
            commonParamsMap.put(Constant.RESOLUTION, DensityUtil.getScreenW(mContext)+"*" + DensityUtil.getScreenH(mContext));
            commonParamsMap.put(Constant.SDK,DeviceUtils.getBuildVersionSDK()+"");
            commonParamsMap.put(Constant.DENSITY_SCALE_FACTOR,mContext.getResources().getDisplayMetrics().density+"");
            String token = ACache.get(mContext).getAsString(Constant.TOKEN);
            commonParamsMap.put(Constant.TOKEN,token==null?"":token);

            if (method.equals("GET")){
                HttpUrl httpUrl=request.url();


                HashMap<String,Object> rootMap = new HashMap<>();

                Set<String> paramNames =  httpUrl.queryParameterNames();

                for (String key :paramNames){

                    if(Constant.PARAM.equals(key)){

                        String oldParamJson =  httpUrl.queryParameter(Constant.PARAM);
                        if(oldParamJson!=null){
                            HashMap<String,Object> p =  mGson.fromJson(oldParamJson,HashMap.class); // 原始参数

                            if(p !=null){
                                for (Map.Entry<String,Object> entry :p.entrySet()){

                                    rootMap.put(entry.getKey(),entry.getValue());
                                }
                            }
                        }
                    }
                    else {
                        rootMap.put(key,httpUrl.queryParameter(key));
                    }
                }
                rootMap.put("publicParams",commonParamsMap);

                String newJsonParams=mGson.toJson(rootMap);
                String url=httpUrl.toString();
                int index=url.indexOf("?");
                if (index>0){
                    url=url.substring(0,url.indexOf("?"));
                }
                url=url+"?"+Constant.PARAM+"="+newJsonParams;
                request=request.newBuilder().url(url).build();


            }else if (method.equals("POST")){


                RequestBody body = request.body();


                HashMap<String,Object> rootMap = new HashMap<>();
                if(body instanceof FormBody){ // form 表单

                    for (int i=0;i<((FormBody) body).size();i++){

                        rootMap.put(((FormBody) body).encodedName(i),((FormBody) body).encodedValue(i));
                    }

                }
                else{
                    //json形式
                    Buffer buffer = new Buffer();

                    body.writeTo(buffer);

                    String oldJsonParams =  buffer.readUtf8();

                    if (!TextUtils.isEmpty(oldJsonParams)){

                        rootMap = mGson.fromJson(oldJsonParams,HashMap.class); // 原始参数
                        if (rootMap!=null){
                            rootMap.put("publicParams",commonParamsMap); // 重新组装
                            String newJsonParams = mGson.toJson(rootMap); // {"page":0,"publicParams":{"imei":'xxxxx',"sdk":14,.....}}
                            request = request.newBuilder().post(RequestBody.create(JSON, newJsonParams)).build();
                        }

                    }
                }
            }
        }catch (JsonSyntaxException e){
            e.printStackTrace();
        }

        return chain.proceed(request);
    }
}
