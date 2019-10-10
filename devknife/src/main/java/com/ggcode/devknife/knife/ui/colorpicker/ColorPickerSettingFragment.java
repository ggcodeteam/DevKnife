package com.ggcode.devknife.knife.ui.colorpicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import com.ggcode.devknife.R;
import com.ggcode.devknife.manager.page.FloatPageManager;
import com.ggcode.devknife.manager.page.PageIntent;
import com.ggcode.devknife.ui.base.BaseFragment;
import com.ggcode.devknife.utils.ToastUtil;

/**
 * @author: zbb 33775
 * @date: 2019/5/17 22:30
 * @desc:
 */
public class ColorPickerSettingFragment extends BaseFragment {

    private static final int CODE_CAPTURE_SCREEN = 10001;

    @Override
    protected int getLayoutId() {
        return R.layout.dk_fragment_color_pickter_settting;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {
        requestCaptureScreenPermission();
    }

    private boolean requestCaptureScreenPermission() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) getContext()
                .getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager == null) {
            return false;
        }
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), CODE_CAPTURE_SCREEN);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            ToastUtil.show(getString(R.string.dk_capture_screen_fail_toast));
            finish();
            return;
        }
        if (requestCode == CODE_CAPTURE_SCREEN) {
            showColorPicker(data);
        }
        finish();
    }

    private void showColorPicker(Intent data) {
        PageIntent pageIntent = new PageIntent(ColorPickerInfoPage.class);
        FloatPageManager.getInstance().startIntent(pageIntent);

        pageIntent = new PageIntent(ColorPickerIconPage.class);
        pageIntent.setBundle(data.getExtras());
        FloatPageManager.getInstance().startIntent(pageIntent);
    }
}
