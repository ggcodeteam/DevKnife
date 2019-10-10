package com.ggcode.devknife.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialog;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.ggcode.devknife.R;

/**
 * <pre>
 * @author : passin
 * @Date: 2019/1/7 16:38
 * </pre>
 * --------------------------------
 * 1. 适用于大部分情况下的 Dialog 使用情况。
 * 2. 不加判 null或类型转换try catch，是希望问题能在测试阶段就复现。
 * 3. 先 build 后才能操纵视图，原因是出于性能考虑。
 */
public class GGDialog extends AppCompatDialog {

    private SparseArray<View> views;
    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

    private GGDialog(@NonNull Context context, @LayoutRes int contentId, @StyleRes int themeResId) {
        super(context, themeResId);
        setContentView(contentId);
        views = new SparseArray<>();
    }

    public GGDialog setText(@IdRes int viewId, CharSequence value) {
        TextView view = getView(viewId);
        view.setText(value);
        return this;
    }

    public GGDialog setText(@IdRes int viewId, @StringRes int strId) {
        TextView view = getView(viewId);
        view.setText(strId);
        return this;
    }

    public GGDialog setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public GGDialog setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public GGDialog setBackgroundRes(@IdRes int viewId, @DrawableRes int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public GGDialog setTextColor(@IdRes int viewId, @ColorInt int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public GGDialog setEnable(@IdRes int viewId, boolean enable) {
        View view = getView(viewId);
        view.setEnabled(enable);
        return this;
    }

    public GGDialog setImageDrawable(@IdRes int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public GGDialog setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public GGDialog setAlpha(@IdRes int viewId, @FloatRange(from = 0, to = 1) float value) {
        View view = getView(viewId);
        view.setAlpha(value);
        return this;
    }

    public GGDialog setVisible(@IdRes int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public GGDialog linkify(@IdRes int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public GGDialog setTypeface(@IdRes int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    public GGDialog setProgress(@IdRes int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public GGDialog setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public GGDialog setMax(@IdRes int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public GGDialog setRating(@IdRes int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public GGDialog setRating(@IdRes int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public GGDialog setAdapter(@IdRes int viewId, Adapter adapter) {
        AdapterView view = getView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    public GGDialog setChecked(@IdRes int viewId, boolean checked) {
        View view = getView(viewId);
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(checked);
        }
        return this;
    }

    public GGDialog setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public GGDialog setOnItemSelectedClickListener(@IdRes int viewId,
            AdapterView.OnItemSelectedListener listener) {
        AdapterView view = getView(viewId);
        view.setOnItemSelectedListener(listener);
        return this;
    }

    public GGDialog setOnCheckedChangeListener(@IdRes int viewId,
            CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

    public GGDialog setTag(@IdRes int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public GGDialog addOnClickListener(@IdRes final int... viewId) {
        for (int id : viewId) {
            final View view = getView(id);
            if (view != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onClickListener != null) {
                            onClickListener
                                    .onClick(GGDialog.this, v);
                        }
                    }
                });
            }
        }
        return this;
    }

    public GGDialog setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        final View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public GGDialog addOnLongClickListener(@IdRes final int... viewId) {
        for (int id : viewId) {
            final View view = getView(id);
            if (view != null) {
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onLongClickListener != null) {
                            return onLongClickListener.onLongClick(GGDialog.this, v);
                        }
                        return false;
                    }
                });
            }
        }
        return this;
    }

    public GGDialog setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        final View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (null == view) {
            view = findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static Builder with(Context context, @StyleRes int themeId) {
        return new Builder(context, themeId);
    }

    public static class Builder {

        private Context context;
        private int themeId;
        private int contentId;
        private int animationId;
        // 是否可以取消，判断优先级比 cancelOnTouchOutside 高
        private boolean cancelable = true;
        private boolean cancelOnTouchOutside;
        private int gravity = Gravity.CENTER;
        private int width = ViewGroup.LayoutParams.MATCH_PARENT;
        private int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        private OnCancelListener onCancelListener;
        private OnDismissListener onDismissListener;
        private OnKeyListener onKeyListener;
        private OnShowListener onShowListener;

        public Builder(Context context) {
            this(context, R.style.DK_Dialog);
        }

        public Builder(Context context, @StyleRes int themeId) {
            this.context = context;
            this.themeId = themeId;
        }

        public Builder themeId(@StyleRes int themeId) {
            this.themeId = themeId;
            return this;
        }

        public Builder contentId(@LayoutRes int contentId) {
            this.contentId = contentId;
            return this;
        }

        /**
         * @see Gravity
         */
        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * 设置宽度全屏显示
         */
        public Builder fullWidth() {
            width = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 设置宽度全屏显示
         */
        public Builder fullHeight() {
            height = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 设置全屏显示
         */
        public Builder fullScreen() {
            width = ViewGroup.LayoutParams.MATCH_PARENT;
            height = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 设置屏幕宽占比
         */
        public Builder screenWidthPercent(@FloatRange(from = 0, to = 1.0) float percent) {
            width = (int) (context.getResources().getDisplayMetrics().widthPixels * percent);
            return this;
        }

        /**
         * 设置屏幕高占比
         */
        public Builder screenHeightPercent(@FloatRange(from = 0, to = 1.0) float percent) {
            height = (int) (context.getResources().getDisplayMetrics().heightPixels * percent);
            return this;
        }

        /**
         * 设置宽高
         */
        public Builder setWidthAndHeight(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        /**
         * 设置动画
         */
        public Builder animationId(@StyleRes int animtionId) {
            this.animationId = animtionId;
            return this;
        }

        /**
         * 设置点击外部是否可以取消,优先级高于 setCancelOnTouchOutside.
         */
        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        /**
         * 设置点击外部是否可以取消
         */
        public Builder cancelOnTouchOutside(boolean cancelable) {
            this.cancelOnTouchOutside = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.onKeyListener = onKeyListener;
            return this;
        }

        public Builder setOnShowListener(OnShowListener onShowListener) {
            this.onShowListener = onShowListener;
            return this;
        }

        /**
         * 出于性能考虑，不在 Builder 时候利用容器储存具体的视图操作，
         * 调用 build() 之后才能操作视图。
         */
        public GGDialog build() {
            if (contentId == 0) {
                throw new IllegalArgumentException("please set contentId");
            }
            final GGDialog dialog = new GGDialog(context, contentId, themeId);
            dialog.setCancelable(cancelable);
            // cancelable 优先级比 setCanceledOnTouchOutside 高
            if (cancelable) {
                dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
            }
            dialog.setOnCancelListener(onCancelListener);
            dialog.setOnDismissListener(onDismissListener);
            dialog.setOnKeyListener(onKeyListener);
            dialog.setOnShowListener(onShowListener);

            Window window = dialog.getWindow();
            window.setWindowAnimations(animationId);
            window.setGravity(gravity);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = width;
            lp.height = height;
            window.setAttributes(lp);
            return dialog;
        }
    }

    public GGDialog showAndGet() {
        super.show();
        return this;
    }

    public GGDialog setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public GGDialog setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
        return this;
    }

    public interface OnClickListener {

        void onClick(GGDialog dialog, View view);
    }

    public interface OnLongClickListener {

        boolean onLongClick(GGDialog dialog, View view);
    }
}
