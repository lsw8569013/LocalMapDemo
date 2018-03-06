package com.map.demo.util;

import android.view.animation.Interpolator;

/**
 * Created by wb-lsw350290 on 2017/12/6.
 */

public class DumpingInterpolator implements Interpolator {


    @Override
    public float getInterpolation(float input) {
        return (float) (1-Math.exp( -3 * input) * Math.cos(10 * input) );
    }
}