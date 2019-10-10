package com.ggcode.devknife.knife.tools.appinfo.info;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ggcode.devknife.R;
import com.ggcode.devknife.ui.base.adapter.DKAdapter;
import com.ggcode.devknife.ui.base.adapter.DKViewHolder;

/**
 * @author: zbb 33775
 * @date: 2019/5/28 9:55
 * @desc:
 */
public class AppInfoAdapter extends DKAdapter<InfoItem, DKViewHolder> {

    private final static int TYPE_ITEM = 0;
    private final static int TYPE_TITLE = 1;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return inflater.inflate(R.layout.dk_item_app_info, parent, false);
        } else {
            return inflater.inflate(R.layout.dk_item_app_info_title, parent, false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof TitleItem) {
            return TYPE_TITLE;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    protected void convert(DKViewHolder helper, InfoItem item) {
        if (helper.getItemViewType() == TYPE_ITEM) {
            helper.setText(R.id.tv_name, item.getName())
                    .setText(R.id.tv_value, item.getValue());
        } else {
            helper.setText(R.id.tv_title, item.getName());
        }
    }
}
