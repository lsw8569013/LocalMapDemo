package com.map.demo.bean;

import android.webkit.JavascriptInterface;

import com.map.demo.config.App;


/**
 * Created by zhaoqi on 2017/12/24.
 */

public class JsEnvInterface
{


    @JavascriptInterface
    public boolean isCloudConnected()
    {
        return App.cloudServiceOk;
//        return false;
    }


    @JavascriptInterface
    public boolean isLocalConnected()
    {
        return App.localServiceOk;
    }


    @JavascriptInterface
    public int getUserId()
    {
        LoginUserInfo userInfo = App.getApplication().getLoginUser();
        if ( userInfo != null ) {
            return userInfo.getUserId();
        }
        else {
            return 0;
        }
    }


    @JavascriptInterface
    public String getUserName()
    {
        LoginUserInfo userInfo = App.getApplication().getLoginUser();
        if ( userInfo != null ) {
            return userInfo.getUserName();
        }
        else {
            return "";
        }
    }


    @JavascriptInterface
    public String getUserGender()
    {
        LoginUserInfo userInfo = App.getApplication().getLoginUser();
        if ( userInfo != null ) {
            return userInfo.getGender();
        }
        else {
            return "";
        }
    }


    @JavascriptInterface
    public String getUserNationality()
    {
        LoginUserInfo userInfo = App.getApplication().getLoginUser();
        if ( userInfo != null ) {
            return userInfo.getNationality();
        }
        else {
            return "";
        }
    }


    @JavascriptInterface
    public String getUserFavoriteSports()
    {
        LoginUserInfo userInfo = App.getApplication().getLoginUser();
        if ( userInfo != null ) {
            return userInfo.getFavoriteSports();
        }
        else {
            return "";
        }
    }


    @JavascriptInterface
    public String getUserFaceId()
    {
        LoginUserInfo userInfo = App.getApplication().getLoginUser();
        if ( userInfo != null ) {
            return userInfo.getFaceId();
        }
        else {
            return "";
        }
    }


    @JavascriptInterface
    public String getUserFaceUrl()
    {
        LoginUserInfo userInfo = App.getApplication().getLoginUser();
        if ( userInfo != null ) {
            return userInfo.getFaceUrl();
        }
        else {
            return "";
        }
    }
}
