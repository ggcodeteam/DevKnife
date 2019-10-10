package com.ggcode.devknife.knife.ui.colorpicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.ggcode.devknife.R;
import com.ggcode.devknife.utils.ColorUtil;
import com.ggcode.devknife.utils.UIUtils;

public class ColorPickerView extends View {

    private Paint mRingPaint;
    private Paint mBitmapPaint;
    private Paint mFocusPaint;
    private Paint mGridPaint;
    private Paint mGridShadowPaint;
    private TextPaint mTextPaint;
    private Path mClipPath = new Path();
    private Matrix mBitmapMatrix = new Matrix();
    private Bitmap mCircleBitmap;
    private String mInfo;
    private RoundedBitmapDrawable mGridDrawable;

    private int mPixelInterval;
    private int mDiameterPixelNumber;

    private int mRingSize;
    private int mFocusOffsetX;
    private int mFocusOffsetY;
    private float mTextVOffset;
    private float mTextHOffset;
    private int mBitmapSize;

    public ColorPickerView(Context context) {
        super(context);
        init();
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int resolveSize = resolveSize(mPixelInterval * mDiameterPixelNumber + mRingSize * 2, widthMeasureSpec);
        setMeasuredDimension(resolveSize, resolveSize);
    }

    private void init() {
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(Color.WHITE);
        mRingPaint.setStyle(Paint.Style.STROKE);

        mFocusPaint = new Paint();
        mFocusPaint.setAntiAlias(true);
        mFocusPaint.setStyle(Paint.Style.STROKE);
        mFocusPaint.setStrokeWidth(3f);
        mFocusPaint.setColor(Color.BLACK);

        mBitmapPaint = new Paint();
        mBitmapPaint.setFilterBitmap(false);

        mGridPaint = new Paint();
        mGridPaint.setStrokeWidth(1f);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setColor(getResources().getColor(R.color.dk_color_CCCCCC));
        mGridShadowPaint = new Paint(mGridPaint);
        mGridPaint.setColor(getResources().getColor(R.color.dk_color_444444));

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTypeface(Typeface.MONOSPACE);
        mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.dk_text_size_12));

        mPixelInterval = UIUtils.dp2px(6);
        mDiameterPixelNumber = mPixelInterval * 2;
        mRingSize = UIUtils.dp2px(13);
        FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextVOffset = -(mRingSize - (fontMetrics.descent - fontMetrics.ascent) / 2) / 2;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmapSize = getWidth() - mRingSize * 2;

        mDiameterPixelNumber = mBitmapSize / mPixelInterval;
        mTextHOffset = (float) (getWidth() * Math.PI * (90 * 1.0 / 360));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mClipPath.reset();
        mClipPath.moveTo(mRingSize, mRingSize);
        mClipPath.addCircle(getWidth() >> 1, getHeight() >> 1, mBitmapSize >> 1, Path.Direction.CW);

        drawBitmap(canvas);
        drawGrid(canvas);
        drawRing(canvas);
        drawText(canvas);
        drawFocus(canvas);
    }

    private void drawBitmap(Canvas canvas) {
        if (mCircleBitmap == null || mCircleBitmap.isRecycled()) {
            return;
        }
        canvas.save();
        canvas.clipPath(mClipPath);

        mBitmapMatrix.reset();
        float scale = mBitmapSize / (float) mCircleBitmap.getWidth();
        mBitmapMatrix.postScale(scale, scale);
        mBitmapMatrix.postTranslate(mRingSize, mRingSize);
        canvas.drawBitmap(mCircleBitmap, mBitmapMatrix, mBitmapPaint);
        canvas.restore();
    }

    private void drawGrid(Canvas canvas) {
        if (mGridDrawable == null) {
            Bitmap gridBitmap = createGridBitmap();
            mGridDrawable = RoundedBitmapDrawableFactory.create(getResources(), gridBitmap);
            mGridDrawable.setBounds(mRingSize, mRingSize, getWidth() - mRingSize, getWidth() - mRingSize);
            mGridDrawable.setCircular(true);
        }
        mGridDrawable.draw(canvas);
    }

    private Bitmap createGridBitmap() {
        int width = mBitmapSize;
        int height = mBitmapSize;
        Bitmap gridBitmap = Bitmap.createBitmap(mBitmapSize, mBitmapSize, Bitmap.Config.ARGB_8888);
        Canvas gridCanvas = new Canvas(gridBitmap);
        if (mPixelInterval >= 4) {
            int alpha = Math.min(mPixelInterval * 36, 255);
            mGridPaint.setAlpha(alpha);
            mGridShadowPaint.setAlpha(alpha);
            float value;
            float start = 0f;
            float end;
            gridCanvas.save();
            for (int i = 0; i <= mDiameterPixelNumber; i++) {
                value = (float) i * mPixelInterval - 1;
                end = (float) height;
                gridCanvas.drawLine(value, start, value, end, mGridPaint);
                value = (float) i * mPixelInterval;
                gridCanvas.drawLine(value, start, value, end, mGridShadowPaint);
            }
            for (int i = 0; i <= mDiameterPixelNumber; i++) {
                value = (float) i * mPixelInterval - 1;
                end = (float) width;
                gridCanvas.drawLine(start, value, end, value, mGridPaint);
                value = (float) i * mPixelInterval;
                gridCanvas.drawLine(start, value, end, value, mGridShadowPaint);
            }
            gridCanvas.restore();
        }
        return gridBitmap;
    }

    private void drawRing(Canvas canvas) {
        // 从 canvas 层面去除绘制时锯齿。
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        int centerPosition = getWidth() / 2;
        mRingPaint.setStrokeWidth(mRingSize);
        canvas.drawCircle(centerPosition, centerPosition, (getWidth() >> 1) - (mRingSize >> 1), mRingPaint);

        mRingPaint.setColor(getResources().getColor(R.color.dk_color_333333));
        mRingPaint.setStrokeWidth(0.5f);
        canvas.drawCircle(centerPosition, centerPosition, getWidth() >> 1, mRingPaint);
        canvas.drawCircle(centerPosition, centerPosition, (getWidth() >> 1) - mRingSize, mRingPaint);
    }

    private void drawText(Canvas canvas) {
        if (!TextUtils.isEmpty(mInfo)) {
            canvas.drawTextOnPath(mInfo, mClipPath, mTextHOffset, mTextVOffset, mTextPaint);
            canvas.setDrawFilter(null);
        }
    }

    private void drawFocus(Canvas canvas) {
        canvas.drawRect((getWidth() >> 1) - 2 + (mFocusOffsetX - 1) * mPixelInterval,
                (getHeight() >> 1) - 2 + (mFocusOffsetY - 1) * mPixelInterval,
                (getWidth() >> 1) + 2 + (mFocusOffsetX - 1) * mPixelInterval + mPixelInterval,
                (getHeight() >> 1) + 2 + (mFocusOffsetY - 1) * mPixelInterval + mPixelInterval,
                mFocusPaint);
    }

    public void setBitmap(Bitmap bitmap, int x, int y) {
        mCircleBitmap = handleBitmap(bitmap, x, y);
        // 坐标从 0 开始，所以减 1。
        int xCenter = mCircleBitmap.getWidth() / 2 - 1;
        int yCenter = mCircleBitmap.getHeight() / 2 - 1;
        int colorInt = getPixel(mCircleBitmap, xCenter + mFocusOffsetX, yCenter + mFocusOffsetX);

        // x，y +1的原因是坐标轴从 0 开始，像素数量从 1 开始。
        mInfo = String.format(UIUtils.getString(R.string.dk_color_picker_result_info),
                ColorUtil.parseColorInt(colorInt), x + 1, y + 1);
        mRingPaint.setColor(colorInt);
        if (ColorUtil.isColdColor(colorInt)) {
            mFocusPaint.setColor(Color.WHITE);
            mTextPaint.setColor(Color.WHITE);
        } else {
            mFocusPaint.setColor(Color.BLACK);
            mTextPaint.setColor(Color.BLACK);
        }
        invalidate();
    }

    /**
     * @param x 坐标系坐标 X
     * @param y 坐标系坐标 Y
     */
    private Bitmap handleBitmap(Bitmap bitmap, int x, int y) {
        // 以 x y 中心点为坐标扩散截取 bitmap。
        int startX = x - (mDiameterPixelNumber / 2 - 1);
        int startY = y - (mDiameterPixelNumber / 2 - 1);
        int width = mDiameterPixelNumber;
        int height = mDiameterPixelNumber;

        if (startX < 0) {
            mFocusOffsetX = startX;
            startX = 0;
        } else if (startX + width > bitmap.getWidth()) {
            mFocusOffsetX = startX + width - bitmap.getWidth();
            startX = bitmap.getWidth() - width;
        } else {
            mFocusOffsetX = 0;
        }
        if (startY < 0) {
            mFocusOffsetY = startY;
            startY = 0;
        } else if (startY + height > bitmap.getHeight()) {
            mFocusOffsetY = startY + height - bitmap.getHeight();
            startY = bitmap.getHeight() - height;
        } else {
            mFocusOffsetY = 0;
        }
        return Bitmap.createBitmap(bitmap, startX, startY, width, height);
    }

    public void setPixelInterval(int pixelInterval, int diameterPixelNumber) {
        mPixelInterval = pixelInterval;
        mDiameterPixelNumber = diameterPixelNumber;
        requestLayout();
    }

    public int getPixelInterval() {
        return mPixelInterval;
    }

    public int getPixel(Bitmap bitmap, int x, int y) {
        if (x < 0 || x >= bitmap.getWidth()) {
            return -1;
        }
        if (y < 0 || y >= bitmap.getHeight()) {
            return -1;
        }
        return bitmap.getPixel(x, y);
    }
}
