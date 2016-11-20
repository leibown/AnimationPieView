package com.leibown.animationpieview;

/**
 * 饼状图单个item类
 * @author leibown
 * create at 2016/11/20 下午2:41
 */

public class PieData {

    private float itemSize;//每个item的大小，不是百分比，根据每个item大小自动算出总和

    private int color = 0;//每个Item的颜色，可以为空


    private float percentage;   // 百分比 不需要设置
    private float angle = 0;    // 角度   不需要设置

    public PieData(float itemSize, int color) {
        this.itemSize = itemSize;
        this.color = color;
    }

    public float getItemSize() {
        return itemSize;
    }

    public void setItemSize(float itemSize) {
        this.itemSize = itemSize;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
