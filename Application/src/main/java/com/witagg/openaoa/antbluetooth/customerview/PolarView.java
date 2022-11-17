//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: MIT
//

package com.witagg.openaoa.antbluetooth.customerview;

import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

import com.witagg.openaoa.antbluetooth.R;
import com.witagg.openaoa.antbluetooth.tools.SharePreferenceUtil;
import com.witagg.openaoa.antbluetooth.adpter.PointD;


public class PolarView extends View {
    private SharePreferenceUtil sharePreferenceUtil;
    private int count = 12;
    private int layerCount = 4;
    private float angle;
    private int centerX;
    private int centerY;
    private float radius;
    private Paint circelPaint;
    private Paint linePaint;
    private Paint pathLinePaint;
    private Paint txtPaint;
    private Paint pointCirclePaint;
    private String[] titles = {"0°", "330°", "300°", "270°", "240°","210°", "180°", "150°", "120°", "90°", "60°", "30°"};//文字
    private double rad_elevation;
    private double dist;
    private List<PointD> pointDList;
    private Path locationPath;
    private double kalman_x_p = 1.0;
    private double kalman_y_p = 1.0;
    public PolarView(Context context, AttributeSet attrs) {

        super(context, attrs);
        sharePreferenceUtil = new SharePreferenceUtil(context);
        initPaint();
    }

    public PolarView(Context context) {

        super(context);
        sharePreferenceUtil = new SharePreferenceUtil(context);
        initPaint();
    }

    public PolarView(Context context,  AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        sharePreferenceUtil = new SharePreferenceUtil(context);
        initPaint();

    }

    private  void initPaint() {

        angle = (float) (Math.PI * 2 / count);

        circelPaint = new Paint();
        circelPaint.setAntiAlias(true);
        circelPaint.setStyle(Paint.Style.STROKE);
        circelPaint.setStrokeWidth(2);


        linePaint = new Paint();
        linePaint.setColor(getResources().getColor(R.color.light_gray));
        linePaint.setAntiAlias(true);
        ;
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2f);

        pathLinePaint = new Paint();
        pathLinePaint.setColor(getResources().getColor(R.color.color_green));
        pathLinePaint.setAntiAlias(true);
        ;
        pathLinePaint.setStyle(Paint.Style.STROKE);
        pathLinePaint.setStrokeWidth(4f);

        txtPaint = new Paint();

        txtPaint.setAntiAlias(true);
        txtPaint.setStyle(Paint.Style.STROKE);
        txtPaint.setTextSize(30);

        pointCirclePaint = new Paint();

        pointCirclePaint.setAntiAlias(true);

        pointCirclePaint = new Paint();
        pointCirclePaint.setStyle(Paint.Style.FILL);
        pointCirclePaint.setAntiAlias(true);
        layerCount = 12;
        angle = (float) (Math.PI * 2 / layerCount);

