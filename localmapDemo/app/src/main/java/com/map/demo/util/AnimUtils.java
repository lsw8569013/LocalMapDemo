package com.map.demo.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;

import java.util.ArrayList;

/**
 * Created by wb-lsw350290 on 2017/12/8.
 * 动画工具类
 */
public class AnimUtils {

    /**
     * view 集合的缩放动画
     * @param views
     * @param time
     */
    private void showZGIconAnim(final ArrayList<View> views,int time ) {

        ValueAnimator mAnimator = ValueAnimator.ofFloat(0.2f,1f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                float animatorValue = (float)animation.getAnimatedValue();
                for (View view : views) {
                    view.setScaleX(animatorValue);
                    view.setScaleY(animatorValue);
                }
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);

                for (View view : views) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //view 第一次点击不显示弹出的动画

            }
        });
        //4.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator.setDuration(time);
        mAnimator.setInterpolator(new DumpingInterpolator());
        mAnimator.start();
    }

    /**
     * 单个view 缩放动画
     * @param view
     * @param time
     */
    public static void showZGIconAnim(final View view, int time) {

        ValueAnimator mAnimator = ValueAnimator.ofFloat(0.2f,1f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                float animatorValue = (float)animation.getAnimatedValue();
                    view.setScaleX(animatorValue);
                    view.setScaleY(animatorValue);
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
            }

        });
        //4.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator.setDuration(time);
        mAnimator.setInterpolator(new DumpingInterpolator());
        mAnimator.start();
    }

    public static void showZGIconAnimMouth(final View view, int time) {

        ValueAnimator mAnimator = ValueAnimator.ofFloat(0.5f,1.2f,1.0f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                float animatorValue = (float)animation.getAnimatedValue();
                view.setScaleX(animatorValue);
                view.setScaleY(animatorValue);
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

            }
        });
        //4.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator.setDuration(time);
        mAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        mAnimator.start();
    }

    /**
     *  闪烁动画 就是缩放动画
     *
     * @param iv_myLocation
     */
    private void myLocationAnim(final View iv_myLocation) {
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0.5f, 1.0f);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                float animatorValue = (float) animation.getAnimatedValue();
                iv_myLocation.setScaleX(animatorValue);
                iv_myLocation.setScaleY(animatorValue);
            }
        });
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mAnimator.setDuration(800);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.start();
    }

    /**
     * 平移动画
     *
     * @param view
     */
    public static void translateAnim(final View view, final float startPoint, float endPoint)
    {
        final int i = 500;

        ValueAnimator animator = ValueAnimator.ofFloat(startPoint, endPoint+i);
        animator.setTarget(view);
        animator.setDuration(1000).start();
        // animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                view.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //连续平移需要记住 平移后的绝对位置
//                startPoint += i;
            }
        });
    }

}
