package com.ggcode.devknife.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import com.ggcode.devknife.R;
import com.ggcode.devknife.base.fragment.Fragmentation;
import com.ggcode.devknife.constant.FragmentLabel;
import com.ggcode.devknife.knife.ui.colorpicker.ColorPickerSettingFragment;
import com.ggcode.devknife.utils.ToastUtil;
import com.ggcode.devknife.utils.UIUtils;
import java.util.ArrayDeque;
import java.util.List;

/**
 * @author: zbb 33775
 * @date: 2019/4/27 23:23
 * @desc: 不希望出现更多的东西（例如四大组件）在 no-op 中，所以没有对外提供拓展。
 */
public class ContainerActivity extends AppCompatActivity {

    private ArrayDeque<BaseFragment> mFragments = new ArrayDeque<>();

    private static final String EXTRA_FRAGMENT_LABEL = "extra_fragment_label";
    private static final String EXTRA_FRAGMENT_BUNDLE_DATA = "extra_fragment_bundle_data";

    public static void start(Context context, int label) {
        start(context, label, false);
    }

    public static void start(Context context, int label, @Nullable Bundle data) {
        start(context, label, data, false);
    }

    public static void start(Context context, int label, boolean isTransparent) {
        start(context, label, null, isTransparent);
    }

    public static void start(Context context, int label, @Nullable Bundle data, boolean isTransparent) {
        Intent intent;
        if (isTransparent) {
            intent = new Intent(context, TransparentActivity.class);
        } else {
            intent = new Intent(context, ContainerActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_FRAGMENT_LABEL, label);
        if (data != null) {
            intent.putExtra(EXTRA_FRAGMENT_BUNDLE_DATA, data);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        int label = bundle.getInt(EXTRA_FRAGMENT_LABEL);

        Class<? extends BaseFragment> fragmentClass = getFragmentClass(label);
        if (fragmentClass == null) {
            finish();
            ToastUtil.show(String.format(UIUtils.getString(R.string.dk_fragment_label_not_found_hint), label));
            return;
        }

        if (savedInstanceState == null) {
            Fragment fragment;
            try {
                fragment = Fragment.instantiate(
                        this, fragmentClass.getName(), getIntent().getBundleExtra(EXTRA_FRAGMENT_BUNDLE_DATA));
                fragment.setRetainInstance(true);
            } catch (Exception e) {
                finish();
                ToastUtil.show(String.format(UIUtils.getString(R.string.dk_fragment_label_not_found_hint), label));
                return;
            }
            Fragmentation.with(this)
                    .setAnimations(R.anim.dk_fragment_enter, R.anim.dk_fragment_exit,
                            R.anim.dk_fragment_pop_enter, R.anim.dk_fragment_pop_exit)
                    .add(android.R.id.content, fragment)
                    .commit();
            if (fragment instanceof BaseFragment) {
                mFragments.push((BaseFragment) fragment);
            } else {
                throw new IllegalStateException("fragment please extends BaseFragment");
            }
        } else {
            Fragment fragment = findFragment(fragmentClass);

            Fragmentation.with(this)
                    .setAnimations(R.anim.dk_fragment_enter, R.anim.dk_fragment_exit,
                            R.anim.dk_fragment_pop_enter, R.anim.dk_fragment_pop_exit)
                    .show(fragment)
                    .commit();
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (int i = fragments.size() - 1; i >= 0; i--) {
                mFragments.push((BaseFragment) fragments.get(i));
            }
        }
    }

    private Class<? extends BaseFragment> getFragmentClass(int label) {
        switch (label) {
            case FragmentLabel.COLOR_PICKTER:
                return ColorPickerSettingFragment.class;
            default:
                return null;
        }
    }

    public Fragment findFragment(Class<? extends Fragment> fragmentClass) {
        return Fragmentation.findFragment(getSupportFragmentManager(), fragmentClass);
    }

    @Override
    public void onBackPressed() {
        BaseFragment first = mFragments.getFirst();
        if (!first.onBackPressed()) {
            finishFragment(first);
        }
    }

    public void finishFragment(BaseFragment baseFragment) {
        for (BaseFragment fragment : mFragments) {
            if (fragment == baseFragment) {
                Fragmentation.with(this)
                        .remove(baseFragment)
                        .commitNow();
                mFragments.remove(baseFragment);
                if (mFragments.isEmpty()) {
                    finish();
                }
            }
        }
    }
}
