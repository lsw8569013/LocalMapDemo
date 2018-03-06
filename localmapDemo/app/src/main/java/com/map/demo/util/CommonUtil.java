package com.map.demo.util;


import android.app.Activity;
import android.util.DisplayMetrics;

import com.map.demo.config.App;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ${xue} on 2016/11/9.
 * Tel:15836901053
 * Email:ThoffyXue@outlook.com
 */

public class CommonUtil {

    public static void runOnUIThread(Runnable r) {
        App.handler.post(r);
    }
    /**
     *
     * @param phoneNum 传入的参数仅仅是一个电话号码时，调用此方法
     * @return 如果匹配正确，return true , else return else
     */
    //Pattern p = Pattern.compile("^(13[0-9]|15[012356789]|17[6780]|18[0-9]|14[57])[0-9]{8}$");
    //如果传进来的是电话号码，则对电话号码进行正则匹配
    public static boolean regexPhoneNumber(String phoneNum){
        Pattern p = Pattern.compile("^(1[34578])[0-9]{9}$");
        Matcher m = p.matcher(phoneNum);
        if(!m.matches()){
            return false;
        }
        return true;
    }


    public static  int getScreenWidth(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();

       ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();

       ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }



}