package com.adhawk.team.travelassist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Anirudh on 12/29/2016.
 */
public class MyMap extends View {
    public static ArrayList<Pair<Integer,Integer>> points = new ArrayList<>();
    public static boolean clearCanvas = false;

    private int cnt_clr = 0;
    private  int paintColor = Color.BLACK;
    private Paint drawPaint;

    public MyMap(Context context, AttributeSet set) {
        super(context,set);
        setFocusable(true);
        setupPaint();
    }

    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Pair<Integer,Integer> p : points){
            drawPaint.setColor(Color.BLACK);
            canvas.drawCircle(p.getFirst(),p.getSecond(),20,drawPaint);
        }
    }

    public void setPoints(ArrayList<Pair<Integer,Integer>> drawPoints){
        points = drawPoints;
    }


}
