package com.peak.test;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Description:
 * Created by peak on 2018/11/4.
 */
public class ScrollBallLoading extends View {

    private static final int DEFAULT_ONE_BALL_COLOR = Color.parseColor("#40df73");
    private static final int DEFAULT_TWO_BALL_COLOR = Color.parseColor("#ffdf3e");
    private float maxRadius = 75.0F;
    private float minRadius = 25.0F;
    private int distance = 100;

    private Ball mLeft;
    private Ball mRight;
    private float mCenterX;
    private float mCenterY;

    private Paint mPaint;
    private AnimatorSet animatorSet;

    public ScrollBallLoading(Context context) {
        this(context, null);
    }

    public ScrollBallLoading(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollBallLoading(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLeft = new Ball();
        mLeft.setColor(DEFAULT_ONE_BALL_COLOR);
        mRight = new Ball();
        mRight.setColor(DEFAULT_TWO_BALL_COLOR);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        configAnimator();
    }

    private void configAnimator() {
        float f = (this.maxRadius + this.minRadius) * 0.5F;
        ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(mLeft, "radius", new float[]{f, this.maxRadius, f, this.minRadius, f});
        localObjectAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        ValueAnimator localValueAnimator1 = ValueAnimator.ofFloat(new float[]{-1.0F, 0.0F, 1.0F, 0.0F, -1.0F});
        localValueAnimator1.setRepeatCount(ValueAnimator.INFINITE);
        localValueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator) {
                float f1 = ((Float) paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
                float f2 = ScrollBallLoading.this.mCenterX;
                float f3 = ScrollBallLoading.this.distance;
                mLeft.setCenterX(f2 + f3 * f1);
                ScrollBallLoading.this.invalidate();
            }
        });
        ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(mRight, "radius", new float[]{f, this.minRadius, f, this.maxRadius, f});
        localObjectAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        ValueAnimator localValueAnimator2 = ValueAnimator.ofFloat(new float[]{1.0F, 0.0F, -1.0F, 0.0F, 1.0F});
        localValueAnimator2.setRepeatCount(ValueAnimator.INFINITE);
        localValueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator) {
                float f1 = ((Float) paramAnonymousValueAnimator.getAnimatedValue()).floatValue();
                float f2 = ScrollBallLoading.this.mCenterX;
                float f3 = ScrollBallLoading.this.distance;
                mRight.setCenterX(f2 + f3 * f1);
            }
        });
        this.animatorSet = new AnimatorSet();
        this.animatorSet.playTogether(new Animator[]{localObjectAnimator1, localValueAnimator1, localObjectAnimator2, localValueAnimator2});
        this.animatorSet.setDuration(1000L);
        this.animatorSet.setInterpolator(new LinearInterpolator());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mLeft.getRadius() > this.mRight.getRadius()) {
            this.mPaint.setColor(this.mRight.getColor());
            canvas.drawCircle(this.mRight.getCenterX(), this.mCenterY, this.mRight.getRadius(), this.mPaint);
            this.mPaint.setColor(this.mLeft.getColor());
            canvas.drawCircle(this.mLeft.getCenterX(), this.mCenterY, this.mLeft.getRadius(), this.mPaint);
            return;
        }
        this.mPaint.setColor(this.mLeft.getColor());
        canvas.drawCircle(this.mLeft.getCenterX(), this.mCenterY, this.mLeft.getRadius(), this.mPaint);
        this.mPaint.setColor(this.mRight.getColor());
        canvas.drawCircle(this.mRight.getCenterX(), this.mCenterY, this.mRight.getRadius(), this.mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mCenterX = (getWidth() / 2);
        this.mCenterY = (getHeight() / 2);
    }


    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mCenterX = (w / 2);
        this.mCenterY = (h / 2);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimator();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimator();
    }

    public void startAnimator() {
        if (getVisibility() != VISIBLE) {
            return;
        }
        while ((this.animatorSet.isRunning()) || (this.animatorSet == null)) {
            return;
        }
        animatorSet.start();
    }

    public void stopAnimator() {
        if (animatorSet != null) {
            animatorSet.end();
        }
    }

    public class Ball {
        private float centerX;
        private float radius;
        private int color;

        public float getCenterX() {
            return centerX;
        }

        public void setCenterX(float centerX) {
            this.centerX = centerX;
        }

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
