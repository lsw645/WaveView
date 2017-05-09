package com.lxw.waveview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2017/05/09
 *     desc   :
 * </pre>
 */

public class WaveView extends View {
    private Paint mPaint;
    private Path mPath;
    //波浪的长度
    private int waveLength;
    //波浪的高度
    private int waveHeight;
    //初始高度
    private int orinalY;
    //滑动的小图
    private Bitmap mBitmap;
    //屏幕高度
    private int height;
    //屏幕宽度
    private int width;
    private int waveView_boatBitmap;
    private int duration;
    private ValueAnimator mValueAnimator;
    private int dx;
    private int dy;
    private Region mRegion;

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        init();
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WaveView);
        waveLength = (int) array.getDimension(R.styleable.WaveView_wave_length, 400);
        waveHeight = (int) array.getDimension(R.styleable.WaveView_wave_height, 200);
        waveView_boatBitmap = array.getResourceId(R.styleable.WaveView_boat_bitmap, 0);
        orinalY = (int) array.getDimension(R.styleable.WaveView_originY, 500);
        duration = array.getInteger(R.styleable.WaveView_duration, 2000);
        array.recycle();

        BitmapFactory.Options options = new BitmapFactory.Options();
       // options.inSampleSize = 2;
        if (waveView_boatBitmap > 0) {
            mBitmap = BitmapFactory.decodeResource(getResources(), waveView_boatBitmap, options);
        } else {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher, options);
        }
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.waterColor));
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        width = widthSize;
        height = heightSize;
        setMeasuredDimension(widthSize, heightSize);
        if (orinalY == 0) {
            orinalY = height;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setPath();
        mRegion = new Region();
        Region clip = new Region(width / 2, 0, width / 2 + 1, height);
        mRegion.setPath(mPath, clip);
        Rect rect = mRegion.getBounds();

        canvas.drawPath(mPath, mPaint);

        canvas.drawBitmap(mBitmap, rect.right-mBitmap.getWidth()/2, rect.top-mBitmap.getHeight()/2 , mPaint);
    }

    private void setPath() {
        mPath.reset();

        int halfWaveLength = waveLength / 2;
        mPath.moveTo(-waveLength + dx, orinalY);
        for (int i = -waveLength; i < width + waveLength; i += waveLength) {
            mPath.rQuadTo(halfWaveLength / 2, -waveHeight, halfWaveLength, 0);
            mPath.rQuadTo(halfWaveLength / 2, waveHeight, halfWaveLength, 0);
        }
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();
    }

    public void startAnimation() {
        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setDuration(duration);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = (float) animation.getAnimatedValue();
                dx = (int) (fraction * waveLength);
                postInvalidate();
            }
        });
        mValueAnimator.start();
    }
}
