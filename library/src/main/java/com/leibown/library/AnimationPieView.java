package com.leibown.library;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

/**
 * @author leibown
 *         create at 2016/11/20 下午2:48
 */

public class AnimationPieView extends View {
    // 颜色表 (注意: 此处定义颜色使用的是ARGB，带Alpha通道的)
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    // 宽高
    private int mWidth, mHeight;

    private List<PieData> mData;

    private Paint mPaint = new Paint();

//    private Paint circlePaint = new Paint();

    // 饼状图初始绘制角度
    private float mStartAngle = 0;

    private boolean isAnimation = false;

    private float animationAngle;//执行动画时每个时段的角度

    public AnimationPieView(Context context) {
        this(context, null);
    }

    public AnimationPieView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
//        circlePaint.setStyle(Paint.Style.STROKE);
//        circlePaint.setStrokeWidth(20);
//        circlePaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData == null)
            return;
        float currentStartAngle = mStartAngle;
        canvas.translate(mWidth / 2, mHeight / 2);

        float r = (float) (Math.min(mWidth, mHeight) / 2 * 0.8);

        RectF rectF = new RectF(-r, -r, r, r);

//        circlePaint.setColor(Color.GRAY);
        if (isAnimation) {
            for (int i = 0; i < mData.size(); i++) {
                PieData data = mData.get(i);
                mPaint.setColor(data.getColor());

                if (animationAngle > data.getAngle()) {//如果上次剩余角度大于当前item的经过角度，直接绘制此item
                    canvas.drawArc(rectF, currentStartAngle, data.getAngle(), true, mPaint);//
//                    canvas.drawArc(rectF, currentStartAngle, data.getAngle(), false, circlePaint);
                } else {//如果上次剩余角度小于当前item的经过角度，就绘制剩余角度
                    canvas.drawArc(rectF, currentStartAngle, animationAngle, true, mPaint);//
//                    canvas.drawArc(rectF, currentStartAngle, animationAngle, false, circlePaint);
                }
                animationAngle = animationAngle - data.getAngle();//计算每次剩余的角度
                if (animationAngle < 0)//如果剩余角度小于0，停止绘制
                    break;
                currentStartAngle += data.getAngle();
            }

        } else {
            for (PieData data : mData) {
                mPaint.setColor(data.getColor());
                canvas.drawArc(rectF, currentStartAngle, data.getAngle(), true, mPaint);
                currentStartAngle += data.getAngle();
            }
//            canvas.drawCircle(0, 0, r, circlePaint);
        }

    }


    public void startAnimation(int duration) {
        isAnimation = true;
        ValueAnimator animator = ValueAnimator.ofFloat(0, 360);
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationAngle = (float) animation.getAnimatedValue();
                invalidate();
                if (animationAngle >= 360) {
                    isAnimation = false;
                    invalidate();
                }
            }
        });
        animator.start();
    }


    //设置起始角度
    public void setStartAngle(float startAngle) {
        this.mStartAngle = startAngle;
        invalidate();//刷新
    }

    public void setData(List<PieData> mData) {
        this.mData = mData;
        initData(mData);
        invalidate();//刷新
    }

    private void initData(List<PieData> pieDatas) {
        if (pieDatas == null && pieDatas.size() == 0)
            return;

        float sumValue = 0;//总和

        for (int i = 0; i < pieDatas.size(); i++) {
            PieData data = pieDatas.get(i);
            sumValue += data.getItemSize();

            if (data.getColor() == 0) {//设置颜色
                int j = i % mColors.length;
                data.setColor(mColors[j]);
            }
        }

        for (int i = 0; i < pieDatas.size(); i++) {
            PieData data = pieDatas.get(i);
            float percentage = data.getItemSize() / sumValue;//获取百分比
            float angle = 360 * percentage;//获取角度
            data.setPercentage(percentage);
            data.setAngle(angle);
        }


    }


}
