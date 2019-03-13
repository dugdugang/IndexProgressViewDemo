package com.wislint.downloaddemo.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Path.Op.UNION;

public class IndexProgressView extends View {

    private int mWidth;
    private int mHeight;
    private int mStartX;
    private int mEndX;
    private int mCenterY;
    private int mCenterX;
    private RectF rectF;//进度条矩形
    private Paint mBgPaint;

    private float mProgress;//当前进度值
    private final int mMax = 100;//最大进度
    private final int mOutCircleR = 12;//外圆半径
    private final int mInCircleR = 5;//内圆半径
    private final int mProgressHeight = 10;//进度条高度
    private final String mBackgroundColor = "#E6E6E6";
    private final String mProgressColor = "#179FED";
    private final String mInCircleColor = "#FFFFFF";
    private final String mTextColor = "#FFFFFF";
    private Paint mTextPaint;
    private float mSpaceHeight = 15;
    private float mTriangHeight = 9;
    private float mAreaPaddingHeight = 10;
    private float mAreaPaddingWidth = 10;
    private float mAreaR = 10;
    private float mTriangleWidth = 18;
    private boolean mIsShow = true;//是否显示指示文字区域


    public IndexProgressView(Context context) {
        this(context, null);
    }

    public IndexProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public IndexProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initBgPaint();
        initTextPaint();
    }

    private void initBgPaint() {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);
        mBgPaint.setStyle(Paint.Style.FILL);
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setColor(Color.parseColor(mTextColor));
        mTextPaint.setTextSize(35);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        String drawText = Math.round(mMax) + "%";
        Rect textRect = new Rect();
        mTextPaint.getTextBounds(drawText, 0, drawText.length(), textRect);
        float textMaxWidth = textRect.width();
        mStartX = (int) (textMaxWidth / 2 + mAreaPaddingWidth * 2);
        mEndX = mWidth - mStartX;
        mCenterX = mWidth / 2;
        int minHeight = (int) (textRect.height() + mAreaPaddingHeight * 2 + mTriangHeight + mSpaceHeight + mOutCircleR*2);
        mHeight = minHeight;
        mCenterY = minHeight-mOutCircleR;
        rectF = new RectF(mStartX, mCenterY - (mProgressHeight >> 1), mEndX, mCenterY + (mProgressHeight >> 1));
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画进度条背景
        drawBackground(canvas);
        //画进度条
        drawProgress(canvas);
        //画指示圆点
        drawCircle(canvas);
        //画指示图标
        if (mIsShow) {
            drawIndicatorText(canvas);
        }
    }

    /**
     * 画文字区域指示器
     */
    private void drawIndicatorText(Canvas canvas) {
        //文字大小
        String drawText = Math.round(getRatio() * mMax) + "%";
        Rect textRect = new Rect();
        mTextPaint.getTextBounds(drawText, 0, drawText.length(), textRect);
        float textWidth = textRect.width();
        float textHeight = textRect.height();
        float textX;
        float textY;
        float startX = mStartX + (mEndX - mStartX) * getRatio();
        textX = startX - textWidth / 2;
        textY = mCenterY - mTriangHeight - mSpaceHeight - mAreaPaddingHeight;
        //画三角
        mBgPaint.setColor(Color.parseColor(mProgressColor));
        Path trianglePath = new Path();
        trianglePath.moveTo(startX, mCenterY - mSpaceHeight);
        trianglePath.lineTo(startX + mTriangleWidth / 2, mCenterY - mTriangHeight - mSpaceHeight);
        trianglePath.lineTo(startX - mTriangleWidth / 2, mCenterY - mTriangHeight - mSpaceHeight);
        trianglePath.close();
        //画圆角矩形与三角合并
        Path roundRPath = new Path();
        RectF mAreaRect = new RectF(startX - textWidth / 2 - mAreaPaddingWidth, textY - textHeight - mAreaPaddingHeight, startX + textWidth / 2 + mAreaPaddingWidth, mCenterY - mSpaceHeight - mTriangHeight);
        roundRPath.addRoundRect(mAreaRect, mAreaR, mAreaR, Path.Direction.CW);
        roundRPath.op(trianglePath, UNION);
        canvas.drawPath(roundRPath, mBgPaint);
        //画文字
        canvas.drawText(drawText, textX, textY, mTextPaint);
    }

    /**
     * 画大小圆
     */
    private void drawCircle(Canvas canvas) {
        float startX = mStartX + (mEndX - mStartX) * getRatio();
        mBgPaint.setColor(Color.parseColor(mProgressColor));
        canvas.drawCircle(startX, mCenterY, mOutCircleR, mBgPaint);
        mBgPaint.setColor(Color.parseColor(mInCircleColor));
        canvas.drawCircle(startX, mCenterY, mInCircleR, mBgPaint);
    }

    /**
     * 画当前进度
     */
    private void drawProgress(Canvas canvas) {
        mBgPaint.setColor(Color.parseColor(mProgressColor));
        mBgPaint.setStyle(Paint.Style.FILL);
        Path pPath = new Path();
        pPath.addRoundRect(rectF, mProgressHeight >> 1, mProgressHeight >> 1, Path.Direction.CW);//圆角矩形
        float startX = mStartX + (mEndX - mStartX) * getRatio();
        RectF rRectf = new RectF(startX, mCenterY - (mProgressHeight >> 1), mEndX, mCenterY + (mProgressHeight >> 1));
        Path rPath = new Path();
        rPath.addRect(rRectf, Path.Direction.CW);
        pPath.op(rPath, Path.Op.DIFFERENCE);
        canvas.drawPath(pPath, mBgPaint);
    }

    /**
     * 获取进度比例
     */
    private float getRatio() {
        return (float) (mProgress * 1.0 / mMax);
    }

    /**
     * 画进度条背景
     */
    private void drawBackground(Canvas canvas) {
        mBgPaint.setColor(Color.parseColor(mBackgroundColor));
        canvas.drawRoundRect(rectF, mProgressHeight >> 1, mProgressHeight >> 1, mBgPaint);
    }

    /**
     * 设置进度值
     *
     * @param progress 进度值
     */
    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > mMax) {
            progress = mMax;
        }
        mProgress = progress;
        invalidate();
    }

    /**
     * 设置是否显示文字区域
     *
     * @param isShow 是否
     */
    public void setIsShowIndicator(boolean isShow) {
        mIsShow = isShow;
    }
}
