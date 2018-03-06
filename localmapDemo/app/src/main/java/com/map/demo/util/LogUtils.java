package com.map.demo.util;

import android.util.Log;

import com.map.demo.config.App;


/**
 * Created by wb-lsw350290 on 2017/12/5.
 */

public class LogUtils {

    public static void e(String msg){
        if(App.isLog){
            Log.e("lsw",msg);
        }
    }
    public static void w(String msg){
        if(App.isLog){
            Log.w("lsw",msg);
        }
    }

    public static void e(Exception e) {
        if(App.isLog){
            Log.w("lsw",e.getMessage().toString());
        }
    }

    public static void i(String msg) {
        if(App.isLog){
            Log.i("lsw",msg);
        }
    }

}
