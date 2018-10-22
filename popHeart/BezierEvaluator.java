package com.peak.animlib;

import android.animation.TypeEvaluator;
import android.graphics.PointF;
import android.util.Log;

import static com.peak.animlib.PopHeart.TAG;


/**
 * Description:
 * Created by peak on 2018/10/22.
 */
public class BezierEvaluator implements TypeEvaluator<PointF> {
    private PointF mPointF1;
    private PointF mPointF2;

    public BezierEvaluator(PointF pointF1, PointF pointF2) {
        mPointF1 = pointF1;
        mPointF2 = pointF2;
    }

    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        float temp = 1.0F - fraction;
        PointF pointF = new PointF();
        pointF.x = temp * temp * temp * startValue.x
                + 3.0F * temp * temp * fraction * mPointF1.x
                + 3.0F * temp * fraction * fraction * mPointF2.x
                + fraction * fraction * fraction * endValue.x;
        pointF.y = temp * temp * temp * startValue.y
                + 3.0F * temp * temp * fraction * this.mPointF1.y
                + 3.0F * temp * fraction * fraction * this.mPointF2.y
                + fraction * fraction * fraction * endValue.y;
        return pointF;
    }
}
