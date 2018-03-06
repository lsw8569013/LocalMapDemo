package com.map.demo.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.map.demo.R;
import com.map.demo.config.App;
import com.map.demo.util.LogUtils;
import com.map.demo.util.SharedPreUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wb-yl349288 on 2017/12/11.
 */

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    private static final String TAG = BaseAppCompatActivity.class.getSimpleName();
    private RelativeLayout mBack;
    private RelativeLayout mEsc;
    private TextView mToolbarRight;
    private Toolbar mToolbar;

    private static List<Activity> activityStack = Collections.synchronizedList(new ArrayList<Activity>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消状态栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutId());

        // 添加Activity到堆栈
        activityStack.add(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
       /*
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub Title");
        */
        mBack = (RelativeLayout) findViewById(R.id.rl_back);
        mEsc = (RelativeLayout) findViewById(R.id.rl_esc);
        mToolbarRight = (TextView) findViewById(R.id.tv_right);
        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mEsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShow();
            }
        });
        init();

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        LogUtils.e("onTouchEvent");
        App.getApplication().setLoginUserActive();

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        LogUtils.e("onTouchEvent _back");

        App.getApplication().setLoginUserActive();
        super.onBackPressed();
    }

    public RelativeLayout getBack() {
        return mBack;
    }

    /**
     * 退出ImageView
     * @return
     */
    public RelativeLayout getEsc() {
        return mEsc;
    }
    /**
     * 获取标题的TextView
     * @return
     */
    public TextView getToolbarRight() {
        return mToolbarRight;
    }

    /**
     * 设置textview的颜色
     * @param color
     */
    public void setRightTextColor(int color) {
        mToolbarRight.setTextColor(color);
    }




    /**
     * 提示用户是否退出
     */
    public void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to quit?");
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

                SharedPreUtil.setParam(App.context, "starOne", 0);
                SharedPreUtil.setParam(App.context, "starTwo", 0);
                SharedPreUtil.setParam(App.context, "starThree", 0);
                SharedPreUtil.setParam(App.context, "starFour", 0);

                finishAllActivity();

            }
        });
        builder.setCancelable(false);
        builder.create().show();
    }


    /**
     * 结束所有Activity
     */
    public static void finishAllActivity()
    {
//        for ( Activity activity : activityStack ) {
//            if ( activity != null ) {
//                activity.finish();
//            }
//        }
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            if ( activity != null ) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

//    protected abstract void back();

    protected abstract void init();
    /**
     * this activity layout res
     * 设置layout布局,在子类重写该方法.
     * @return res layout xml id
     */
    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        // 结束Activity&从栈中移除该Activity
        activityStack.remove(this);
        super.onDestroy();
        Log.v(TAG, "onDestroy...");

    }


    public static void addActivity( Activity activity ) {
        if ( activity != null ) {
            activityStack.add( activity );
        }
    }


    public static void removeActivity( Activity activity ) {
        if ( activity != null ) {
            activityStack.remove( activity );
        }
    }
}