        pointDList = new ArrayList<>();
        locationPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCircel(canvas);//draw circel
        drawLines(canvas);//draw line
        drawText(canvas);//draw text
        canvas.drawPath(locationPath, pathLinePaint);
        if (pointDList.size() > 0) {
            PointD lastPointD = pointDList.get(pointDList.size()-1);
            double y_r = lastPointD.getDist()*radius/4;

            double y = Math.cos(lastPointD.getRad())*y_r;
            double x = Math.sin(lastPointD.getRad())*y_r;

            float x_real1 = (float)(-x + centerX);
            float y_real1 = (float)(-y + centerY);
            canvas.drawCircle(x_real1, y_real1, 10, pointCirclePaint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = Math.min(h, w) / 2 * 0.83f;
        centerX = w / 2;
        centerY = h / 2 - 350;
    }

    private void drawCircel(Canvas canvas) {

        float r = radius / 4;
        for (int i = 1; i <= 4; i++) {
            float curR = r * i; //current layer Circle radius
            for (int j = 0; j < count; j++) {
                if (j == 0) {

                    float x = (float) (centerX + Math.sin(-angle/2 ) * curR);
                    float y = (float) (centerY - Math.cos(-angle/2 ) * curR);
                    String txt = String.valueOf(i) + "h";
                    float width = txtPaint.measureText(txt);
                    canvas.drawText(txt, x - width/2 , y , txtPaint);
                    circelPaint.setColor(getResources().getColor(R.color.black));
                } else {
                    circelPaint.setColor(getResources().getColor(R.color.light_gray));
                }
                canvas.drawCircle(centerX, centerY, curR, circelPaint);
            }

            if (i == 4) {
                for (int j = 0; j < count; j++) {
                    float x = (float) (centerX + Math.sin(angle * j) * (curR + 12));
                    float y = (float) (centerY - Math.cos(angle * j) * (curR + 12));
                    canvas.drawCircle(x, y, 4, pointCirclePaint);
                }
            }

        }
    }

    private void drawLines(Canvas canvas) {
        float r = radius / layerCount;
        for (int i = 0; i < layerCount; i++) {

            float startX = centerX ;
            float startY = centerY ;

            float endX = (float) (centerX + Math.sin(angle * i) * radius);
            float endY = (float) (centerY - Math.cos(angle * i) * radius);
            canvas.drawLine(startX, startY, endX, endY, linePaint);
        }
    }

    private void drawText(Canvas canvas) {
        for (int i = 0; i < layerCount; i++) {

            float x = (float) (centerX + Math.sin(angle * i) * (radius + 12));
            float y = (float) (centerY - Math.cos(angle * i) * (radius + 12));
            if (angle * i == 0) {

                txtPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(titles[i], x, y - 18, txtPaint);
                txtPaint.setTextAlign(Paint.Align.LEFT);
            } else if (angle * i > 0 && angle * i < Math.PI / 2) {

                canvas.drawText(titles[i], x + 18, y + 10, txtPaint);
            } else if (angle * i >= Math.PI / 2 && angle * i < Math.PI) {

                String txt = titles[i];
                Rect bounds = new Rect();
                txtPaint.getTextBounds(txt, 0, txt.length(), bounds);
                float height = bounds.bottom - bounds.top;
                float width = txtPaint.measureText(txt);
                canvas.drawText(txt, x - width * 0.4f, y + height + 18, txtPaint);
            } else if (angle * i >= Math.PI && angle * i < 3 * Math.PI / 2) {

                String txt = titles[i];
                Rect bounds = new Rect();
                txtPaint.getTextBounds(txt, 0, txt.length(), bounds);
                float width = txtPaint.measureText(txt);
                float height = bounds.bottom - bounds.top;
                canvas.drawText(txt, x - width * 0.6f, y + height + 18, txtPaint);
            } else if (angle * i >= 3 * Math.PI / 2 && angle * i < 2 * Math.PI) {

                String txt = titles[i];
                float width = txtPaint.measureText(txt);
                canvas.drawText(txt, x - width - 18, y + 10, txtPaint);
            }

        }
    }
    // kalman algorithm
    private PointD kalman_calc(double rad, double dist, double rad_prev, double dist_prev) {
        PointD pointD = new PointD();
        int kalman_R = 0;
        int progress = sharePreferenceUtil.getKalmanStrength();
        if (progress == 0) {
            pointD.setDist(dist);
            pointD.setRad(rad);
            return  pointD;
        } else if (progress == 1) {
            kalman_R = 10;
        } else if (progress == 2) {
            kalman_R = 50;
        } else if (progress == 3) {
            kalman_R = 100;
        } else if (progress == 4) {
            kalman_R = 250;
        } else if (progress == 5) {
            kalman_R = 500;
        } else {
            kalman_R = 1000;
        }
        double x = dist * Math.cos(rad);
        double y = dist * Math.sin(rad);
        double x_prev = dist_prev * Math.cos(rad_prev);
        double y_prev = dist_prev * Math.sin(rad_prev);
        double kalman_Q = 1.0;
        kalman_x_p = kalman_x_p + kalman_Q;
        kalman_y_p = kalman_y_p + kalman_Q;
        double kalman_gain_x = kalman_x_p / (kalman_x_p + kalman_R);
        double kalman_gain_y = kalman_y_p / (kalman_y_p + kalman_R);
        double x_delta = kalman_gain_x * (x - x_prev);
        double y_delta = kalman_gain_y * (y - y_prev);
        double x_kalman = x_delta + x_prev;
        double y_kalman = y_delta + y_prev;
        pointD.setRad(Math.atan2(y_kalman, x_kalman));
        pointD.setDist(Math.sqrt(x_kalman*x_kalman + y_kalman*y_kalman));

        return  pointD;
    }

    public void setPoint(double rad_elevation, double distance) {
        this.rad_elevation = rad_elevation;
        this.dist = distance;

        double dist = 1/Math.tan(Math.toRadians(this.dist));
        double rad = Math.toRadians(this.rad_elevation);
        PointD pointD = new PointD();
        if (pointDList.size() > 0) {
            PointD lastPointD = pointDList.get(pointDList.size()-1);
            pointD = kalman_calc(rad, dist, lastPointD.getRad(), lastPointD.getDist());
        } else {
            pointD.setRad(rad);
            pointD.setDist(dist);
        }

        if (pointD.getDist() > 4) {
            pointD.setDist(4.0);
        }
        int progress = sharePreferenceUtil.getTailLength();
        while (pointDList.size() >= progress) {
            pointDList.remove(0);
        }
        pointDList.add(pointD);

        locationPath.reset();
        for(int i = 0; i < pointDList.size(); i++) {
            PointD lastPointD = pointDList.get(i);
            double y_r = lastPointD.getDist()*radius/4;

            double y = Math.cos(lastPointD.getRad())*y_r;
            double x = Math.sin(lastPointD.getRad())*y_r;

            float x_real = (float)(-x + centerX);
            float y_real = (float)(-y + centerY);
            if (i == 0) {
                locationPath.moveTo(x_real, y_real);
            } else {
                locationPath.lineTo(x_real, y_real);
            }
        }

        invalidate();
    }



}
