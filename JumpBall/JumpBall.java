package com.peak.window;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;


public class JumpBall extends View {

    private int mColor = Color.GRAY;//ball default color
    private int mShadowColor = Color.parseColor("#3F3B2D");
    private float mDensity;
    private int mStartX;
    private int mStartY;
    private int mEndY;
    private int mCurrentY;
    private int mRadius = 10;
    private RectF mRectF;
    private Paint mPaint;

    public JumpBall(Context context) {
        this(context, null);
    }

    public JumpBall(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JumpBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = getResources().getDisplayMetrics().density;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mStartX = getWidth() / 2;
        mEndY = getHeight() / 2;
        mStartY = mEndY * 5 / 6;
        mPaint.setColor(mColor);
        if (mCurrentY == 0) {
            playAnimator();
        } else {
            drawCircle(canvas);
            drawShader(canvas);
        }
    }

    private void playAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(new int[]{mStartY, mEndY});
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animator) {
                JumpBall.this.mCurrentY = (Integer) animator.getAnimatedValue();
                JumpBall.this.invalidate();
            }
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator(1.2F));
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.setDuration(800L);
        valueAnimator.start();
    }

    private void drawShader(Canvas canvas) {
        int start = mStartY;
        int end = mEndY;
        float progress = (float) ((double) (mCurrentY - mStartY) * 1.0D / (double) (end - start));
        if ((double) progress > 0.3D) {
            end = (int) ((float) mRadius * progress * mDensity);
            mPaint.setColor(mShadowColor);
            mRectF = new RectF((float) (mStartX - end), (float) (mEndY + 10), (float) (mStartX + end), (float) (mEndY + 15));
            canvas.drawOval(mRectF, mPaint);
        }
    }

    private void drawCircle(Canvas canvas) {
        if (mEndY - mCurrentY > 10) {
            canvas.drawCircle((float) mStartX, (float) mCurrentY, (float) mRadius * mDensity, mPaint);
        } else {
            mRectF = new RectF((float) mStartX - (float) mRadius * mDensity - 2.0F,
                    (float) mCurrentY - (float) mRadius * mDensity + 5.0F,
                    (float) mStartX + (float) mRadius * mDensity + 2.0F,
                    (float) mCurrentY + (float) mRadius * mDensity);
            canvas.drawOval(mRectF, mPaint);
        }
    }

    public void setColor(int color) {
        mColor = color;
    }
}
