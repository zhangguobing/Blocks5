package com.bing.blocks5.widget;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import uk.co.senab.photoview.PhotoView;

/**
 * author：zhangguobing on 2017/7/5 14:40
 * email：bing901222@qq.com
 */

public class SmoothImageView extends PhotoView {
    public static final int STATE_NORMAL = 0;
    public static final int STATE_TRANSFORM_IN = 1;
    public static final int STATE_TRANSFORM_OUT = 2;
    private int mOriginalWidth;
    private int mOriginalHeight;
    private int mOriginalLocationX;
    private int mOriginalLocationY;
    private int mState = STATE_NORMAL;
    private Matrix mSmoothMatrix;
    private Bitmap mBitmap;
    private boolean mTransformStart = false;
    private Transform mTransform;
    private final int mBgColor = 0xFF000000;
    private int mBgAlpha = 0;
    private Paint mPaint;

    public SmoothImageView(Context context) {
        super(context);
        init();
    }

    public SmoothImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmoothImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        super.init();
        mSmoothMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setOriginalInfo(int width, int height, int locationX, int locationY) {
        mOriginalWidth = width;
        mOriginalHeight = height;
        mOriginalLocationX = locationX;
        mOriginalLocationY = locationY;
        mOriginalLocationY = mOriginalLocationY - getStatusBarHeight(getContext());
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        java.lang.reflect.Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    public void transformIn() {
        mState = STATE_TRANSFORM_IN;
        mTransformStart = true;
        invalidate();
    }

    public void transformOut() {
        mState = STATE_TRANSFORM_OUT;
        mTransformStart = true;
        invalidate();
    }

    private class Transform {
        float startScale;
        float endScale;
        float scale;
        LocationSizeF startRect;
        LocationSizeF endRect;
        LocationSizeF rect;

        void initStartIn() {
            scale = startScale;
            try {
                rect = (LocationSizeF) startRect.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

        void initStartOut() {
            scale = endScale;
            try {
                rect = (LocationSizeF) endRect.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }

    }

    private void initTransform() {
        if (getDrawable() == null) {
            return;
        }

        if (getDrawable() instanceof ColorDrawable) return;

        if (mBitmap == null || mBitmap.isRecycled()) {
            if (getDrawable() instanceof BitmapDrawable) {
                mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
            } else {
                return;
            }
        }

        if (mTransform != null) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        mTransform = new Transform();

        float xSScale = mOriginalWidth / ((float) mBitmap.getWidth());
        float ySScale = mOriginalHeight / ((float) mBitmap.getHeight());
        float startScale = xSScale > ySScale ? xSScale : ySScale;
        mTransform.startScale = startScale;

        float xEScale = getWidth() / ((float) mBitmap.getWidth());
        float yEScale = getHeight() / ((float) mBitmap.getHeight());
        float endScale = xEScale < yEScale ? xEScale : yEScale;
        mTransform.endScale = endScale;

        mTransform.startRect = new LocationSizeF();
        mTransform.startRect.left = mOriginalLocationX;
        mTransform.startRect.top = mOriginalLocationY;
        mTransform.startRect.width = mOriginalWidth;
        mTransform.startRect.height = mOriginalHeight;

        mTransform.endRect = new LocationSizeF();
        float bitmapEndWidth = mBitmap.getWidth() * mTransform.endScale;
        float bitmapEndHeight = mBitmap.getHeight() * mTransform.endScale;
        mTransform.endRect.left = (getWidth() - bitmapEndWidth) / 2;
        mTransform.endRect.top = (getHeight() - bitmapEndHeight) / 2;
        mTransform.endRect.width = bitmapEndWidth;
        mTransform.endRect.height = bitmapEndHeight;

        mTransform.rect = new LocationSizeF();
    }

    private class LocationSizeF implements Cloneable {
        float left;
        float top;
        float width;
        float height;

        @Override
        public String toString() {
            return "[left:" + left + " top:" + top + " width:" + width + " height:" + height + "]";
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

    }

    private void getBmpMatrix() {
        if (getDrawable() == null) {
            return;
        }
        if (mTransform == null) {
            return;
        }
        if (mBitmap == null || mBitmap.isRecycled()) {
            mBitmap = ((BitmapDrawable) getDrawable()).getBitmap();
        }

        mSmoothMatrix.setScale(mTransform.scale, mTransform.scale);
        mSmoothMatrix.postTranslate(-(mTransform.scale * mBitmap.getWidth() / 2 - mTransform.rect.width / 2),
                -(mTransform.scale * mBitmap.getHeight() / 2 - mTransform.rect.height / 2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }

        if (mState == STATE_TRANSFORM_IN || mState == STATE_TRANSFORM_OUT) {
            if (mTransformStart) {
                initTransform();
            }
            if (mTransform == null) {
                super.onDraw(canvas);
                return;
            }

            if (mTransformStart) {
                if (mState == STATE_TRANSFORM_IN) {
                    mTransform.initStartIn();
                } else {
                    mTransform.initStartOut();
                }
            }

            if (mTransformStart) {
                Log.d("SmoothImageView", "mTransform.startScale:" + mTransform.startScale);
                Log.d("SmoothImageView", "mTransform.startScale:" + mTransform.endScale);
                Log.d("SmoothImageView", "mTransform.scale:" + mTransform.scale);
                Log.d("SmoothImageView", "mTransform.startRect:" + mTransform.startRect.toString());
                Log.d("SmoothImageView", "mTransform.endRect:" + mTransform.endRect.toString());
                Log.d("SmoothImageView", "mTransform.rect:" + mTransform.rect.toString());
            }

            mPaint.setAlpha(mBgAlpha);
            canvas.drawPaint(mPaint);

            int saveCount = canvas.getSaveCount();
            canvas.save();

            getBmpMatrix();
            canvas.translate(mTransform.rect.left, mTransform.rect.top);
            canvas.clipRect(0, 0, mTransform.rect.width, mTransform.rect.height);
            canvas.concat(mSmoothMatrix);
            getDrawable().draw(canvas);
            canvas.restoreToCount(saveCount);
            if (mTransformStart) {
                mTransformStart = false;
                startTransform(mState);
            }
        } else {
            mPaint.setAlpha(255);
            canvas.drawPaint(mPaint);
            super.onDraw(canvas);
        }
    }

    private void startTransform(final int state) {
        if (mTransform == null) {
            return;
        }
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (state == STATE_TRANSFORM_IN) {
            PropertyValuesHolder scaleHolder = PropertyValuesHolder.ofFloat("scale", mTransform.startScale, mTransform.endScale);
            PropertyValuesHolder leftHolder = PropertyValuesHolder.ofFloat("left", mTransform.startRect.left, mTransform.endRect.left);
            PropertyValuesHolder topHolder = PropertyValuesHolder.ofFloat("top", mTransform.startRect.top, mTransform.endRect.top);
            PropertyValuesHolder widthHolder = PropertyValuesHolder.ofFloat("width", mTransform.startRect.width, mTransform.endRect.width);
            PropertyValuesHolder heightHolder = PropertyValuesHolder.ofFloat("height", mTransform.startRect.height, mTransform.endRect.height);
            PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofInt("alpha", 0, 255);
            valueAnimator.setValues(scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder);
        } else {
            PropertyValuesHolder scaleHolder = PropertyValuesHolder.ofFloat("scale", mTransform.endScale, mTransform.startScale);
            PropertyValuesHolder leftHolder = PropertyValuesHolder.ofFloat("left", mTransform.endRect.left, mTransform.startRect.left);
            PropertyValuesHolder topHolder = PropertyValuesHolder.ofFloat("top", mTransform.endRect.top, mTransform.startRect.top);
            PropertyValuesHolder widthHolder = PropertyValuesHolder.ofFloat("width", mTransform.endRect.width, mTransform.startRect.width);
            PropertyValuesHolder heightHolder = PropertyValuesHolder.ofFloat("height", mTransform.endRect.height, mTransform.startRect.height);
            PropertyValuesHolder alphaHolder = PropertyValuesHolder.ofInt("alpha", 255, 0);
            valueAnimator.setValues(scaleHolder, leftHolder, topHolder, widthHolder, heightHolder, alphaHolder);
        }

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public synchronized void onAnimationUpdate(ValueAnimator animation) {
                mTransform.scale = (Float) animation.getAnimatedValue("scale");
                mTransform.rect.left = (Float) animation.getAnimatedValue("left");
                mTransform.rect.top = (Float) animation.getAnimatedValue("top");
                mTransform.rect.width = (Float) animation.getAnimatedValue("width");
                mTransform.rect.height = (Float) animation.getAnimatedValue("height");
                mBgAlpha = (Integer) animation.getAnimatedValue("alpha");
                invalidate();
                ((Activity) getContext()).getWindow().getDecorView().invalidate();
            }
        });
        valueAnimator.addListener(new ValueAnimator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (state == STATE_TRANSFORM_IN) {
                    mState = STATE_NORMAL;
                }
                if (mTransformListener != null) {
                    mTransformListener.onTransformComplete(state);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    public void setOnTransformListener(TransformListener listener) {
        mTransformListener = listener;
    }

    private TransformListener mTransformListener;

    public static interface TransformListener {
        /**
         * @param mode STATE_TRANSFORM_IN 1 ,STATE_TRANSFORM_OUT 2
         */
        void onTransformComplete(int mode);// mode 1
    }
}
