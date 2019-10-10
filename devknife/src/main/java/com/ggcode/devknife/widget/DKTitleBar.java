package com.ggcode.devknife.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.ggcode.devknife.R;
import com.ggcode.devknife.ui.base.ContainerActivity;

/**
 * @author : zbb 33775
 * @date: 2019/3/18 16:43
 */
public class DKTitleBar extends ConstraintLayout {

    private TextView mTvTitle;
    private ImageView mIvBack;
    private TextView mTvMenu;
    private ImageView mIvMenu;
    private TextView mTvMenu1;
    private ImageView mIvMenu1;
    private View.OnClickListener mOnBackClickListener;

    public DKTitleBar(Context context) {
        this(context, null);
    }

    public DKTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DKTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.dk_title_bar, this, true);
        mTvTitle = findViewById(R.id.tv_title);
        mIvBack = findViewById(R.id.iv_back);
        mTvMenu = findViewById(R.id.tv_menu);
        mIvMenu = findViewById(R.id.iv_menu);
        mTvMenu1 = findViewById(R.id.tv_menu1);
        mIvMenu1 = findViewById(R.id.iv_menu1);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DKTitleBar);
        // back
        if (a.hasValue(R.styleable.DKTitleBar_backSrc)) {
            setBackImg(a.getDrawable(R.styleable.DKTitleBar_backSrc));
        }
        if (a.hasValue(R.styleable.DKTitleBar_backImgVisible)) {
            setBackVisible(a.getBoolean(R.styleable.DKTitleBar_backImgVisible, true));
        }
        if (a.hasValue(R.styleable.DKTitleBar_titleTextColor)) {
            setTitleColor(a.getColorStateList(R.styleable.DKTitleBar_titleTextColor));
        }
        if (a.hasValue(R.styleable.DKTitleBar_titleText)) {
            setTitle(a.getString(R.styleable.DKTitleBar_titleText));
        }
        if (a.hasValue(R.styleable.DKTitleBar_menuText)) {
            setMenuText(a.getString(R.styleable.DKTitleBar_menuText));
        }
        if (a.hasValue(R.styleable.DKTitleBar_menuTextColor)) {
            setMenuTextColor(a.getColorStateList(R.styleable.DKTitleBar_menuTextColor));
        }
        if (a.hasValue(R.styleable.DKTitleBar_menuSrc)) {
            setMenuImg(a.getDrawable(R.styleable.DKTitleBar_menuSrc));
        }

        if (a.hasValue(R.styleable.DKTitleBar_menu1Text)) {
            setMenu1Text(a.getString(R.styleable.DKTitleBar_menu1Text));
        }
        if (a.hasValue(R.styleable.DKTitleBar_menu1TextColor)) {
            setMenu1TextColor(a.getColorStateList(R.styleable.DKTitleBar_menu1TextColor));
        }
        if (a.hasValue(R.styleable.DKTitleBar_menu1Src)) {
            setMenu1Img(a.getDrawable(R.styleable.DKTitleBar_menu1Src));
        }

        a.recycle();

        mIvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnBackClickListener != null) {
                    mOnBackClickListener.onClick(v);
                } else {
                    if (getContext() instanceof ContainerActivity) {
                        ((ContainerActivity) getContext()).onBackPressed(); // default behavior
                    }
                }
            }
        });
    }

    public void setBackImg(@DrawableRes int menuImgRsd) {
        mIvBack.setImageResource(menuImgRsd);
    }

    public void setBackImg(@Nullable Drawable menuImg) {
        mIvBack.setImageDrawable(menuImg);
    }

    public void setTitle(@StringRes int id) {
        setTitle(getResources().getString(id));
    }

    public void setTitle(String title) {
        setTitle(title, true);
    }

    public void setTitle(@Nullable String title, boolean alpha) {
        if (TextUtils.isEmpty(title)) {
            mTvTitle.setText("");
        } else {
            mTvTitle.setText(title);
            if (alpha) {
                mTvTitle.setAlpha(0);
                mTvTitle.animate().alpha(1).start();
            }
        }
    }

    public void setTitleColor(@ColorInt int color) {
        mTvTitle.setTextColor(color);
    }

    public void setTitleColor(ColorStateList colors) {
        if (colors == null) {
            return;
        }
        mTvTitle.setTextColor(colors);
    }

    public void setTitleColorRes(@ColorRes int textColorRes) {
        mTvTitle.setTextColor(getResources().getColor(textColorRes));
    }

    public void setBackVisible(boolean visible) {
        if (visible) {
            mIvBack.setVisibility(VISIBLE);
        } else {
            mIvBack.setVisibility(INVISIBLE);
        }
    }

    public void setMenuTextColorRes(@ColorRes int textColorRes) {
        mTvMenu.setTextColor(getResources().getColor(textColorRes));
    }

    public void setMenuTextColor(@ColorInt int color) {
        mTvMenu.setTextColor(color);
    }

    public void setMenuTextColor(ColorStateList colors) {
        if (colors == null) {
            return;
        }
        mTvMenu.setTextColor(colors);
    }

    public void setMenuText(@Nullable String text) {
        if (text == null) {
            return;
        }
        mTvMenu.setVisibility(View.VISIBLE);
        mIvMenu.setVisibility(View.GONE);
        mTvMenu.setText(text);
    }

    public void setMenuImg(@DrawableRes int imgRes) {
        mTvMenu.setVisibility(View.GONE);
        mIvMenu.setVisibility(View.VISIBLE);
        mIvMenu.setImageResource(imgRes);
    }

    public void setMenuImg(@Nullable Drawable drawable) {
        mTvMenu.setVisibility(View.GONE);
        mIvMenu.setVisibility(View.VISIBLE);
        mIvMenu.setImageDrawable(drawable);
    }

    public void setMenuVisible(boolean visible) {
        if (visible) {
            mTvMenu.setVisibility(View.VISIBLE);
            mIvMenu.setVisibility(View.VISIBLE);
        } else {
            mTvMenu.setVisibility(View.GONE);
            mIvMenu.setVisibility(View.GONE);
        }
    }

    public void setMenuEnabled(boolean b) {
        mTvMenu.setEnabled(b);
        mIvMenu.setEnabled(b);
    }

    public void setMenu1TextColorRes(@ColorRes int textColorRes) {
        mTvMenu1.setTextColor(getResources().getColor(textColorRes));
    }

    public void setMenu1TextColor(@ColorInt int color) {
        mTvMenu1.setTextColor(color);
    }

    public void setMenu1TextColor(ColorStateList colors) {
        if (colors == null) {
            return;
        }
        mTvMenu1.setTextColor(colors);
    }

    public void setMenu1Text(@Nullable String text) {
        if (text == null) {
            return;
        }
        mTvMenu1.setVisibility(View.VISIBLE);
        mIvMenu1.setVisibility(View.GONE);
        mTvMenu1.setText(text);
    }

    public void setMenu1Img(@DrawableRes int imgRes) {
        mTvMenu1.setVisibility(View.GONE);
        mIvMenu1.setVisibility(View.VISIBLE);
        mIvMenu1.setImageResource(imgRes);
    }

    public void setMenu1Img(@Nullable Drawable drawable) {
        mTvMenu1.setVisibility(View.GONE);
        mIvMenu1.setVisibility(View.VISIBLE);
        mIvMenu1.setImageDrawable(drawable);
    }

    public void setMenu1Visible(boolean visible) {
        if (visible) {
            mTvMenu1.setVisibility(View.VISIBLE);
            mIvMenu1.setVisibility(View.VISIBLE);
        } else {
            mTvMenu1.setVisibility(View.GONE);
            mIvMenu1.setVisibility(View.GONE);
        }
    }

    public void setMenu1Enabled(boolean b) {
        mTvMenu1.setEnabled(b);
        mIvMenu1.setEnabled(b);
    }

    public void setOnBackClickListener(OnClickListener onBackClickListener) {
        mOnBackClickListener = onBackClickListener;
    }
}
