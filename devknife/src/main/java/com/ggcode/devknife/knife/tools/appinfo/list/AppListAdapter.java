package com.ggcode.devknife.knife.tools.appinfo.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ggcode.devknife.R;
import com.ggcode.devknife.knife.tools.appinfo.AppInfoConfig;
import com.ggcode.devknife.knife.tools.appinfo.AppInfoConfig.LayoutType;
import com.ggcode.devknife.ui.base.adapter.DKAdapter;
import com.ggcode.devknife.ui.base.adapter.DKViewHolder;

/**
 * @author: zbb 33775
 * @date: 2019/5/28 15:47
 * @desc:
 */
public class AppListAdapter extends DKAdapter<AppItem, DKViewHolder> {

    private int mStrategy;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (mStrategy == AppInfoConfig.LAYOUT_STRATEGY_GRID) {
            return inflater.inflate(R.layout.dk_item_app_list_grid, parent, false);
        } else {
            return inflater.inflate(R.layout.dk_item_app_list_linear, parent, false);
        }
    }

    @Override
    protected void convert(DKViewHolder helper, AppItem item) {
        helper.setImageDrawable(R.id.iv_app_icon, item.getIcon())
                .setText(R.id.tv_app_name, item.getName());
        TextView tvPackageName = helper.getView(R.id.tv_package_name);
        if (tvPackageName != null) {
            tvPackageName.setText(item.getPackageName());
        }
    }

    public void setStrategy(@LayoutType int strategy) {
        mStrategy = strategy;
    }

    @Override
    public int getItemViewType(int position) {
        return mStrategy;
    }
}
