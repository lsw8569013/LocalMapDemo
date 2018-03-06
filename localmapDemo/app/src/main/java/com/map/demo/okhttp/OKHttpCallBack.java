package com.map.demo.okhttp;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.map.demo.base.BaseNoBean;
import com.map.demo.util.JsonUtil;
import com.map.demo.util.LogUtils;
import com.map.demo.util.ToastUtil;
import com.zhy.http.okhttp.callback.Callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by  on 2017/11/29.
 * OkHttp请求callBack
 *
 */
public abstract class OKHttpCallBack<T> extends Callback<T> {


    @Override
    public void onError(Call call, Exception e) {
        if(e instanceof TimeoutException){
            ToastUtil.showToast("服务器链接超时~");
        }else{
            ToastUtil.showToast("服务器错误！"+e.getMessage().toString());
        }
    }

    @Override
    public T parseNetworkResponse(Response response) throws Exception {
        Class <T>  entityClass  =  (Class <T> ) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String body;
        if (Build.VERSION.SDK_INT >= 24) {
            Reader reader = response.body().charStream();
            body = readerToString(reader);
        } else {
            body = response.body().string();
        }
        LogUtils.e(body);
        if (!TextUtils.isEmpty(body)) {
            BaseNoBean bean = JsonUtil.parseJsonToBean(body, BaseNoBean.class);
            if (bean != null) {
                try {
                    //                    return JsonUtil.parseJsonToBean(body,entityClass);
                    return JSON.parseObject(body, entityClass);
                } catch (Exception e) {
                    Log.d("OKHTTP_ERROR", "onResponse: "+e.getMessage());
                    if (bean.result instanceof String && TextUtils.isEmpty((CharSequence) bean.result)) {
                        //Object为字符串且为""
                        //                      ToastUtil.showToast("没有数据");
                        return null;
                    }
                }
            } else {
                //BaseBean为null
                ToastUtil.showToast("网络不给力");
                return null;
            }
        } else {
            //body为空
            ToastUtil.showToast("网络不给力");
            return null;
        }
        return JSON.parseObject(body,entityClass);
    }

    public String readerToString(Reader reader) {
        BufferedReader r = new BufferedReader(reader);
        StringBuilder b = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                b.append(line);
                //                b.append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(b);
    }
}
