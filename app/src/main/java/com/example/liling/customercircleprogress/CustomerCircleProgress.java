package com.example.liling.customercircleprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import org.xclcharts.common.MathHelper;

import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * Created by park on 15/9/14.
 */
public class CustomerCircleProgress extends View {
    private final int EVENT_END_PROGRESS = 1;
    private Timer timer;
    /**
     * 从105度开始画弧
     */
    private final int mStartAngle = 108;
    /**
     * 从90度开始画弧
     */
    private final int mArcStartAngle = 90;
    /**
     * 总共画325度的弧线
     */
    private final int mTotalAngle = 360;
    /**
     * 完成的格子数
     */
    private int mCompletedLineCount;
    /**
     * 动画过程中，累加的完成的格子数
     */
    private int mCompletedLineCountPer;
    /**
     * 小圆中的短线的个数，默认为66个
     */
    private int mShortLineCount = 66;
    /**
     * 外边距
     */
    private int mMarggin;
    /**
     * 大圆的半径
     */
    private float mBigCircleRadius;
    /**
     * 小圆外圈的半径
     */
    private float mSmallCircleOutRadius;
    /**
     * 小圆内圈的半径
     */
    private float mSmallCircleInRadius;
    /**
     * 小圆圈短线的长度
     */
    private int mShortLineWidth;
    /**
     * 大圆内部到小圆外边缘的距离
     */
    private int mPaddingInBigCircle;
    /**
     * 灰色画笔
     */
    private Paint mGrayPaint = null;
    /**
     * 红色画笔
     */
    private Paint mRedPaint = null;
    /**
     * 内部短线的灰色画笔
     */
    private Paint mShortLineGrayPaint = null;
    /**
     * 内部短线的红色画笔
     */
    private Paint mShortLineRedPaint = null;
    /**
     * 整个view的矩形框，用来画弧线
     */
    private RectF oval = null;

    /**
     * 构造器
     * @param context context
     */
    public CustomerCircleProgress(Context context) {
        super(context);
        init();
    }

    /**
     * 构造器
     * @param context context
     * @param attrs attrs
     */
    public CustomerCircleProgress(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    /**
     * 构造器
     * @param context context
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     */
    public CustomerCircleProgress(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mMarggin = PaintUtils.dip2px(getContext(), 10);
        mShortLineWidth = PaintUtils.dip2px(getContext(), 15);
        mPaddingInBigCircle = PaintUtils.dip2px(getContext(), 15);

        mGrayPaint = new Paint();
        mGrayPaint.setColor(Color.parseColor("#dfdfdf"));
        mGrayPaint.setStyle(Paint.Style.STROKE);
        mGrayPaint.setStrokeWidth(PaintUtils.dip2px(getContext(), 5));

        mRedPaint = new Paint();
        mRedPaint.setColor(Color.parseColor("#ff2626"));
        mRedPaint.setStyle(Paint.Style.STROKE);
        mRedPaint.setStrokeWidth(PaintUtils.dip2px(getContext(), 4));

        mShortLineGrayPaint = new Paint();
        mShortLineGrayPaint.setColor(Color.parseColor("#dfdfdf"));
        mShortLineGrayPaint.setStyle(Paint.Style.STROKE);
        mShortLineGrayPaint.setStrokeWidth(PaintUtils.dip2px(getContext(), 3));

        mShortLineRedPaint = new Paint();
        mShortLineRedPaint.setColor(Color.parseColor("#ff2626"));
        mShortLineRedPaint.setStyle(Paint.Style.STROKE);
        mShortLineRedPaint.setStrokeWidth(PaintUtils.dip2px(getContext(), 3));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mBigCircleRadius = getMeasuredWidth() < getMeasuredHeight() ?
                getMeasuredWidth() / 2  - mMarggin: getMeasuredHeight() / 2 - mMarggin;
        mSmallCircleOutRadius = mBigCircleRadius - mPaddingInBigCircle;
        mSmallCircleInRadius = mSmallCircleOutRadius - mShortLineWidth;

        //圆心
        float mCircleX = mBigCircleRadius + mMarggin;
        float mCircleY = mBigCircleRadius + mMarggin;

        //画灰色的弧线
        canvas.drawCircle(mCircleX, mCircleY, mBigCircleRadius, mGrayPaint);

        if (mCompletedLineCountPer >= mShortLineCount) {
            canvas.drawCircle(mCircleX, mCircleY, mBigCircleRadius, mRedPaint);
        }
        else if (mCompletedLineCountPer > 0) {
            //总共66个短线，65个格子，325度，每个格子是5度
            int mCompletedAngle = mCompletedLineCountPer * 5 + 18;

            //画红色的弧线
            oval = new RectF(mMarggin, mMarggin, mBigCircleRadius * 2 + mMarggin, mBigCircleRadius * 2 + mMarggin);
            canvas.drawArc(oval, mArcStartAngle, mCompletedAngle, false, mRedPaint);
        }

        for (int i = 0; i < mShortLineCount; i++){
            //获取短线外边的坐标点
            MathHelper.getInstance().calcArcEndPointXY(mCircleX, mCircleY, mSmallCircleOutRadius, mStartAngle + 5 * i);

            float startX = MathHelper.getInstance().getPosX();
            float startY = MathHelper.getInstance().getPosY();

            //获取短线里边的坐标点
            MathHelper.getInstance().calcArcEndPointXY(mCircleX, mCircleY, mSmallCircleInRadius, mStartAngle + 5 * i);

            float stopX = MathHelper.getInstance().getPosX();
            float stopY = MathHelper.getInstance().getPosY();

            //画短线，如果完成的则划红线，没完成画灰线
            if (mCompletedLineCountPer != 0 && i <= mCompletedLineCountPer){
                canvas.drawLine(startX, startY, stopX, stopY, mShortLineRedPaint);
            }
            else {
                canvas.drawLine(startX, startY, stopX, stopY, mShortLineGrayPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { // fill_parent
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { // wrap_content
            result = Math.min(result, specSize);
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) { // fill_parent
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) { // wrap_content
            result = Math.min(result, specSize);
        }
        return result;
    }

    /**
     * 更新进度
     * @param progress 要设置进度
     */
    public void setProgress(int progress){
        setProgressWithAnimation(progress, 0);
    }

    /**
     * 更新进度，并设置显示过渡动画
     * @param progress 进度
     * @param duration 动画时长，单位为ms
     */
    public void setProgressWithAnimation(int progress, long duration){
        if (progress > 100) {
            progress = 100;
        }
        mCompletedLineCount = mShortLineCount * progress / 100;

        //如果完成度为0，则直接刷新UI
        //如果duration小于完成的格子数，则说明动画时间很短，直接刷新UI，不做动画
        if (mCompletedLineCount == 0 || duration < mCompletedLineCount) {
            mCompletedLineCountPer = mCompletedLineCount;
            invalidate();
            return;
        }

        mCompletedLineCountPer = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mCompletedLineCountPer < mCompletedLineCount) {
                    postInvalidate();
                    mCompletedLineCountPer++;
                }
                else {
                    mHandler.sendEmptyMessage(EVENT_END_PROGRESS);
                }
            }
        }, 0, duration / mCompletedLineCount);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_END_PROGRESS:
                    timer.cancel();
                    break;
                default:
                    break;
            }
        }
    };
}
