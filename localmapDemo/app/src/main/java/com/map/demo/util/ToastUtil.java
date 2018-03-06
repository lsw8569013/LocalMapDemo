package com.map.demo.util;

import android.widget.Toast;

import com.map.demo.config.App;


/**
 * Created by wb-yl349288 on 2017/11/29.
 */

public class ToastUtil {
    private static Toast toast;

    /**
     * 在zhu线程弹吐司
     * @param msg
     */
    public static void showToasts( final String msg) {
        CommonUtil.runOnUIThread(new Runnable() {

            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(App.context, msg, Toast.LENGTH_SHORT);
                } else {
                    toast.setText(msg);
                }
                toast.show();
            }
        });

    }

    /**
     * 在主线程弹吐司
     * @param msg
     */
    public static void showToast (final String msg){
        if (toast == null) {
            toast = Toast.makeText(App.context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

}
