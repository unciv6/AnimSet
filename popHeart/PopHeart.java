package com.peak.animlib;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.util.Random;


/**
 * Description:
 * Created by peak on 2018/10/22.
 */
public class PopHeart extends RelativeLayout {
    private static final String TAG = "PopHeart";

    private Interpolator acc = new AccelerateInterpolator();
    private Interpolator accdec = new AccelerateDecelerateInterpolator();
    private Interpolator dec = new DecelerateInterpolator();
    private Interpolator line = new LinearInterpolator();
    private Interpolator[] mInterpolators;

    private int mHeight;
    private int mWidth;
    private Drawable[] mDrawables;
    private LayoutParams mLayoutParams;
    private Random random = new Random();

    private int dHeight;
    private int dWidth;

    public PopHeart(Context context) {
        this(context, null);
    }

    public PopHeart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopHeart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDrawables = new Drawable[3];
        Drawable one = getResources().getDrawable(R.drawable.pl_blue);
        Drawable two = getResources().getDrawable(R.drawable.pl_red);
        Drawable three = getResources().getDrawable(R.drawable.pl_yellow);
        mDrawables[0] = one;
        mDrawables[1] = two;
        mDrawables[2] = three;
        dHeight = one.getIntrinsicHeight();
        dWidth = one.getIntrinsicWidth();
        mLayoutParams = new LayoutParams(dWidth, dHeight);
        mLayoutParams.addRule(ALIGN_PARENT_BOTTOM, -1);
        mLayoutParams.addRule(CENTER_HORIZONTAL, -1);
        mInterpolators = new Interpolator[4];
        mInterpolators[0] = line;
        mInterpolators[1] = acc;
        mInterpolators[2] = dec;
        mInterpolators[3] = accdec;
    }

    public void addHeart() {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageDrawable(mDrawables[this.random.nextInt(3)]);
        imageView.setLayoutParams(mLayoutParams);
        addView(imageView);
        Animator animator = getAnimator(imageView);
        animator.addListener(new AnimEndListener(imageView));
        animator.start();
    }

    private Animator getAnimator(View view) {
        AnimatorSet animatorSet = getEnterAnimtor(view);
        ValueAnimator valueAnimator = getBezierValueAnimator(view);
        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animatorSet);
        animSet.playSequentially(animatorSet, valueAnimator);
        animSet.setInterpolator(mInterpolators[random.nextInt(4)]);
        animSet.setTarget(view);
        return animSet;
    }

    private AnimatorSet getEnterAnimtor(View view) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.2F, 1.0F);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.2F, 1.0F);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.2F, 1.0F);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500L);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(alpha, scaleX, scaleY);
        animatorSet.setTarget(view);
        return animatorSet;
    }

    private ValueAnimator getBezierValueAnimator(View view) {
        ValueAnimator animator = ValueAnimator.ofObject(new BezierEvaluator(getPointF(2),
                getPointF(1)), new PointF((float) ((this.mWidth - this.dWidth) / 2),
                (float) (this.mHeight - this.dHeight)), new PointF((float) this.random.nextInt(this.getWidth()), 0.0F));
        animator.addUpdateListener(new BezierListener(view));
        animator.setTarget(view);
        animator.setDuration(3000L);
        return animator;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    private PointF getPointF(int i) {
        PointF pointF = new PointF();
        pointF.x = (float) random.nextInt(mWidth - 100);
        pointF.y = (float) (random.nextInt(mHeight - 100) / i);
        return pointF;
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            PopHeart.this.removeView(target);
        }
    }


    private class BezierListener implements AnimatorUpdateListener {
        private View target;

        public BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            target.setAlpha(1.0f - animation.getAnimatedFraction());
        }
    }
}
