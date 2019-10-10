package com.ggcode.devknife.knife.tools.appinfo.info;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.ggcode.devknife.R;
import com.ggcode.devknife.base.component.Component;
import com.ggcode.devknife.knife.tools.appinfo.list.AppListActivity;
import com.ggcode.devknife.ui.base.BaseActivity;
import com.ggcode.devknife.utils.AppUtils;
import com.ggcode.devknife.utils.DeviceUtils;
import com.ggcode.devknife.utils.FileUtils;
import com.ggcode.devknife.utils.ScreenUtils;
import com.ggcode.devknife.utils.TimeUtils;
import com.ggcode.devknife.widget.DKToolBar;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zbb 33775
 * @date: 2019/5/27 17:30
 * @desc:
 */
public class AppInfoActivity extends BaseActivity {

    private static final int REQUEST_CODE_CHOICE_APP = 811;

    private DKToolBar mTitleBar;
    private ImageView mIvAppIcon;
    private RecyclerView mRvAppInfo;
    private AppInfoAdapter mAdapter;
    private String mCurrentPackageName;

    List<InfoItem> appInfos = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, AppInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dk_fragment_app_info;
    }

    @Override
    protected void initView() {
        mTitleBar = findViewById(R.id.title_bar);
        mIvAppIcon = findViewById(R.id.iv_app_icon);
        mRvAppInfo = findViewById(R.id.rv_app_info);

        initToolBar();
        initRecyclerView();
    }

    private void initToolBar() {
        setSupportActionBar(mTitleBar);
        mTitleBar.inflateMenu(R.menu.menu_app_info);
        mTitleBar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int i = menuItem.getItemId();
                if (i == R.id.menu_app_list) {
                    AppListActivity.start(mActivity, REQUEST_CODE_CHOICE_APP);
                } else if (i == R.id.menu_app_system_info) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + mCurrentPackageName));
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_info, menu);
        return true;
    }

    private void initRecyclerView() {
        mRvAppInfo.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AppInfoAdapter();
        mAdapter.bindToRecyclerView(mRvAppInfo);
    }

    @Override
    protected void initData() {
        post(new Runnable() {
            @Override
            public void run() {
                setData(getPackageName());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHOICE_APP) {
            setData(data.getStringExtra(AppListActivity.RESULT_EXTRA_APP_INFO));
        }
    }

    private void setData(String packageName) {
        mCurrentPackageName = packageName;
        PackageManager pm = Component.app().getPackageManager();
        PackageInfo pi;

        try {
            pi = pm.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        ApplicationInfo ai = pi.applicationInfo;
        // icon 和 标题
        mTitleBar.setTitle(String.format(getString(R.string.dk_app_info_title), ai.loadLabel(pm).toString()));
        mIvAppIcon.setImageDrawable(ai.loadIcon(pm));
        appInfos.clear();
        addAppInfo(pi, ai);
        addSystemInfo();
        mAdapter.replaceData(appInfos);
    }

    private void addAppInfo(PackageInfo pi, ApplicationInfo ai) {
        appInfos.add(new TitleItem(getString(R.string.dk_app_info_subtitle_app_information)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_package_name), pi.packageName));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_version_name), pi.versionName));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_version_code), String.valueOf(pi.versionCode)));
        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_min_sdk_version),
                    AppUtils.getSdkVersionDesc(ai.minSdkVersion)));
        }
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_target_sdk_version),
                AppUtils.getSdkVersionDesc(ai.targetSdkVersion)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_uid),
                String.valueOf(ai.uid)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_is_system_app),
                (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0 ? getString(R.string.dk_yes)
                        : getString(R.string.dk_no)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_apk_path), ai.sourceDir));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_apk_size), FileUtils.getFileSize(ai.sourceDir)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_sign_md5),
                AppUtils.getAppSignatureMD5(mCurrentPackageName)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_sign_sha1),
                AppUtils.getAppSignatureSHA1(mCurrentPackageName)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_sign_sha256),
                AppUtils.getAppSignatureSHA256(mCurrentPackageName)));
        String fileDir = getFilesDir().toString();
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_data_directory),
                fileDir.substring(0, fileDir.lastIndexOf("/"))));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_launcher_entrance),
                AppUtils.getLauncherActivityName(mCurrentPackageName)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_application_name), ai.name));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_first_installation_time),
                TimeUtils.format(pi.firstInstallTime)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_last_upgrade_time),
                TimeUtils.format(pi.lastUpdateTime)));
    }

    public void addSystemInfo() {

        appInfos.add(new TitleItem(getString(R.string.dk_app_info_subtitle_photo_information)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_phone_model),
                android.os.Build.BRAND + " " + android.os.Build.MODEL));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_system_version),
                AppUtils.getSdkVersionDesc(android.os.Build.VERSION.SDK_INT)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_storage),
                String.format(getString(R.string.dk_app_info_available_desc), DeviceUtils.getRomTotalSize(),
                        DeviceUtils.getRomAvailableSize())));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_memory),
                String.format(getString(R.string.dk_app_info_available_desc), DeviceUtils.getTotalMemorySize(),
                        DeviceUtils.getAvailableMemorySize())));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_screen_resolution),
                String.format(getString(R.string.dk_app_info_screen_resolution_desc), ScreenUtils.getScreenWidth(),
                        ScreenUtils.getScreenWidth(), Resources.getSystem().getDisplayMetrics().densityDpi)));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_equipment_density),
                String.format(getString(R.string.dk_app_info_equipment_density_desc),
                        String.valueOf(Resources.getSystem().getDisplayMetrics().density),
                        ScreenUtils.getDensityType())));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_imei), DeviceUtils.getIMEI()));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_imei), DeviceUtils.getIMEI()));
        appInfos.add(new InfoItem(getString(R.string.dk_app_info_item_mac_address), DeviceUtils.getMacAddress()));
    }
}
