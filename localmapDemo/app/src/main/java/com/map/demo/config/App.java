package com.map.demo.config;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.map.demo.bean.LoginUserInfo;



/**
 * Created by wb-yl349288 on 2017/11/28.
 */

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static App instance;

    public static Context context;
    public static Handler handler;
    public static boolean isLog = true;

    public static boolean cloudServiceOk = true;
    public static boolean localServiceOk = true;

    public static boolean enableIdst = false;


    //用户信息
    public LoginUserInfo loginUser = null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        context = this;
        handler = new Handler();

//        Thread.currentThread().setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());

        //内存泄漏检测
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);

//        Intent intent = new Intent( getApplicationContext(), CheckNetStatusService.class );
//        startService( intent );



    }


    @Override
    public void onTerminate()
    {
        Log.d( TAG, "onTerminate" );


//        intent = new Intent( getApplicationContext(), CheckNetStatusService.class );
//        stopService( intent );

        super.onTerminate();
    }


    public static App getApplication() {
        return instance;
    }


    public LoginUserInfo getLoginUser() {
        return loginUser;
    }


    public void setLoginUserActive() {
        if ( loginUser != null ) {
            loginUser.setLastRecognizeTime( System.currentTimeMillis() );
        }
    }
}
