package com.ggcode.devknife.knife.tools.appinfo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import com.ggcode.devknife.R;
import com.ggcode.devknife.knife.tools.appinfo.info.AppInfoActivity;
import com.ggcode.devknife.manager.init.Category;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.ui.home.DevToolsActivity;
import com.ggcode.devknife.utils.PermissionUtil;

/**
 * @author: zbb 33775
 * @date: 2019/5/27 17:19
 * @desc:
 */
public class AppInfo extends Knife {

    @Override
    public int getCategory() {
        return Category.TOOLS;
    }

    @Override
    public int getName() {
        return R.string.dk_tools_app_info;
    }

    @Override
    public int getIcon() {
        return R.drawable.dk_page_app_info;
    }

    @Override
    public void onClick(Context context) {
        if (!PermissionUtil.requestPermission((Activity) context, android.Manifest.permission.READ_PHONE_STATE,
                DevToolsActivity.REQUEST_CODE_APP_INFO)) {
            return;
        }

        AppInfoActivity.start(context);
    }

    @Override
    public void onInit(Application application) {
    }

    @Override
    public boolean onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (requestCode != DevToolsActivity.REQUEST_CODE_APP_INFO) {
            return false;
        }
        if (PermissionUtil.onPermissionResult(activity, requestCode, permissions, grantResults)) {
            AppInfoActivity.start(activity);
        }
        return true;
    }

}
