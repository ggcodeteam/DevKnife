package com.ggcode.devknife.ui.base;

import android.view.MotionEvent;
import android.view.View;
import com.ggcode.devknife.utils.UIUtils;
import com.ggcode.devknife.base.component.Component;

/**
 * @author: zbb 33775
 * @date: 2019/5/15 17:39
 * @desc:
 */
public class TouchProxy implements View.OnTouchListener {

    private static final int MIN_CLICK_TIME = 30;
    private static final int LONG_CLICK_PRESS_TIME = 300;

    private float mLastY, mLastX = 0f;
    private float mDownX, mDownY = 0f;
    private boolean mIsClickState;
    private boolean mHasPerformedLongPress;
    private int clickLimitValue = UIUtils.dp2px(18) / 4;
    private View mEventView;

    private OnTouchEventListener mTouchEventListener;

    private Runnable mPendingCheckForLongPress = new Runnable() {
        @Override
        public void run() {
            if (mEventView != null) {
                mHasPerformedLongPress = true;
                mEventView.performLongClick();
            }
        }
    };

    public TouchProxy(OnTouchEventListener onTouchEventListener) {
        this.mTouchEventListener = onTouchEventListener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float X = event.getRawX();
        float Y = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mEventView = v;
                mIsClickState = true;
                mDownX = X;
                mDownY = Y;
                mLastX = X;
                mLastY = Y;
                if (mTouchEventListener != null) {
                    mTouchEventListener.onDown(X, Y);
                }
                Component.uiHandler().postDelayed(mPendingCheckForLongPress, LONG_CLICK_PRESS_TIME);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(X - mDownX) < clickLimitValue && Math.abs(Y - mDownY) < clickLimitValue && mIsClickState) {
                    mIsClickState = true;
                } else {
                    Component.uiHandler().removeCallbacks(mPendingCheckForLongPress);
                    mIsClickState = false;
                    if (mTouchEventListener != null) {
                        mTouchEventListener.onMove(mLastX, mLastY, X - mLastX, Y - mLastY);
                    }
                    mLastX = X;
                    mLastY = Y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mTouchEventListener != null) {
                    mTouchEventListener.onUp(X, Y);
                }
                if (mHasPerformedLongPress) {
                    return true;
                }
                Component.uiHandler().removeCallbacks(mPendingCheckForLongPress);

                if (mIsClickState && event.getEventTime() - event.getDownTime() > MIN_CLICK_TIME) {
                    v.performClick();
                }
                mEventView = null;
                break;
        }
        return true;
    }

    public interface OnTouchEventListener {

        void onMove(float downX, float downx, float dx, float dy);

        void onUp(float x, float y);

        void onDown(float x, float y);
    }
}
