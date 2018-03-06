package com.map.demo.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

/**
 * Created by wb-lsw350290 on 2017/12/13.
 */

public class ImageViewTouchLisenter implements View.OnTouchListener {
    private  TextView tv_recommed_title;
    private float mStartY;
    private boolean isTouched;
    private ViewGroup.MarginLayoutParams mParams;
    private int top;
    private int height;
    private float mStartX;

    public ImageViewTouchLisenter(TextView tv_recommed_title) {
        super();
        this.tv_recommed_title = tv_recommed_title;
    }

    @Override
    public boolean onTouch(final View view, MotionEvent motionEvent) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        top = location[1];
        height = view.getHeight();
        mParams = (ViewGroup.MarginLayoutParams)view.getLayoutParams();

        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                isTouched = true;
                mStartY = motionEvent.getY();
                mStartX = motionEvent.getX();
                LogUtils.e("图片手势yACTION_DOWN---"+mStartY);
                return true;


            case MotionEvent.ACTION_MOVE:
                float  y = motionEvent.getY();

//                LogUtils.e("图片手势ACTION_MOVEy---"+y);
//                if(y < mStartY){
//                    mParams.bottomMargin  =  mParams.bottomMargin +(int) (mStartY - y);
//                    view.setLayoutParams(mParams);
//
//                }
//                view.setLayoutParams(mParams);
                break;
            case MotionEvent.ACTION_UP:
                float nowY = motionEvent.getY();
                float nowX = motionEvent.getX();

                int deltaY = (int)nowY - (int)mStartY;
//                int deltaX = nowX - mStartX;
                
                if (isTouched /*&& Math.abs(deltaX) < 5*/ && Math.abs(deltaY) < 5){
                    view.setVisibility(View.GONE);
                    tv_recommed_title.setVisibility(View.GONE);
                }

                if (isTouched  && Math.abs(deltaY) > 10){
                    TranslateAnimation animation = new TranslateAnimation(0.0f, 0,0,-height );

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.setVisibility(View.GONE);
                            tv_recommed_title.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });

                    animation.setDuration(300);
                    view.startAnimation(animation);

                    break;
                }
                isTouched = false;
        }
        return false ;
    }
}
