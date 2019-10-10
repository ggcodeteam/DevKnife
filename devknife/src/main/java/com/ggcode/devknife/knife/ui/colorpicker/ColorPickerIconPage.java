package com.ggcode.devknife.knife.ui.colorpicker;

import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import com.ggcode.devknife.R;
import com.ggcode.devknife.manager.page.BaseFloatPage;
import com.ggcode.devknife.manager.page.FloatPageManager;
import com.ggcode.devknife.ui.base.TouchProxy;
import com.ggcode.devknife.ui.base.TouchProxy.OnTouchEventListener;
import com.ggcode.devknife.utils.UIUtils;

/**
 * @author: zbb 33775
 * @date: 2019/5/17 13:37
 * @desc:
 */
@RequiresApi(api = VERSION_CODES.LOLLIPOP)
public class ColorPickerIconPage extends BaseFloatPage implements OnTouchEventListener {

    private ImageView mIvColorPickerIcon;
    private ColorPickerInfoPage mColorPickerInfoPage;
    private ImageCapture mImageCapture;

    private int mScreenWidth = UIUtils.getScreenWidth();
    private int mScreenHeight = UIUtils.getScreenHeight();
    private int mMinX;
    private int mMaxX;
    private int mMinY;
    private int mMaxY;

    @Override
    protected void onCreate() {
        setContextView(R.layout.dk_page_color_picker_info);
        mIvColorPickerIcon = findViewById(R.id.iv_color_picker_icon);
        getRootView().setOnTouchListener(new TouchProxy(this));

        mImageCapture = new ImageCapture();
        mImageCapture.init(getContext(), getBundle());
        mColorPickerInfoPage = (ColorPickerInfoPage) FloatPageManager.getInstance()
                .getFloatPage(ColorPickerInfoPage.class);
        post(new Runnable() {
            @Override
            public void run() {
                mMinX = -mIvColorPickerIcon.getPaddingLeft();
                mMaxX = mScreenWidth + mMinX;
                mMinY = -(mIvColorPickerIcon.getHeight() - mIvColorPickerIcon.getPaddingBottom() + UIUtils
                        .getStatusBarHeight());
                mMaxY = mScreenHeight + mMinY;

                mLayoutParams.x = (mScreenWidth - mIvColorPickerIcon.getWidth()) / 2;
                mLayoutParams.y = (mScreenHeight - mIvColorPickerIcon.getHeight()) / 2;
                updateViewLayout();
            }
        });
    }

    @Override
    protected void configWindowsLayoutParams(LayoutParams params) {
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
    }

    @Override
    public void onMove(float downX, float downx, float dx, float dy) {
        mLayoutParams.x += dx;
        mLayoutParams.y += dy;
        checkBound(mLayoutParams);
        updateViewLayout();
        showColorInfo();
    }

    private void checkBound(WindowManager.LayoutParams layoutParams) {
        if (layoutParams.x < mMinX) {
            layoutParams.x = mMinX;
        } else if (layoutParams.x > mMaxX) {
            layoutParams.x = mMaxX;
        }

        if (layoutParams.y < mMinY) {
            layoutParams.y = mMinY;
        } else if (layoutParams.y > mMaxY) {
            layoutParams.y = mMaxY;
        }
    }

    @Override
    public void onUp(float x, float y) {

    }

    @Override
    public void onDown(float x, float y) {
        captureProjectionInfo();
    }

    private void captureProjectionInfo() {
        hide();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mImageCapture.captureProjection();
                showColorInfo();
                show();
            }
        }, 100);
    }

    @Override
    public void hide() {
        super.hide();
        mColorPickerInfoPage.hide();
    }

    @Override
    public void show() {
        super.show();
        mColorPickerInfoPage.show();
    }

    private void showColorInfo() {
        mColorPickerInfoPage.showInfo(mImageCapture.getOriginalBitmap(), mLayoutParams.x - mMinX,
                mLayoutParams.y - mMinY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageCapture.destroy();
        mImageCapture = null;
    }
}
