package com.ggcode.devknife.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import com.ggcode.devknife.R;

/**
 * @author: zbb 33775
 * @date: 2019/5/29 16:16
 * @desc:
 */
public class DKToolBar extends Toolbar {

    public DKToolBar(Context context) {
        super(context);
        init();
    }

    public DKToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DKToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setNavigationIcon(R.drawable.dk_back_btn);
        setTitleTextColor(getResources().getColor(R.color.dk_text_black));
        setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof Activity) {
                    ((Activity) getContext()).onBackPressed();
                }
            }
        });
    }
}
