package com.ggcode.devknife.knife.ui.colorpicker;

import android.app.Application;
import android.content.Context;
import com.ggcode.devknife.R;
import com.ggcode.devknife.constant.FragmentLabel;
import com.ggcode.devknife.manager.init.Category;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.ui.base.ContainerActivity;

/**
 * @author: zbb 33775
 * @date: 2019/4/24 9:01
 * @desc:
 */
public class ColorPicker extends Knife {

    @Override
    public int getCategory() {
        return Category.UI;
    }

    @Override
    public int getName() {
        return R.string.dk_ui_color_picker;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_page_color_picker;
    }

    @Override
    public void onClick(Context context) {
        ContainerActivity.start(context, FragmentLabel.COLOR_PICKTER, true);
    }

    @Override
    public void onInit(Application application) {

    }
}
