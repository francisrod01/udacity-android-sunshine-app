package com.example.android.sunshine.app.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.android.sunshine.app.R;

/**
 * Created by Francis Rodrigues on 4/9/17.
 */

public class CompassView extends View {

    private float direction;

    public CompassView(Context context) {
        super(context);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CompassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int r;
        if (w > h) {
            r = h / 2;
        } else {
            r = w / 2;
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.GRAY);

        canvas.drawCircle((w / 2), (h / 2), r, paint);

        paint.setColor(getResources().getColor(R.color.sunshine_dark_blue));
        canvas.drawLine((w / 2), (h / 2), (float) ((w / 2) + r * Math.sin(-direction)),
                (float) ((h / 2) - r * Math.cos(-direction)), paint);
    }

    public void update(float dir) {
        direction = dir;
//        direction = dir * ((float) Math.PI / 180);

        // Call invalidate to force drawing on page.
        invalidate();
    }
}
