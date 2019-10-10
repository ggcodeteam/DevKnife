package com.ggcode.devknife.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import com.ggcode.devknife.R;
import com.ggcode.devknife.manager.init.Category;
import com.ggcode.devknife.manager.init.Knife;
import com.ggcode.devknife.manager.init.KnifeGroup;
import com.ggcode.devknife.manager.init.KnifeInitManager;
import com.ggcode.devknife.ui.base.adapter.DKAdapter;
import com.ggcode.devknife.ui.base.adapter.DKAdapter.OnItemClickListener;
import com.ggcode.devknife.ui.base.sort.ItemDragHelperCallback;
import com.ggcode.devknife.ui.base.sort.OnItemMoveListener;
import com.ggcode.devknife.utils.DKUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zbb 33775
 * @date: 2019/4/15 16:03
 * @desc:
 */
public class DevToolsActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_APP_INFO = 1000;


    private RecyclerView mRvDevTools;
    private DevToolsAdapter mDevToolsAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, DevToolsActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dk_activity_dev_tools);
        mRvDevTools = findViewById(R.id.rv_dev_tools);
        initView();
    }

    private void initView() {
        mDevToolsAdapter = new DevToolsAdapter(KnifeInitManager.getSortKnifeGroup());
        mDevToolsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(DKAdapter adapter, View view, int position) {
                if (adapter.getItemViewType(position) == Category.EXIT) {
                    ((KnifeGroup) adapter.getItem(position)).getKnives().get(0).onClick(adapter.getContext());
                }
            }
        });
        mRvDevTools.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(new ItemDragHelperCallback() {
            @Override
            public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
                if (recyclerView.getAdapter() instanceof OnItemMoveListener) {
                    OnItemMoveListener listener = ((OnItemMoveListener) recyclerView.getAdapter());
                    listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                }
                return true;
            }
        }).attachToRecyclerView(mRvDevTools);
        mDevToolsAdapter.bindToRecyclerView(mRvDevTools);
    }

    @Override
    protected void onDestroy() {
        mDevToolsAdapter.replaceData(new ArrayList<KnifeGroup>(0));
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DKUtils.finish(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<KnifeGroup> knifeGroups = mDevToolsAdapter.getData();
        for (KnifeGroup knifeGroup : knifeGroups) {
            List<Knife> knives = knifeGroup.getKnives();
            for (Knife knife : knives) {
                if (knife.onRequestPermissionsResult(this, requestCode, permissions, grantResults)) {
                    return;
                }
            }
        }
    }
}

