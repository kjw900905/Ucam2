package com.example.Activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.Beans.Variable;

public class DrawTimeTable extends View {
    public DrawTimeTable(Context context) {
        super(context);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float scaleX = canvas.getWidth() / 1080f;
        float scaleY = canvas.getHeight() / 1920f;
        canvas.scale(scaleX, scaleY);

        Paint paint = new Paint();

        for(int x = 0; x < Variable.m_TIME.length + 1; x++) {
            for(int y = 0; y < Variable.m_DAY.length + 1; y++) {
                paint.setStyle(Paint.Style.STROKE); // 외곽선 그리기
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(3);
                paint.setTextSize(30);

                //canvas.drawRect();
            }
        }
    }
}
