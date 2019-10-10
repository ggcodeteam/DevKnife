package com.ggcode.devknife.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ggcode.devknife.R;
import com.ggcode.devknife.config.KnifeSortConfig;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.ui.base.adapter.DKAdapter;
import com.ggcode.devknife.ui.base.adapter.DKViewHolder;
import com.ggcode.devknife.ui.base.sort.OnItemMoveListener;

/**
 * @author: zbb 33775
 * @date: 2019/4/16 13:38
 * @desc:
 */
public class KnifeAdapter extends DKAdapter<Knife, DKViewHolder> implements OnItemMoveListener {

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return inflater.inflate(R.layout.dk_item_knife, parent, false);
    }

    @Override
    protected void convert(DKViewHolder helper, Knife item) {
        helper.setText(R.id.tv_name, item.getName());
        if (item.getIcon() != 0) {
            helper.setImageDrawable(R.id.iv_icon, mContext.getResources().getDrawable(item.getIcon()));
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Knife fromItem = mData.get(fromPosition);
        Knife toItem = mData.get(fromPosition);
        mData.remove(fromPosition);
        mData.add(toPosition, fromItem);
        fromItem.setPriority(toItem.getPriority() + 1);
        notifyItemMoved(fromPosition, toPosition);
        KnifeSortConfig.saveKnifeSort();
    }
}
