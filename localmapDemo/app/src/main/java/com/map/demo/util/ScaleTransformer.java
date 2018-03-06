package com.map.demo.util;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * squareFragment 详情页scale控制
 * Created by Administrator on 2017/1/11.
 */
public class ScaleTransformer implements ViewPager.PageTransformer {
    public static final float MAX_SCALE = 1.00f;
    public static final float MIN_SCALE = 0.84f;
    /**核心就是实现transformPage(View page, float position)这个方法**/
    @Override
    public void transformPage(View page, float position) {

        if (position < -1) {
            position = -1;
        } else if (position > 1) {
            position = 1;
        }

        float tempScale = position < 0 ? 1 + position : 1 - position;

        float slope = (MAX_SCALE - MIN_SCALE) / 1;//0.2f
//        Log.i("position=",""+position);
//        Log.i("tempScale=",""+tempScale);
//        Log.i("slope=",""+slope);
        //一个公式
        float scaleValue = MIN_SCALE + tempScale * slope;
//        Log.i("scaleValue=",""+scaleValue);
        page.setScaleX(scaleValue);
        page.setScaleY(scaleValue);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }
    
}
