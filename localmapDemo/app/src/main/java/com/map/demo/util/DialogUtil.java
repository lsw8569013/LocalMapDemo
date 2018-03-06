package com.map.demo.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

;

/**
 * Created by wb-yl349288 on 2017/12/25.
 */

public class DialogUtil {

    /**
     * 一般dialog提示
     * @param context 上下文
     * @param message 提示信息
     */
    public static void diaLogInfo(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().setCanceledOnTouchOutside(true);
        builder.create().show();
    }

}
