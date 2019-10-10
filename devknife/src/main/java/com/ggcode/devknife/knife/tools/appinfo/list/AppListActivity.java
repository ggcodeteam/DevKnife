package com.ggcode.devknife.knife.tools.appinfo.list;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.ggcode.devknife.R;
import com.ggcode.devknife.knife.tools.appinfo.AppInfoConfig;
import com.ggcode.devknife.ui.base.BaseActivity;
import com.ggcode.devknife.ui.base.adapter.DKAdapter;
import com.ggcode.devknife.ui.base.adapter.DKAdapter.OnItemClickListener;
import com.ggcode.devknife.widget.DKToolBar;
import java.util.ArrayList;
import java.util.List;
import me.passin.loadknife.core.LoadKnife;
import me.passin.loadknife.core.LoadService;

/**
 * @author: zbb 33775
 * @date: 2019/5/28 11:25
 * @desc:
 */
public class AppListActivity extends BaseActivity {

    public static final String RESULT_EXTRA_APP_INFO = "result_extra_app_info";

    private DKToolBar mTitleBar;
    private EditText mEtSearchApp;
    private RecyclerView mRvAppList;
    private AppListAdapter mAdapter;
    private MenuItem mMenuShowStrategy;
    private LoadService mLoadService;

    private List<AppItem> mNonSystemAppItems;
    /**
     * 包含系统应用
     */
    private List<AppItem> mAllAppItems;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;

    private int mStrategy = AppInfoConfig.getLastShowStrategy();
    private boolean mIsContainsSystemApp = AppInfoConfig.isContainsSystemApp();

    public static void start(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, AppListActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dk_activity_app_list;
    }

    @Override
    protected void initView() {
        mTitleBar = findViewById(R.id.title_bar);
        mEtSearchApp = findViewById(R.id.et_search_app);
        mRvAppList = findViewById(R.id.rv_app_list);

        mLoadService = LoadKnife.getDefault().register(this);

        initToolBar();
        initRecyclerView();

        mEtSearchApp.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    handled = true;
                    matchingApp();
                }
                return handled;
            }
        });
    }

    private void initToolBar() {
        mTitleBar.inflateMenu(R.menu.menu_app_list);
        setSupportActionBar(mTitleBar);
        mTitleBar.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.menu_is_show_system_app) {
                    mIsContainsSystemApp = !mIsContainsSystemApp;
                    menuItem.setChecked(mIsContainsSystemApp);
                    matchingApp();
                    AppInfoConfig.saveIsContainsSystemApp(mIsContainsSystemApp);
                } else if (menuItem.getItemId() == R.id.menu_app_list_show_strategy) {
                    if (mStrategy == AppInfoConfig.LAYOUT_STRATEGY_GRID) {
                        mStrategy = AppInfoConfig.LAYOUT_STRATEGY_LINEAR;
                    } else {
                        mStrategy = AppInfoConfig.LAYOUT_STRATEGY_GRID;
                    }
                    invalidateOptionsMenu();
                }
                return true;
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new AppListAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(DKAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra(AppListActivity.RESULT_EXTRA_APP_INFO,
                        ((AppItem) adapter.getItem(position)).getPackageName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        if (mStrategy == AppInfoConfig.LAYOUT_STRATEGY_LINEAR) {
            mRvAppList.setLayoutManager(getLinearLayoutManager());
        } else {
            mRvAppList.setLayoutManager(getGridLayoutManager());
        }
        mAdapter.bindToRecyclerView(mRvAppList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app_list, menu);
        mMenuShowStrategy = menu.findItem(R.id.menu_app_list_show_strategy);
        MenuItem menuIsShowSystemApp = menu.findItem(R.id.menu_is_show_system_app);
        menuIsShowSystemApp.setChecked(mIsContainsSystemApp);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mAdapter.setStrategy(mStrategy);
        if (mStrategy == AppInfoConfig.LAYOUT_STRATEGY_LINEAR) {
            mRvAppList.setLayoutManager(getLinearLayoutManager());
            mMenuShowStrategy.setIcon(R.drawable.dk_app_info_menu_app_list_gird);
        } else {
            mRvAppList.setLayoutManager(getGridLayoutManager());
            mMenuShowStrategy.setIcon(R.drawable.dk_app_info_menu_app_list_linear);
        }
        AppInfoConfig.saveLastShowStrategy(mStrategy);
        return true;
    }

    @Override
    protected void initData() {
        mRvAppList.postDelayed(new Runnable() {
            @Override
            public void run() {
                matchingApp();
                mLoadService.showSuccess();
            }
        }, 100);
    }

    private void matchingApp() {
        String keyword = mEtSearchApp.getText().toString();
        List<AppItem> appItems;
        if (mIsContainsSystemApp) {
            appItems = getAllAppItems();
        } else {
            appItems = getNonSystemAppItems();
        }
        if (keyword.length() == 0) {
            mAdapter.replaceData(appItems);
        } else {
            List<AppItem> items = new ArrayList<>();
            for (AppItem appItem : appItems) {
                if (appItem.getName().contains(keyword)) {
                    items.add(appItem);
                }
            }
            mAdapter.replaceData(items);
        }
    }

    private List<AppItem> getNonSystemAppItems() {
        if (mNonSystemAppItems == null) {
            mNonSystemAppItems = getAppItems(false);
        }
        return mNonSystemAppItems;
    }

    private List<AppItem> getAllAppItems() {
        if (mAllAppItems == null) {
            mAllAppItems = getAppItems(true);
        }
        return mAllAppItems;
    }

    private List<AppItem> getAppItems(boolean isContainsSystemApp) {
        PackageManager pm = getApplication().getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        List<AppItem> appItems;
        if (isContainsSystemApp) {
            appItems = new ArrayList<>(200);
        } else {
            appItems = new ArrayList<>(128);
        }
        for (PackageInfo pi : installedPackages) {
            if (isContainsSystemApp) {
                if ((ApplicationInfo.FLAG_SYSTEM & pi.applicationInfo.flags) != 0) {
                    AppItem appItem = new AppItem(pi.applicationInfo.loadLabel(pm).toString(),
                            pi.packageName, pi.applicationInfo.loadIcon(pm));
                    appItems.add(appItem);
                    continue;
                }
            } else {
                AppItem appItem = new AppItem(pi.applicationInfo.loadLabel(pm).toString(),
                        pi.packageName, pi.applicationInfo.loadIcon(pm));
                appItems.add(appItem);
            }
        }
        return appItems;
    }

    private LayoutManager getLinearLayoutManager() {
        if (mLinearLayoutManager == null) {
            mLinearLayoutManager = new LinearLayoutManager(this);
        }
        return mLinearLayoutManager;
    }

    private LayoutManager getGridLayoutManager() {
        if (mGridLayoutManager == null) {
            mGridLayoutManager = new GridLayoutManager(this, 4);
        }
        return mGridLayoutManager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRvAppList.getHandler().removeCallbacksAndMessages(null);
    }
}
