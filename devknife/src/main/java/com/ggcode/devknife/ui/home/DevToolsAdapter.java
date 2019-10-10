package com.ggcode.devknife.ui.home;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.ggcode.devknife.R;
import com.ggcode.devknife.config.KnifeSortConfig;
import com.ggcode.devknife.manager.init.Category;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.manager.init.KnifeGroup;
import com.ggcode.devknife.ui.base.adapter.DKAdapter;
import com.ggcode.devknife.ui.base.adapter.DKViewHolder;
import com.ggcode.devknife.ui.base.sort.ItemDragHelperCallback;
import com.ggcode.devknife.ui.base.sort.OnItemMoveListener;
import java.util.List;

/**
 * @author: zbb 33775
 * @date: 2019/4/16 16:03
 * @desc:
 */
public class DevToolsAdapter extends DKAdapter<KnifeGroup, DKViewHolder> implements OnItemMoveListener {

    public DevToolsAdapter(List<KnifeGroup> data) {
        super(data);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == Category.EXIT) {
            return inflater.inflate(R.layout.dk_item_close, parent, false);
        } else {
            return inflater.inflate(R.layout.dk_item_dev_tool, parent, false);
        }
    }

    @Override
    protected void convert(DKViewHolder helper, KnifeGroup item) {
        if (helper.getItemViewType() != Category.EXIT) {
            helper.setText(R.id.tv_title, item.getName());
            RecyclerView recyclerView = helper.getView(R.id.rv_knife);
            if (recyclerView.getAdapter() == null) {
                recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
                new ItemTouchHelper(new ItemDragHelperCallback()).attachToRecyclerView(recyclerView);
                KnifeAdapter knifeAdapter = new KnifeAdapter();
                knifeAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DKAdapter adapter, View view, int position) {
                        ((Knife) adapter.getItem(position)).onClick(mContext);
                    }
                });
                knifeAdapter.bindToRecyclerView(recyclerView);
            }
            ((DKAdapter) recyclerView.getAdapter()).setNewData(item.getKnives());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getCategory();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        KnifeGroup fromItem = mData.get(fromPosition);
        mData.remove(fromPosition);
        mData.add(toPosition, fromItem);
        notifyItemMoved(fromPosition, toPosition);
        KnifeSortConfig.saveKnifeSort();
    }
}
