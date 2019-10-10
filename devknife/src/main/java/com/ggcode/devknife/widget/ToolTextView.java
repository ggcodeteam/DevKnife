package com.ggcode.devknife.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * <pre>
 * @author : zbb 33775
 * @Date: 2019/2/14 23:26
 * </pre>
 */
public class ToolTextView extends AppCompatTextView {

    private Paint mPaint = new Paint();

    {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#00CC88"));
    }

    public ToolTextView(Context context) {
        this(context, null);
    }

    public ToolTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas c) {
        c.drawCircle(getWidth() / 2, getHeight() / 2,
                Math.min((getWidth() - getPaddingLeft() - getPaddingRight()) / 2,
                        (getHeight() - getPaddingTop() - getPaddingBottom()) / 2), mPaint);
        super.onDraw(c);
    }

    public void setBgColor(@ColorInt int bgColor) {
        mPaint.setColor(bgColor);
        invalidate();
    }
}
