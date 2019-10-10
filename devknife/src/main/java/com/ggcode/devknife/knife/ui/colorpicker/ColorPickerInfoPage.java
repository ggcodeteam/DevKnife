package com.ggcode.devknife.knife.ui.colorpicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.ggcode.devknife.R;
import com.ggcode.devknife.config.ColorPickerConfig;
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
public class ColorPickerInfoPage extends BaseFloatPage implements OnTouchEventListener, OnClickListener {

    private static ColorPickerConfig mColorPickerConfig;

    private ColorPickerView mPickerView;
    private FrameLayout mFlFunction;
    private ImageView mIvClose;
    private ImageView mIvShiftSwtich;
    private ImageView mIvUpShift;
    private ImageView mIvDownShift;
    private ImageView mIvLeftShift;
    private ImageView mIvRightShift;
    private ConstraintLayout mClShift;

    private Bitmap mCurrentBitmap;
    private int mCurrentX;
    private int mCurrentY;

    private int mScreenWidth = UIUtils.getScreenWidth();
    private int mScreenHeight = UIUtils.getScreenHeight();
    private int mStatusBarHeight = UIUtils.getStatusBarHeight();
    private int mMinX;
    private int mMaxX;
    private int mMinY;
    private int mMaxY;

    private int mCloseViewTranslationX;

    public static void config(ColorPickerConfig colorPickerConfig) {
        mColorPickerConfig = colorPickerConfig;
    }

    @Override
    protected void onCreate() {
        setContextView(R.layout.dk_page_color_picker);
        initView();
        post(new Runnable() {
            @Override
            public void run() {
                mMinX = -mPickerView.getWidth() / 2;
                mMaxX = mScreenWidth - mPickerView.getWidth() / 2 - mPickerView.getPixelInterval();
                mMinY = -mPickerView.getHeight() / 2 - mStatusBarHeight;
                mMaxY = mScreenHeight - (getRootView().getHeight() - mPickerView.getHeight() / 2) - mPickerView
                        .getPixelInterval() + mFlFunction.getHeight();
            }
        });
    }

    private void initView() {
        mPickerView = findViewById(R.id.color_picker_view);
        mFlFunction = findViewById(R.id.fl_function);
        mIvClose = findViewById(R.id.iv_close);
        mIvShiftSwtich = findViewById(R.id.iv_shift_switch);
        mIvUpShift = findViewById(R.id.iv_up_shift);
        mIvDownShift = findViewById(R.id.iv_down_shift);
        mIvLeftShift = findViewById(R.id.iv_left_shift);
        mIvRightShift = findViewById(R.id.iv_right_shift);
        mClShift = findViewById(R.id.cl_shift);

        getRootView().setOnTouchListener(new TouchProxy(this));
        mIvClose.setOnClickListener(this);
        mIvShiftSwtich.setOnClickListener(this);
        mIvUpShift.setOnClickListener(this);
        mIvDownShift.setOnClickListener(this);
        mIvLeftShift.setOnClickListener(this);
        mIvRightShift.setOnClickListener(this);

        if (mColorPickerConfig != null) {
            mPickerView
                    .setPixelInterval(mColorPickerConfig.getPixelIntervalPx(), mColorPickerConfig.getDiamPixelNumber());
        }

        post(new Runnable() {
            @Override
            public void run() {
                mLayoutParams.x = (mScreenWidth - mPickerView.getWidth()) / 2;
                mLayoutParams.y = mScreenHeight - mPickerView.getHeight() - UIUtils.dp2px(100);
                updateViewLayout();

                ViewGroup.LayoutParams layoutParams = mFlFunction.getLayoutParams();
                layoutParams.width = Math.max(UIUtils.dp2px(180), mPickerView.getWidth());
                mFlFunction.setLayoutParams(layoutParams);

                mCloseViewTranslationX =
                        layoutParams.width / 2 - mIvClose.getWidth() / 2 - mFlFunction.getPaddingEnd();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_close) {
            FloatPageManager.getInstance().remove(ColorPickerIconPage.class);
            FloatPageManager.getInstance().remove(ColorPickerInfoPage.class);
        } else if (i == R.id.iv_shift_switch) {
            swtichShiftVisibility();
        } else if (i == R.id.iv_up_shift) {
            showInfo(mCurrentX, mCurrentY - 1);
        } else if (i == R.id.iv_down_shift) {
            showInfo(mCurrentX, mCurrentY + 1);
        } else if (i == R.id.iv_left_shift) {
            showInfo(mCurrentX - 1, mCurrentY);
        } else if (i == R.id.iv_right_shift) {
            showInfo(mCurrentX + 1, mCurrentY);
        }
    }

    private void swtichShiftVisibility() {
        if (mClShift.getVisibility() == View.VISIBLE) {
            // 隐藏
            mIvClose.animate().translationX(0).start();

            mClShift.animate()
                    .translationY(UIUtils.dp2px(0))
                    .alpha(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mClShift.setVisibility(View.GONE);
                        }
                    })
                    .start();
        } else {
            // 显示
            mClShift.setVisibility(View.VISIBLE);
            mIvClose.animate()
                    .translationX(mCloseViewTranslationX)
                    .start();
            mClShift.animate()
                    .translationY(UIUtils.dp2px(-38))
                    .alpha(1)
                    .setListener(null)
                    .start();
        }
    }

    @Override
    protected void configWindowsLayoutParams(LayoutParams params) {
        params.flags = LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_NO_LIMITS;
    }

    @Override
    public void onMove(float downX, float downx, float dx, float dy) {
        mLayoutParams.x += dx;
        mLayoutParams.y += dy;
        checkBound();
        updateViewLayout();
    }

    private void checkBound() {
        if (mLayoutParams.x < mMinX) {
            mLayoutParams.x = mMinX;
        } else if (mLayoutParams.x > mMaxX) {
            mLayoutParams.x = mMaxX;
        }

        if (mLayoutParams.y < mMinY) {
            mLayoutParams.y = mMinY;
        } else if (mLayoutParams.y > mMaxY) {
            mLayoutParams.y = mMaxY;
        }
    }

    /**
     * @param x 取色点 X 轴坐标
     * @param y 取色点 Y 轴坐标
     */
    public void showInfo(Bitmap originalBitmap, int x, int y) {
        if (originalBitmap == null) {
            return;
        }
        mCurrentBitmap = originalBitmap;
        showInfo(x, y);
    }

    private void showInfo(int x, int y) {
        if (mCurrentBitmap == null) {
            return;
        }
        int checkX = Math.max(0, Math.min(x, mScreenWidth - 1));
        int checkY = Math.max(0, Math.min(y, mScreenHeight - 1));
        if (mCurrentX == checkX && mCurrentY == checkY) {
            return;
        }
        mCurrentX = checkX;
        mCurrentY = checkY;

        mPickerView.setBitmap(mCurrentBitmap, mCurrentX, mCurrentY);
    }

    @Override
    public void onUp(float x, float y) {

    }

    @Override
    public void onDown(float x, float y) {
    }
}
