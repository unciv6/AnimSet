package com.peakzhang.helloworld;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CircleLoadingView extends View {

    private Path mDst;
    private Path mPath;
    private Paint mPaint;
    private float mLength;
    private float mAnimValue;

    private int centerX;
    private int centerY;

    private PathMeasure mPathMeasure;

    public CircleLoadingView(Context context) {
        super(context);
    }

    public CircleLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);

        mPath = new Path();
        mDst = new Path();

        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        centerX = getMeasuredWidth() / 2;
        centerY = getMeasuredHeight() / 2;

        mPath.addCircle(centerX, centerY, 100, Path.Direction.CW);
        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, true);

        mLength = mPathMeasure.getLength();

    }

    public CircleLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDst.reset();
        mDst.lineTo(0, 0);

        float stop = mLength * mAnimValue;
        float start = (float) (stop - ((0.5 - Math.abs(mAnimValue - 0.5)) * mLength));

        mPathMeasure.getSegment(start, stop, mDst, true);
        canvas.drawPath(mDst, mPaint);
    }
}
