package com.example.storyapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class DecagonImageView extends AppCompatImageView {
    private Path decagonPath;
    private Paint borderPaint;
    private int borderColor = 0xFF000000; // Default border color

    public DecagonImageView(Context context) {
        super(context);
        init();
    }

    public DecagonImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DecagonImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        decagonPath = new Path();

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(15); // Adjust the border width as needed
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int centerX = w / 2;
        int centerY = h / 2;
        int radius = Math.min(w, h) / 2;

        decagonPath.reset();

        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(i * 36); // 360 degrees / 10 sides = 36 degrees per side
            float x = (float) (centerX + radius * Math.cos(angle));
            float y = (float) (centerY + radius * Math.sin(angle));

            if (i == 0) {
                decagonPath.moveTo(x, y);
            } else {
                decagonPath.lineTo(x, y);
            }
        }
        decagonPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(decagonPath);
        super.onDraw(canvas);
        canvas.drawPath(decagonPath, borderPaint);
    }

    public void setBorderColor(int color) {
        borderColor = color;
        borderPaint.setColor(borderColor);
        invalidate();
    }
}
